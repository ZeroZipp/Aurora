package com.zerozipp.client.utils.screen;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.*;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.settings.*;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Window {
    public static Color cGray = new Color(80, 80, 80, 255);
    public static Color cBorder = new Color(180, 180, 180, 255);
    public static Color cHandle = new Color(233, 233, 233, 255);
    public static Color cToggle = new Color(200, 200, 200, 255);
    public static Color cSetting = new Color(250, 250, 250, 255);
    public static Color cListing = new Color(240, 240, 240, 255);
    public static Color cEnabled = new Color(220, 220, 220, 255);
    public static Color cModule = new Color(255, 255, 255, 255);
    public static Color cActive = new Color(100, 150, 238, 255);
    public static Color cText = new Color(30, 30, 30, 255);
    public static Color cKey = new Color(50, 50, 50, 50);
    public static final float width = 75;
    public static final float height = 11;
    public static final float handle = 16;
    public static final float size = 95;
    private float x, y, scroll, last;
    public final Category category;
    private Keybind keybind;

    public Window(Category category, float x, float y) {
        this.category = category;
        this.keybind = null;
        this.scroll = 0;
        this.last = 0;
        this.x = x;
        this.y = y;
    }

    public void draw(Render font, float w, float h, int x, int y, float ticks) {
        final float width = w / 2 + this.x, height = h / 12 + this.y;
        drawModules(font, width, height, x, y, ticks);
        drawHandle(font, width, height, ticks);
        this.last = this.scroll;
    }

    private void drawHandle(Render font, float x, float y, float ticks) {
        final float w = width / 2, h = handle / 2, f = font.floatFontHeight() / 2;
        final int handle = cHandle.getColor(), text = cText.getColor();
        Display.drawPinRect(x - w, y - h, x + w, y + h, 1, handle);
        font.drawString(category.name, x - w + 6, y - f, text, false);
    }

    private void drawModules(Render font, float x, float y, float mX, float mY, float ticks) {
        float w = width / 2, h = height / 2, s = handle / 2, r = 1, index = 0;
        int color = cModule.getColor(), text = cText.getColor(), gray = cGray.getColor();
        Display.drawPinRect(x - w, y - s, x + w, y + s + size, r, color);
        int st = cBorder.addColor(new Color(0, 0, 0, -255)).getColor();
        Resolution res = new Resolution(Invoker.client.MC());
        float height = res.getHeight();
        float scale = res.getFactor();
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double pos = last + (scroll - last) * ticks;
        int sy = (int) ((height - y - s - size + r) * scale);
        int sx = (int) ((x - w) * scale);
        int sw = (int) (width * scale);
        int sh = (int) ((size) * scale);
        GL11.glTranslated(0, pos, 0);
        GL11.glScissor(sx, sy, sw, sh);
        for(Module m : category.modules) {
            index += drawModule(m, index, font, x, y, mX, mY, ticks);
            boolean o = m.isOpened() && !m.settings.isEmpty();
            float start = y + s + 3 + index;
            int border = cBorder.getColor();
            if(o) for(Setting e : m.settings) {
                index += drawSetting(e, index, font, x, y, ticks);
            } float end = y + s + 3 + index, ps = x - w, pe = x + w;
            if(o) Display.drawRect(ps, start, pe, start + 1, border, st);
            if(o) Display.drawRect(ps, end - 1, pe, end, st, border);
        } GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        float rl = x - w, rt = y + s - r;
        float rr = x + w, rb = y + s + r;
        Display.drawRect(rl, rt, rr, rb, gray, 0);
    }

    private float drawModule(Module module, float index, Render font, float x, float y, float mX, float mY, float ticks) {
        boolean isKey = module.keybind.getKey() != null, a = module.keybind.equals(keybind), v = module.isActive();
        String key = a ? "ANY" : isKey ? Keyboard.getKeyName(module.keybind.getKey()) : "NO";
        final float w = width / 2, h = height / 2, s = handle / 2;
        final float f = font.floatFontHeight() / 2, p = y + s + 3 + index;
        int c = cKey.addColor(new Color(0, 0, 0, -50)).getColor(), t = cKey.getColor();
        if(v) Display.drawRect(x - w, p, x + w, p + height, cActive.getColor());
        font.drawString(module.name, x - w + 5, p + h - f, cText.getColor(), false);
        if(module.keybind.equals(keybind)) Display.verticalRect(x + w / 2, p, x + w, p + height, c, t);
        font.drawString(key, x + w - 5 - font.getStringWidth(key), p + h - f, cText.getColor(), false);
        return height;
    }

    private float drawSetting(Setting setting, float index, Render font, float x, float y, float ticks) {
        final float w = width / 2, h = height / 2, s = handle / 2;
        final float f = font.floatFontHeight() / 2, p = y + s + 3 + index;
        int color = cSetting.getColor(), text = cText.getColor(), active = cActive.getColor();
        Display.drawRect(x - w, p, x + w, p + setting.getHeight(), color);
        font.drawString(setting.name, x - w + 4, p + h - f, text, false);
        if(setting instanceof Toggle) {
            Toggle t = (Toggle) setting;
            int c = t.isActive() ? active : cToggle.getColor();
            Display.drawCircle(x + w - 6, p + h, 4, c);
            Display.drawCircle(x + w - 6, p + h, 2, -1);
        } else if(setting instanceof Option) {
            Option o = (Option) setting;
            String type = o.values[o.getIndex()];
            float dw = x + w - 4 - font.getStringWidth(type);
            int c = cToggle.addColor(new Color(0, 0, 0, -255)).getColor();
            Display.verticalRect(x, p, x + w, p + height, c, cToggle.getColor());
            font.drawString(type, dw, p + h - f, text, false);
        } else if(setting instanceof Value) {
            Value v = (Value) setting;
            String str = Float.toString(v.getValue());
            float dw = x + w - 4 - font.getStringWidth(str);
            double pos = v.getLast() + (v.getValue() - v.getLast()) * ticks;
            float size = (((float) pos - v.min) / (v.max - v.min)) * width;
            Display.drawRect(x - w, p + 11, x + w, p + 13, cToggle.getColor());
            Display.drawRect(x - w, p + 11, x - w + size, p + 13, active);
            Display.drawCircle(x - w + size, p + 12, 3, active);
            font.drawString(str, dw, p + h - f, text, false);
        } else if(setting instanceof Active) {
            drawActive((Active) setting, font, index, x, y, ticks);
        } return setting.getHeight();
    }

    private void drawActive(Active a, Render font, float index, float x, float y, float ticks) {
        int color = cListing.getColor(), text = cText.getColor(), active = cEnabled.getColor();
        int st = cBorder.addColor(new Color(0, 0, 0, -255)).getColor();
        final float w = width / 2, h = height / 2, s = handle / 2;
        final float f = font.floatFontHeight() / 2;
        float start = y + s + 3 + index + height;
        int border = cBorder.getColor();
        for(Active.Listing l : a.listings) {
            index += l.getHeight();
            float p = y + s + 3 + index;
            int c = l.isActive() ? active : color;
            Display.drawRect(x - w, p, x + w, p + l.getHeight(), c);
            font.drawString(l.name, x - w + 4, p + h - f, text, false);
        } float end = y + s + 3 + index + height, ps = x - w, pe = x + w;
        Display.drawRect(ps, start, pe, start + 1, border, st);
        Display.drawRect(ps, end - 1, pe, end, st, border);
    }

    public void onMouse(float w, float h, int x, int y, int button) {
        float width = w / 2 + this.x, height = h / 12 + this.y;
        float ws = Window.width / 2, hs = handle / 2;
        if(keybind != null) keybind = null;
        if(x > width - ws && x < width + ws) {
            float index = scroll + hs + 3;
            float hx = Window.height;
            if(y > height + hs && y < height + hs + size) {
                for(Module mod : category.modules) {
                    if(y > height + index && y < height + index + hx) {
                        boolean isKey = !mod.keybind.equals(keybind);
                        if(button == 0) mod.setActive(!mod.isActive());
                        if(button == 1) mod.setOpened(!mod.isOpened());
                        if(button == 2 && isKey) keybind = mod.keybind;
                    } index += Window.height;
                    if(mod.isOpened() && !mod.settings.isEmpty()) {
                        for(Setting s : mod.settings) {
                            if(y > height + index && y < height + index + s.getHeight()) {
                                float pos = (x - (width - ws)) / Window.width * 100;
                                if(s instanceof Active) {
                                    Active a = (Active) s;
                                    onActive(a, index, h, y, button);
                                } onSetting(s, button, pos);
                            } index += s.getHeight();
                        }
                    }
                }
            }
        }
    }

    private void onActive(Active s, float index, float h, int y, int button) {
        float height = h / 12 + this.y;
        for(Active.Listing l : s.listings) {
            index += l.getHeight();
            if(y > height + index && y < height + index + l.getHeight()) {
                if(button == 0) l.setActive(!l.isActive());
            }
        }
    }

    public void onMouseDrag(float w, float h, int x, int y, int button) {
        float width = w / 2 + this.x, height = h / 12 + this.y;
        float ws = Window.width / 2, hs = handle / 2;
        if(x > width - ws && x < width + ws) {
            float index = scroll + hs + 3;
            if(y > height + hs && y < height + hs + size) {
                for(Module mod : category.modules) {
                    index += Window.height;
                    if(mod.isOpened() && !mod.settings.isEmpty()) {
                        for(Setting s : mod.settings) {
                            if(y > height + index && y < height + index + s.getHeight()) {
                                float pos = (x - (width - ws)) / Window.width * 100;
                                if(s instanceof Value) onSetting(s, button, pos);
                            } index += s.getHeight();
                        }
                    }
                }
            }
        }
    }

    private void onSetting(Setting setting, int button, float pos) {
        if(setting instanceof Toggle) {
            Toggle s = (Toggle) setting;
            if(button == 0) s.setActive(!s.isActive());
        } else if(setting instanceof Value) {
            Value s = (Value) setting;
            float value = (s.max - s.min) * (pos / 100);
            double v = Math.round(10 * (value + s.min)) * 0.1;
            if(button == 0) s.setValue((float) v);
        } else if(setting instanceof Option) {
            Option s = (Option) setting;
            if(button == 0) s.setIndex(s.getIndex() + 1);
            if(button == 1) s.setIndex(s.getIndex() - 1);
        }
    }

    public void onMouseWheel(float mouse, float x, float y, float w, float h) {
        final float width = w / 2 + this.x, height = h / 12 + this.y;
        final float ws = Window.width / 2, hs = handle / 2;
        if(x > width - ws && x < width + ws) {
            if(y > height + hs && y < height + hs + size) {
                this.scroll += mouse * 2;
                scroll = Math.min(scroll, 0);
            }
        }
    }

    public void onKeyboard(int key) {
        if(keybind == null) return;
        if(Keyboard.getKeyName(key).length() == 1) {
            keybind.setKey(key);
            keybind = null;
        } else if(key == 211) {
            keybind.setKey(null);
            keybind = null;
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}