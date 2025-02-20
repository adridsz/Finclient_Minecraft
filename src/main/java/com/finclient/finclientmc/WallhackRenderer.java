package com.finclient.finclientmc;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WallhackRenderer {

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.world == null || mc.player == null) return;

    ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
    Vec3d viewPos = info.getProjectedView();

        GlStateManager.pushMatrix();
        GlStateManager.disableDepthTest();
        GlStateManager.translated(
                -viewPos.x,
            -viewPos.y,
            -viewPos.z
        );

        mc.world.getPlayers().forEach(player -> {
        if (player != mc.player && !player.isSpectator() && player.isAlive()) {
            renderPlayerInfo(player);
        }
    });

        GlStateManager.enableDepthTest();
        GlStateManager.popMatrix();
}

private static void renderPlayerInfo(PlayerEntity player) {
    EntityRendererManager renderManager = mc.getRenderManager();
    double x = player.posX;
    double y = player.posY + player.getHeight() + 0.5F;
    double z = player.posZ;

    String name = player.getName().getFormattedText();
    float maxHealth = player.getMaxHealth();
    float currentHealth = Math.min(player.getHealth(), maxHealth);
    String health = String.format("\u00A7c\u2764 %.1f/%.1f", currentHealth, maxHealth);

    double distance = mc.player.getDistance(player);
    String distanceText = String.format("Distance: %.1f m", distance);

    float viewerYaw = renderManager.info.getYaw();
    float viewerPitch = renderManager.info.getPitch();

    GlStateManager.pushMatrix();
    GlStateManager.translated(x, y, z);
    GlStateManager.rotatef(-viewerYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(viewerPitch, 1.0F, 0.0F, 0.0F);

    float scale = 0.025F; // Fixed scale factor
    GlStateManager.scalef(-scale, -scale, scale);

    int nameWidth = mc.fontRenderer.getStringWidth(name);
    int healthWidth = mc.fontRenderer.getStringWidth(health);
    int distanceWidth = mc.fontRenderer.getStringWidth(distanceText);

    mc.fontRenderer.drawStringWithShadow(name, -nameWidth / 2f, 0, 0xFFFFFF);
    mc.fontRenderer.drawStringWithShadow(health, -healthWidth / 2f, 10, 0xFFFFFF);
    mc.fontRenderer.drawStringWithShadow(distanceText, -distanceWidth / 2f, 20, 0xFFFFFF);

    GlStateManager.popMatrix();
}

public static void register() {
    MinecraftForge.EVENT_BUS.register(WallhackRenderer.class);
}
}