package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Buffer;
import com.zerozipp.client.utils.base.Display;
import com.zerozipp.client.utils.base.Rendering;
import com.zerozipp.client.utils.base.Tessellator;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.utils.Vector3;
import org.lwjgl.opengl.GL11;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Renderer {
    public static void drawNameplate(Object entity, String text, Vector3 pos, Render font, float size, float ticks) {
        int white = new Color(255, 255, 255, 255).getColor();
        int color = new Color(0, 0, 0, 130).getColor();
        Object mc = Invoker.client.MC();
        JClass s = JClass.getClass("settings");
        JClass c = JClass.getClass("minecraft");
        JField v = s.getField("thirdPersonView");
        JField settings = c.getField("mcSettings");
        boolean view = (int) v.get(settings.get(mc)) == 2;
        float width = font.stringWidth(text), w = width / 2;
        Rotation rot = Entity.getView(entity, ticks);
        GL11.glPushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(pos.x, pos.y, pos.z);
        GL11.glRotatef(-rot.yaw, 0, 1, 0);
        GL11.glRotatef(rot.pitch, 1, 0, 0);
        if(view) GL11.glRotatef(180, 0, 1, 0);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glScalef(size, size, size);
        GL11.glTranslatef(-w / 2, 0, 0.02f);
        Display.drawRect(-3, -10, w + 5, 10, color);
        Display.drawRect(-3, 10, w + 5, 11, white);
        GL11.glTranslatef(0, 0, -0.02f);
        font.drawString(text, 0, -10, -1, false);
        GL11.glPopMatrix();
    }

    public static void drawTracer(Vector3 pos1, Vector3 pos2, Color color, float width) {
        int r = color.red / 255, g = color.green / 255, b = color.blue / 255, a = color.alpha / 255;
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        buffer.begin(3, "vertexPositionColor");
        buffer.pos(pos1.x, pos1.y, pos1.z).color(r, g, b, a).end();
        buffer.pos(pos2.x, pos2.y, pos2.z).color(r, g, b, a).end();
        tessellator.draw();
        GL11.glLineWidth(2);
        GL11.glPopMatrix();
    }

    public static void drawCircle(Vector3 pos, double radius, Color color, float width) {
        int r = color.red / 255, g = color.green / 255, b = color.blue / 255, a = color.alpha / 255;
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        Tessellator tessellator = new Tessellator();
        Buffer buffer = new Buffer(tessellator.getBuffer());
        buffer.begin(3, "vertexPositionColor");
        for(float k = 0.0F; k <= 360.0F; k += 0.1F) {
            double xPos = pos.x + radius * Math.cos(Math.toRadians(k));
            double zPos = pos.z - radius * Math.sin(Math.toRadians(k));
            buffer.pos(xPos, pos.y, zPos).color(r, g, b, a).end();
        } tessellator.draw();
        GL11.glLineWidth(2);
        GL11.glPopMatrix();
    }

    public static void pushRenderer() {
        GL11.glPushMatrix();
        Display.pushScreen();
        Rendering.pushIndex(3553);
        Rendering.pushIndex(2884);
        Rendering.pushIndex(2929);
        GL11.glDepthMask(false);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glDisable(2929);
    }

    public static void popRenderer() {
        GL11.glDepthMask(true);
        Rendering.popIndex(3553);
        Rendering.popIndex(2884);
        Rendering.popIndex(2929);
        Display.popScreen();
        GL11.glPopMatrix();
    }
}
