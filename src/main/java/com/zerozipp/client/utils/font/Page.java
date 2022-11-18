package com.zerozipp.client.utils.font;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Page {
    private int imgSize;
    private int maxFontHeight = -1;
    private final Font font;
    private final boolean antiAliasing;
    private final boolean fractionalMetrics;
    private final HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();
    private BufferedImage bufferedImage;
    private Object loadedTexture;

    public Page(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars) {
        double maxWidth = -1;
        double maxHeight = -1;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, antiAliasing, fractionalMetrics);
        for(char ch : chars) {
            Rectangle2D bounds = font.getStringBounds(Character.toString(ch), fontRenderContext);
            if(maxWidth < bounds.getWidth()) maxWidth = bounds.getWidth();
            if(maxHeight < bounds.getHeight()) maxHeight = bounds.getHeight();
        }

        maxWidth += 2;
        maxHeight += 2;
        double tmp1 = Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth);
        double tmp2 = Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight);
        imgSize = (int) Math.ceil(Math.max(tmp1, tmp2) * Math.max(maxWidth, maxHeight)) + 1;
        bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.white);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FontMetrics fontMetrics = g.getFontMetrics();
        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;

        for(char ch : chars) {
            Glyph glyph = new Glyph();
            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);
            glyph.width = bounds.getBounds().width + 8;
            glyph.height = bounds.getBounds().height;
            if(posY + glyph.height >= imgSize) {
                throw new IllegalStateException("Not all characters will fit");
            }

            if(posX + glyph.width >= imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }

            glyph.x = posX;
            glyph.y = posY;
            if(glyph.height > maxFontHeight) maxFontHeight = glyph.height;
            if(glyph.height > currentCharHeight) currentCharHeight = glyph.height;
            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());
            posX += glyph.width;
            glyphCharacterMap.put(ch, glyph);
        }
    }

    public void setupTexture() {
        JClass j = JClass.getClass("texture");
        Constructor<?> c = j.getConstructor(BufferedImage.class);
        loadedTexture = j.newInstance(c, bufferedImage);
    }

    public void bindTexture() {
        Object texture = JClass.getClass("texture").getMethod("getGlTextureId").call(loadedTexture);
        JClass.getClass("glState").getMethod("bindTexture", int.class).call(null, texture);
    }

    public void unbindTexture() {
        JClass.getClass("glState").getMethod("bindTexture", int.class).call(null, 0);
    }

    public float drawChar(char ch, float x, float y) {
        Glyph glyph = glyphCharacterMap.get(ch);
        if(glyph == null) throw new IllegalArgumentException("'" + ch + "' wasn't found");
        float pageX = glyph.x / (float) imgSize;
        float pageY = glyph.y / (float) imgSize;
        float pageWidth = glyph.width / (float) imgSize;
        float pageHeight = glyph.height / (float) imgSize;
        float width = glyph.width;
        float height = glyph.height;
        glBegin(GL_TRIANGLES);
        glTexCoord2f(pageX + pageWidth, pageY);
        glVertex2f(x + width, y);
        glTexCoord2f(pageX, pageY);
        glVertex2f(x, y);
        glTexCoord2f(pageX, pageY + pageHeight);
        glVertex2f(x, y + height);
        glTexCoord2f(pageX, pageY + pageHeight);
        glVertex2f(x, y + height);
        glTexCoord2f(pageX + pageWidth, pageY + pageHeight);
        glVertex2f(x + width, y + height);
        glTexCoord2f(pageX + pageWidth, pageY);
        glVertex2f(x + width, y);
        glEnd();
        return width - 8;
    }

    public float getWidth(char ch) {
        return glyphCharacterMap.get(ch).width;
    }

    public int getMaxFontHeight() {
        return maxFontHeight;
    }

    public boolean isAntiAliasingEnabled() {
        return antiAliasing;
    }

    public boolean isFractionalMetricsEnabled() {
        return fractionalMetrics;
    }

    static class Glyph {
        private int x;
        private int y;
        private int width;
        private int height;

        Glyph(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Glyph() {}

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}