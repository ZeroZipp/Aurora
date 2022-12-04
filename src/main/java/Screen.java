import com.zerozipp.client.Client;
import com.zerozipp.client.utils.base.Keybind;
import com.zerozipp.client.utils.base.Setting;
import com.zerozipp.client.utils.settings.*;
import com.zerozipp.client.utils.utils.Color;
import static com.zerozipp.client.utils.base.Display.*;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector2;
import org.lwjgl.input.Keyboard;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Screen extends blk {
    private final float width, height;
    private Keybind keybind = null;
    private Render font = null;

    public Screen() {
        height = 12;
        width = 70;
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public void a(int mouseX, int mouseY, float partialTicks) {
        super.a(mouseX, mouseY, partialTicks);
        pushScreen();
        String f = "font/medium.ttf";
        if(font == null) font = Client.getFont(f, 16);
        drawRect(0, 0, l, m, Color.background.getColor());
        float offset = (float) (Category.values().length - 1) / -2;
        for(Category category : Category.values()) {
            doWindow(category, (width + 3) * offset);
            offset += 1;
        } popScreen();
    }

    private void doWindow(Category category, float offset) {
        float x = (float) l / 2 + offset;
        float y = (float) m / 12;
        float w = width / 2;
        float h = height / 2;
        float f = (float) font.getFontHeight() / 2;
        float t = (float) font.getStringWidth(category.name) / 2;
        drawRect(x - w, y - 8, x + w, y + 8, Color.window.getColor());
        drawRect(x - w, y - 8, x + w, y - 7, Color.border.getColor());
        font.drawString(category.name, x - t, y - f, Color.text.getColor(), false);
        if(category.isClosed()) return;
        float position = 8 - h;
        for(Module module : category.modules) {
            position += height;
            boolean empty = !module.settings.isEmpty();
            boolean isNull = module.keybind.getKey() == null;
            Color text = module.isOpened() ? Color.opened : Color.text;
            Color color = module.isActive() ? Color.active : Color.module;
            String key = isNull ? "NO" : Keyboard.getKeyName(module.keybind.getKey());
            float tOffset = module.settings.isEmpty() ? 3 : 8, tWidth = font.getStringWidth(key);
            drawRect(x - w, y + position - h, x + w, y + position + h, color.getColor());
            if(empty) font.drawString("°", x - w + 3, y + position - f, Color.text.getColor(), false);
            font.drawString(module.name, x - w + tOffset, y + position - f, text.getColor(), false);
            font.drawString(key, x + w - 3 - tWidth, y + position - f, Color.text.getColor(), false);
            if(module.keybind.equals(keybind)) doKey(color, offset, position);
            if(module.isOpened() && !module.settings.isEmpty()) {
                for(Setting setting : module.settings) {
                    position += height;
                    int sColor = Color.setting.getColor();
                    drawRect(x - w, y + position - h, x + w, y + position + h, sColor);
                    if(setting instanceof Toggle) {
                        Toggle s = (Toggle) setting;
                        String v = s.isActive() ? "ON" : "OFF";
                        int vWidth = font.getStringWidth(v), c = Color.text.getColor();
                        font.drawString(v, x + w - 5 - vWidth, y + position - f, c, false);
                        font.drawString(setting.name, x - w + 5, y + position - f, Color.text.getColor(), false);
                    } else if(setting instanceof Value) {
                        Value s = (Value) setting;
                        String v = Float.toString(s.getValue());
                        int vWidth = font.getStringWidth(v), c = Color.text.getColor();
                        float size = ((s.getValue() - s.min) / (s.max - s.min)) * width;
                        drawRect(x - w, y + position - h, x - w + size, y + position + h, Color.slider.getColor());
                        font.drawString(v, x + w - 5 - vWidth, y + position - f, c, false);
                        font.drawString(setting.name, x - w + 5, y + position - f, Color.text.getColor(), false);
                    } else if(setting instanceof Option) {
                        Option s = (Option) setting;
                        String v = s.values[s.getIndex()];
                        int vWidth = font.getStringWidth(v), c = Color.text.getColor();
                        font.drawString(v, x + w - 5 - vWidth, y + position - f, c, false);
                        font.drawString(setting.name, x - w + 5, y + position - f, Color.text.getColor(), false);
                    }
                }
            }
        }
    }

    private void doKey(Color color, float offset, float position) {
        float x = (float) l / 2 + offset;
        float y = (float) m / 12;
        float w = width / 2;
        float h = height / 2;
        float f = (float) font.getFontHeight() / 2;
        color = color.addColor(Color.keyBind);
        Vector2 p1 = new Vector2(x + w - 23, y + position + h);
        Vector2 p2 = new Vector2(x + w, y + position + h);
        Vector2 p3 = new Vector2(x + w, y + position - h);
        Vector2 p4 = new Vector2(x + w - 20, y + position - h);
        drawVector(p1, p2, p3, p4, color.getColor());
        font.drawString("ANY", x + w - 20, y + position - f, Color.text.getColor(), false);
    }

    @Override
    public void a(int mouseX, int mouseY, int mouseButton) {
        float y = (float) m / 12;
        float w = width / 2;
        float h = height / 2;
        super.a(mouseX, mouseY, mouseButton);
        float offset = (float) (Category.values().length - 1) / -2;
        for(Category category : Category.values()) {
            float x = (float) l / 2 + (width + 3) * offset;
            if(mouseX > x - w && mouseX < x + w) {
                float position = 8 + h;
                if(mouseY > y - 8 && mouseY < y + 8) {
                    if(mouseButton == 1) category.setOpened(category.isClosed());
                } else for(Module module : category.modules) {
                    if(category.isClosed()) break;
                    if(mouseY > y + position - h && mouseY < y + position + h) {
                        boolean e = !module.settings.isEmpty();
                        if(mouseButton == 0) module.setActive(!module.isActive());
                        if(mouseButton == 1 && e) module.setOpened(!module.isOpened());
                        if(mouseButton == 2 && module.keybind.equals(keybind)) keybind = null;
                        else if(mouseButton == 2) keybind = module.keybind;
                    } position += height;
                    if(!module.settings.isEmpty() && module.isOpened()) {
                        for(Setting setting : module.settings) {
                            if(mouseY > y + position - h && mouseY < y + position + h) {
                                float pos = (mouseX - (x - w)) / width * 100;
                                onSetting(setting, mouseButton, pos);
                            } position += height;
                        }
                    }
                }
            } offset += 1;
        }
    }

    @Override
    protected void a(int mouseX, int mouseY, int mouseButton, long lastClick) {
        float y = (float) m / 12;
        float w = width / 2;
        float h = height / 2;
        super.a(mouseX, mouseY, mouseButton);
        float offset = (float) (Category.values().length - 1) / -2;
        for(Category category : Category.values()) {
            float x = (float) l / 2 + (width + 3) * offset;
            if(mouseX > x - w && mouseX < x + w) {
                float position = 8 + h;
                for(Module module : category.modules) {
                    if(category.isClosed()) break;
                    position += height;
                    if(!module.settings.isEmpty() && module.isOpened()) {
                        for(Setting setting : module.settings) {
                            if(mouseY > y + position - h && mouseY < y + position + h) {
                                if(setting instanceof Value) {
                                    float pos = (mouseX - (x - w)) / width * 100;
                                    onSetting(setting, mouseButton, pos);
                                }
                            } position += height;
                        }
                    }
                }
            } offset += 1;
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
            s.setValue((float) v);
        } else if(setting instanceof Option) {
            Option s = (Option) setting;
            if(button == 0) s.setIndex(s.getIndex() + 1);
            if(button == 1) s.setIndex(s.getIndex() - 1);
        }
    }

    @Override
    public void l() {
        super.l();
        char c0 = Keyboard.getEventCharacter();
        if(Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            if(keybind == null) return;
            if(Keyboard.getKeyName(Keyboard.getEventKey()).length() == 1) {
                keybind.setKey(Keyboard.getEventKey());
                keybind = null;
            } else if(Keyboard.getEventKey() == 211) {
                keybind.setKey(null);
                keybind = null;
            }
        }
    }
}