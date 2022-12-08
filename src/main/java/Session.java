import java.util.UUID;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public class Session extends blk {
    public blk lastScreen;
    private bje nameEdit;

    public Session(blk last) {
        this.lastScreen = last;
    }

    public void e() {
        this.nameEdit.a();
    }

    public void b() {
        Keyboard.enableRepeatEvents(true);
        this.n.clear();
        this.n.add(new bja(0, this.l / 2 - 100, 141, 98, 20, "Exit"));
        this.n.add(new bja(1, this.l / 2 + 2, 141, 98, 20, "Done"));
        this.nameEdit = new bje(2, this.q, this.l / 2 - 100 + 1, 117, 200 - 2, 20);
        this.nameEdit.b(true);
        this.nameEdit.a(getSession().c());
        (this.n.get(1)).l = !this.nameEdit.b().trim().isEmpty();
    }

    protected void a(bja button) {
        if(button.l) {
            if(button.k == 0) j.a(lastScreen);
            if(button.k == 1) {
                saveSession();
                j.a(lastScreen);
            }
        }
    }

    protected void a(char typedChar, int keyCode) {
        this.nameEdit.a(typedChar, keyCode);
        (this.n.get(1)).l = !this.nameEdit.b().trim().isEmpty();
        if (keyCode == 28 || keyCode == 156) {
            this.a(this.n.get(1));
        }
    }

    protected void a(int mouseX, int mouseY, int mouseButton) {
        super.a(mouseX, mouseY, mouseButton);
        this.nameEdit.a(mouseX, mouseY, mouseButton);
    }

    public void saveSession() {
        if(!this.nameEdit.b().trim().isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            setSession(new bii(this.nameEdit.b(), uuid, "0", "mojang"));
        }
    }

    public bii getSession() {
        return j.K();
    }

    public void setSession(bii session) {
        JClass mc = JClass.getClass("minecraft");
        JField field = mc.getDecField("session");
        field.set(j, session);
    }

    public void m() {
        Keyboard.enableRepeatEvents(false);
    }

    public void a(int mouseX, int mouseY, float partialTicks) {
        this.c();
        a(0, 73, this.l, 173, 0x88000000);
        this.a(this.q, "Session edit", this.l / 2, 85, 16777215);
        this.nameEdit.g();
        super.a(mouseX, mouseY, partialTicks);
    }
}
