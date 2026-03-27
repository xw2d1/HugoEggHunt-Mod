package w2d1.hugoegghunt.context;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PlayerContext {

    private static final MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

    public static void drawSolidBox(PoseStack matrices, AABB box, int color, boolean depthTest) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        MultiBufferSource.BufferSource vcp = bufferSource;

        RenderType layer = RenderTypes.lines();
        VertexConsumer buffer = vcp.getBuffer(layer);

        drawSolidBox(matrices, buffer, box.move(camera.position().reverse()), color);
        vcp.endBatch(layer);
    }

    private static void drawSolidBox(PoseStack matrices, VertexConsumer buffer, AABB box, int color) {
        PoseStack.Pose entry = matrices.last();
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        addBoxVertex(buffer, entry, x1, y1, z1, color);
        addBoxVertex(buffer, entry, x2, y1, z1, color);

        addBoxVertex(buffer, entry, x2, y1, z1, color);
        addBoxVertex(buffer, entry, x2, y1, z2, color);

        addBoxVertex(buffer, entry, x2, y1, z2, color);
        addBoxVertex(buffer, entry, x1, y1, z2, color);

        addBoxVertex(buffer, entry, x1, y1, z2, color);
        addBoxVertex(buffer, entry, x1, y1, z1, color);

        addBoxVertex(buffer, entry, x1, y2, z1, color);
        addBoxVertex(buffer, entry, x2, y2, z1, color);

        addBoxVertex(buffer, entry, x2, y2, z1, color);
        addBoxVertex(buffer, entry, x2, y2, z2, color);

        addBoxVertex(buffer, entry, x2, y2, z2, color);
        addBoxVertex(buffer, entry, x1, y2, z2, color);

        addBoxVertex(buffer, entry, x1, y2, z2, color);
        addBoxVertex(buffer, entry, x1, y2, z1, color);

        addBoxVertex(buffer, entry, x1, y1, z1, color);
        addBoxVertex(buffer, entry, x1, y2, z1, color);

        addBoxVertex(buffer, entry, x2, y1, z1, color);
        addBoxVertex(buffer, entry, x2, y2, z1, color);

        addBoxVertex(buffer, entry, x2, y1, z2, color);
        addBoxVertex(buffer, entry, x2, y2, z2, color);

        addBoxVertex(buffer, entry, x1, y1, z2, color);
        addBoxVertex(buffer, entry, x1, y2, z2, color);
    }

    private static void addBoxVertex(VertexConsumer buffer, PoseStack.Pose entry, float x, float y, float z, int color) {
        buffer.addVertex(entry, x, y, z)
                .setColor(color)
                .setNormal(entry, 1.0f, 1.0f, 1.0f)
                .setLineWidth(1.0f);
    }

    public static void drawLine(PoseStack matrices, Vec3 start, Vec3 end, int color, boolean depthTest, float lineWidth) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cam = camera.position();

        float sx = (float) (start.x - cam.x);
        float sy = (float) (start.y - cam.y);
        float sz = (float) (start.z - cam.z);
        float ex = (float) (end.x - cam.x);
        float ey = (float) (end.y - cam.y);
        float ez = (float) (end.z - cam.z);

        MultiBufferSource.BufferSource vcp = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType layer = RenderTypes.lines();
        VertexConsumer buffer = vcp.getBuffer(layer);

        PoseStack.Pose entry = matrices.last();

        float dx = ex - sx;
        float dy = ey - sy;
        float dz = ez - sz;
        float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        float nx = len > 0 ? dx / len : 1.0f;
        float ny = len > 0 ? dy / len : 0.0f;
        float nz = len > 0 ? dz / len : 0.0f;

        buffer.addVertex(entry, sx, sy, sz).setColor(color).setNormal(entry, nx, ny, nz).setLineWidth(lineWidth);
        buffer.addVertex(entry, ex, ey, ez).setColor(color).setNormal(entry, nx, ny, nz).setLineWidth(lineWidth);

        vcp.endBatch(layer);
    }
}