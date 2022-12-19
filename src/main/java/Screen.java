import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.screen.Window;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.ArrayList;
import static com.zerozipp.client.utils.base.Display.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Screen extends blk {
    public static final Color cBackground = new Color(0, 0, 0, 80);
    private final ArrayList<Window> windows;
    private Render font = null;

    public Screen() {
        windows = new ArrayList<>();
        float offset = (float) (Category.values().length - 1) / -2;
        for(Category category : Category.values()) {
            windows.add(new Window(category, (78) * offset, 0));
            offset += 1;
        }
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public void a(int mouseX, int mouseY, float ticks) {
        super.a(mouseX, mouseY, ticks);
        pushScreen();
        String f = "font/medium.ttf";
        if(font == null) font = Client.getFont(f, 16);
        drawRect(0, 0, l, m, cBackground.getColor());
        for(Window win : windows) win.draw(font, l, m, mouseX, mouseY, ticks);
        popScreen();
    }

    @Override
    public void a(int mouseX, int mouseY, int button) {
        for(Window win : windows) win.onMouse(l, m, mouseX, mouseY, button);
        super.a(mouseX, mouseY, button);
    }

    @Override
    protected void a(int mouseX, int mouseY, int button, long last) {
        for(Window win : windows) win.onMouseDrag(l, m, mouseX, mouseY, button);
        super.a(mouseX, mouseY, button, last);
    }

    @Override
    public void k() {
        super.k();
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object w = c.getField("displayWidth").get(mc);
        Object h = c.getField("displayHeight").get(mc);
        final int y = m - Mouse.getY() * m / (int) h - 1;
        final int x = Mouse.getX() * l / (int) w;
        int i = Mouse.getEventDWheel();
        i = Integer.compare(i, 0);
        for(Window win : windows) {
            win.onMouseWheel(i, x, y, l, m);
        }
    }

    @Override
    public void l() {
        super.l();
        for(Window win : windows) {
            win.onKeyboard(Keyboard.getEventKey());
        }
    }
}