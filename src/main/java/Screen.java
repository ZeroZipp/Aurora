import com.zerozipp.client.Client;
import com.zerozipp.client.utils.Color;
import com.zerozipp.client.utils.Vector2;
import static com.zerozipp.client.utils.base.Display.*;
import com.zerozipp.client.utils.base.Keybinding;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.types.Type;
import org.lwjgl.input.Keyboard;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Screen extends blk {
    private final float width, height;
    private Keybinding keybinding = null;
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
            doWindow(category, font, (width + 3) * offset);
            offset += 1;
        } popScreen();
    }

    public void doWindow(Category category, Render font, float offset) {
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
            boolean isNull = module.keybinding.getKey() == null;
            String key = isNull ? "NO" : Keyboard.getKeyName(module.keybinding.getKey());
            Color color = module.isActive() ? Color.active : Color.module;
            drawRect(x - w, y + position - h, x + w, y + position + h, color.getColor());
            font.drawString(module.name, x - w + 3, y + position - f, Color.text.getColor(), false);
            font.drawString(key, x + w - 3 - font.getStringWidth(key), y + position - f, Color.text.getColor(), false);
            if(module.keybinding.equals(keybinding)) {
                color = color.addColor(Color.keyBind);
                Vector2 p1 = new Vector2(x + w - 23, y + position + h);
                Vector2 p2 = new Vector2(x + w, y + position + h);
                Vector2 p3 = new Vector2(x + w, y + position - h);
                Vector2 p4 = new Vector2(x + w - 20, y + position - h);
                drawVector(p1, p2, p3, p4, color.getColor());
                font.drawString("ANY", x + w - 20, y + position - f, Color.text.getColor(), false);
            }
        }
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
                        if(mouseButton == 0) module.setActive(!module.isActive());
                        if(mouseButton == 1) {
                            if(module.keybinding.equals(keybinding)) keybinding = null;
                            else keybinding = module.keybinding;
                        }
                    } position += height;
                }
            } offset += 1;
        }
    }

    @Override
    public void l() {
        super.l();
        char c0 = Keyboard.getEventCharacter();
        if(Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            if(keybinding == null) return;
            if(Keyboard.getKeyName(Keyboard.getEventKey()).length() == 1) {
                keybinding.setKey(Keyboard.getEventKey());
                keybinding = null;
            } else if(Keyboard.getEventKey() == 211) {
                keybinding.setKey(null);
                keybinding = null;
            }
        }
    }
}