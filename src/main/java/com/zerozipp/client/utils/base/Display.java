package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.utils.Vector2;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import static org.lwjgl.opengl.GL11.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Display {
    public static void drawPinRect(float left, float top, float right, float bottom, float radius, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        float pin = radius / 3;
        glBlendFunc(770, 771);
        glColor4f(var6, var7, var8, var11);
        buffer.begin(GL_POLYGON, "vertexPosition");
        buffer.pos(left, bottom - radius, 0.0).end();
        buffer.pos(left + pin, bottom - pin, 0.0).end();
        buffer.pos(left + radius, bottom, 0.0).end();
        buffer.pos(right - radius, bottom, 0.0).end();
        buffer.pos(right - pin, bottom - pin, 0.0).end();
        buffer.pos(right, bottom - radius, 0.0).end();
        buffer.pos(right, top + radius, 0.0).end();
        buffer.pos(right - pin, top + pin, 0.0).end();
        buffer.pos(right - radius, top, 0.0).end();
        buffer.pos(left + radius, top, 0.0).end();
        buffer.pos(left + pin, top + pin, 0.0).end();
        buffer.pos(left, top + radius, 0.0).end();
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(770, 771);
        glColor4f(var6, var7, var8, var11);
        buffer.begin(7, "vertexPosition");
        buffer.pos(left, bottom, 0.0).end();
        buffer.pos(right, bottom, 0.0).end();
        buffer.pos(right, top, 0.0).end();
        buffer.pos(left, top, 0.0).end();
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(770, 771);
        glColor4f(var6, var7, var8, var11);
        buffer.begin(GL_POLYGON, "vertexPosition");
        for(float k = 0.0F; k <= 360.0F; k += 0.1F) {
            double xPos = x + radius * Math.cos(Math.toRadians(k));
            double yPos = y - radius * Math.sin(Math.toRadians(k));
            buffer.pos(xPos, yPos, 0).end();
        } tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawVector(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(770, 771);
        glColor4f(var6, var7, var8, var11);
        buffer.begin(7, "vertexPosition");
        buffer.pos(p1.x, p1.y, 0.0).end();
        buffer.pos(p2.x, p2.y, 0.0).end();
        buffer.pos(p3.x, p3.y, 0.0).end();
        buffer.pos(p4.x, p4.y, 0.0).end();
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color, int color2) {
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        float var9 = (float) (color >> 24 & 255) / 255.0F;
        float var13 = (float) (color2 >> 24 & 255) / 255.0F;
        float var10 = (float) (color2 >> 16 & 255) / 255.0F;
        float var11 = (float) (color2 >> 8 & 255) / 255.0F;
        float var12 = (float) (color2 & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        glShadeModel(7425);
        glBlendFunc(770, 771);
        buffer.begin(7, "vertexPositionColor");
        buffer.pos(left, bottom, 0.0).color(var10, var11, var12, var13).end();
        buffer.pos(right, bottom, 0.0).color(var10, var11, var12, var13).end();
        buffer.pos(right, top, 0.0).color(var6, var7, var8, var9).end();
        buffer.pos(left, top, 0.0).color(var6, var7, var8, var9).end();
        tessellator.draw();
        glShadeModel(7424);
        glEnable(GL_TEXTURE_2D);
    }

    public static void verticalRect(float left, float top, float right, float bottom, int color, int color2) {
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;
        float var9 = (float) (color >> 24 & 255) / 255.0F;
        float var13 = (float) (color2 >> 24 & 255) / 255.0F;
        float var10 = (float) (color2 >> 16 & 255) / 255.0F;
        float var11 = (float) (color2 >> 8 & 255) / 255.0F;
        float var12 = (float) (color2 & 255) / 255.0F;
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        glDisable(GL_TEXTURE_2D);
        glShadeModel(7425);
        glBlendFunc(770, 771);
        buffer.begin(7, "vertexPositionColor");
        buffer.pos(left, bottom, 0.0).color(var6, var7, var8, var9).end();
        buffer.pos(right, bottom, 0.0).color(var10, var11, var12, var13).end();
        buffer.pos(right, top, 0.0).color(var10, var11, var12, var13).end();
        buffer.pos(left, top, 0.0).color(var6, var7, var8, var9).end();
        tessellator.draw();
        glShadeModel(7424);
        glEnable(GL_TEXTURE_2D);
    }

    public static void pushScreen() {
        Rendering.pushIndex(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        glColor4f(1, 1, 1, 1);
    }

    public static void popScreen() {
        glEnable(GL_ALPHA_TEST);
        Rendering.popIndex(GL_BLEND);
        glDepthMask(true);
        glColor4f(1, 1, 1, 1);
    }
}
