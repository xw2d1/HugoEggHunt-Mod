package w2d1.hugoegghunt;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ModInitializer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import w2d1.hugoegghunt.context.PlayerContext;

import java.awt.*;

public class Hugoegghunt implements ModInitializer {

    public static Hugoegghunt INSTANCE;
	public static final String MOD_ID = "hugoegghunt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        INSTANCE = this;
		LOGGER.info("HugoSMP EggHunt Mod aktiviert.");
	}

    /**
     * Called in LevelRendererMixin#onRender
     * @param matrixStack
     * @param tickProgress
     */
    public void hookRenderEvent(PoseStack matrixStack, float tickProgress) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (!(entity instanceof ItemFrame itemFrame)) continue;

            if (!minecraft.level.getBlockState(entity.blockPosition()).is(Blocks.BARRIER)) continue;

            ItemStack stack = itemFrame.getItem();
            if (stack.isEmpty()) continue;

            Identifier modelId = stack.get(DataComponents.ITEM_MODEL);

            if (modelId != null && modelId.toString().contains("easter_eggs")) {
                Color boxColor = new Color(253, 2, 2, 166);
                Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().position();
                Vec3 lookVec = minecraft.player.getViewVector(tickProgress);

                PlayerContext
                        .drawSolidBox(matrixStack, entity.getBoundingBox().expandTowards(0,1,0), boxColor.getRGB(), false);
                PlayerContext.drawLine(matrixStack,
                        cameraPos.add(lookVec.scale(1.5)), entity.getPosition(tickProgress),
                        new Color(253, 2, 2, 255).getRGB(), false, 1);
            }
        }
    }
}