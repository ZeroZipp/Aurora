import com.zerozipp.client.utils.Vector2;
import com.zerozipp.client.utils.base.Buffer;
import com.zerozipp.client.utils.base.Tessellator;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import static org.lwjgl.opengl.GL11.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Display extends blk {
    public void drawRect(float left, float top, float right, float bottom, int color) {
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
        buffer.pos(left, bottom, 0.0);
        buffer.pos(right, bottom, 0.0);
        buffer.pos(right, top, 0.0);
        buffer.pos(left, top, 0.0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public void drawVector(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, int color) {
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
        buffer.pos(p1.x, p1.y, 0.0);
        buffer.pos(p2.x, p2.y, 0.0);
        buffer.pos(p3.x, p3.y, 0.0);
        buffer.pos(p4.x, p4.y, 0.0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public void pushScreen() {
        glEnable(GL_BLEND);
        glDepthMask(false);
        glColor4f(1, 1, 1, 1);
    }

    public void popScreen() {
        glDisable(GL_BLEND);
        glDepthMask(true);
        glColor4f(1, 1, 1, 1);
    }
}
