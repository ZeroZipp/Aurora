package com.zerozipp.client.utils.font;

import com.zerozipp.client.utils.base.Buffer;
import com.zerozipp.client.utils.base.Tessellator;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import java.awt.*;
import java.util.Locale;
import static org.lwjgl.opengl.GL11.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Render {
    private float posX;
    private float posY;
    private final int[] colorCode = new int[32];
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    private final Page regularGlyphPage, boldGlyphPage, italicGlyphPage, boldItalicGlyphPage;

    public Render(Page regularGlyphPage, Page boldGlyphPage, Page italicGlyphPage, Page boldItalicGlyphPage) {
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;

        for(int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if(i == 6) k += 85;
            if(i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            int tmp = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
            this.colorCode[i] = tmp;
        }
    }

    public static Render create(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        char[] chars = new char[256];
        for(int i = 0; i < chars.length; i++) chars[i] = (char) i;
        Page regularPage;
        regularPage = new Page(font, antiAliasing, fractionalMetrics);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();
        Page boldPage = regularPage;
        Page italicPage = regularPage;
        Page boldItalicPage = regularPage;
        return new Render(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public void drawString(String text, float x, float y, int color, boolean dropShadow) {
        JClass.getClass("glState").getMethod("enableAlpha").call(null);
        this.resetStyles();
        if(dropShadow) {
            int i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            this.renderString(text, x, y, color, false);
        } else this.renderString(text, x, y, color, false);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if(text == null) return 0;
        if((color & -67108864) == 0) color |= -16777216;
        if(dropShadow) color = (color & 16579836) >> 2 | color & -16777216;

        this.red = (float) (color >> 16 & 255) / 255.0F;
        this.blue = (float) (color >> 8 & 255) / 255.0F;
        this.green = (float) (color & 255) / 255.0F;
        this.alpha = (float) (color >> 24 & 255) / 255.0F;
        glColor4d(this.red, this.blue, this.green, this.alpha);
        this.posX = x * 2.0f;
        this.posY = y * 2.0f;
        this.renderStringAtPos(text, dropShadow);
        return (int) (this.posX / 4.0f);
    }

    private void renderStringAtPos(String text, boolean shadow) {
        Page glyphPage = getCurrentGlyphPage();
        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glyphPage.bindTexture();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        for(int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            if(c0 == 167 && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if(i1 < 16) {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if(i1 < 0) i1 = 15;
                    if(shadow) i1 += 16;
                    int j1 = this.colorCode[i1];
                    glColor4d((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, this.alpha);
                } else if(i1 == 17) this.boldStyle = true;
                else if(i1 == 18) this.strikethroughStyle = true;
                else if(i1 == 19) this.underlineStyle = true;
                else if(i1 == 20) {
                    this.italicStyle = true;
                } else {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    glColor4d(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                glyphPage = getCurrentGlyphPage();
                glyphPage.bindTexture();
                float f = glyphPage.drawChar(c0, posX, posY);
                doDraw(f, glyphPage);
            }
        }

        glyphPage.unbindTexture();
        glPopMatrix();
    }

    private void doDraw(float f, Page glyphPage) {
        if(this.strikethroughStyle) {
            Tessellator object = new Tessellator();
            Buffer buffer = new Buffer(object.getBuffer());
            glDisable(GL_TEXTURE_2D);
            buffer.begin(7, "vertexPosition");
            buffer.pos(this.posX, this.posY + (float) (glyphPage.getMaxFontHeight() / 2), 0.0D);
            buffer.pos(this.posX + f, this.posY + (float) (glyphPage.getMaxFontHeight() / 2), 0.0D);
            buffer.pos(this.posX + f, this.posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0D);
            buffer.pos(this.posX, this.posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0D);
            object.draw();
            glEnable(GL_TEXTURE_2D);
        }

        if(this.underlineStyle) {
            Tessellator object = new Tessellator();
            Buffer buffer = new Buffer(object.getBuffer());
            glDisable(GL_TEXTURE_2D);
            buffer.begin(7, "vertexPosition");
            int l = this.underlineStyle ? -1 : 0;
            buffer.pos(this.posX + (float) l, this.posY + (float) glyphPage.getMaxFontHeight(), 0.0D);
            buffer.pos(this.posX + f, this.posY + (float) glyphPage.getMaxFontHeight(), 0.0D);
            buffer.pos(this.posX + f, this.posY + (float) glyphPage.getMaxFontHeight() - 1.0F, 0.0D);
            buffer.pos(this.posX + (float) l, this.posY + (float) glyphPage.getMaxFontHeight() - 1.0F, 0.0D);
            object.draw();
            glEnable(GL_TEXTURE_2D);
        }

        this.posX += f;
    }

    private Page getCurrentGlyphPage() {
        if(boldStyle && italicStyle) return boldItalicGlyphPage;
        else if(boldStyle) return boldGlyphPage;
        else if(italicStyle) return italicGlyphPage;
        else return regularGlyphPage;
    }

    private void resetStyles() {
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    public int getFontHeight() {
        return regularGlyphPage.getMaxFontHeight() / 2;
    }

    public int getStringWidth(String text) {
        return (int) stringWidth(text) / 2;
    }

    public float floatStringWidth(String text) {
        return stringWidth(text) / 2;
    }

    public float stringWidth(String text) {
        if(text == null) return 0;
        float width = 0;
        Page currentPage;
        float size = text.length();
        boolean on = false;

        for(int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if(character == '§') on = true;
            else if(on && character >= '0' && character <= 'r') {
                on = false;
            } else {
                character = text.charAt(i);
                currentPage = getCurrentGlyphPage();
                width += currentPage.getWidth(character) - 8;
            }
        }

        return width;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        boolean on = false;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        int width = 0;
        Page currentPage;

        for(int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);
            if(character == '§') on = true;
            else if(on && character >= '0' && character <= 'r') {
                on = false;
            } else {
                if(on) on = false;
                character = text.charAt(i);
                currentPage = getCurrentGlyphPage();
                width += (currentPage.getWidth(character) - 8) / 2;
            }

            if(i > width) break;
            if(reverse) stringbuilder.insert(0, character);
            else stringbuilder.append(character);
        }

        return stringbuilder.toString();
    }
}