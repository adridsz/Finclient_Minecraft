package com.finclient.finclientmc;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChestESPRenderer {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean enabled = true;  // You can add a toggle if desired

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.world == null || mc.player == null)
            return;

        // Get player's view position
        double renderPosX = mc.getRenderManager().info.getProjectedView().x;
        double renderPosY = mc.getRenderManager().info.getProjectedView().y;
        double renderPosZ = mc.getRenderManager().info.getProjectedView().z;

        // Save current render states
        GlStateManager.pushMatrix();
        
        // Disable depth testing to see through walls
        GlStateManager.disableDepthTest();
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        
        // Enable blend for transparency
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glLineWidth(2.0F);

        for (TileEntity te : mc.world.loadedTileEntityList) {
            if (te instanceof ChestTileEntity) {
                BlockPos pos = te.getPos();
                AxisAlignedBB bb = new AxisAlignedBB(pos).offset(-renderPosX, -renderPosY, -renderPosZ);
                
                // Draw green outline
                GlStateManager.color4f(0.0F, 1.0F, 0.0F, 1.0F);
                drawBoundingBox(bb);
                
                // Draw semi-transparent fill
                GlStateManager.color4f(0.0F, 1.0F, 0.0F, 0.2F);
                drawFilledBox(bb);
            }
        }

        // Restore render states
        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void drawBoundingBox(AxisAlignedBB bb) {
        GL11.glBegin(GL11.GL_LINES);
        
        // Bottom face
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

        // Top face
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

        // Connecting vertices
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        GL11.glEnd();
    }

    private static void drawFilledBox(AxisAlignedBB bb) {
        GL11.glBegin(GL11.GL_QUADS);
        
        // Bottom face
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

        // Top face
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        // Front face
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        // Back face
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

        // Left face
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

        // Right face
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

        GL11.glEnd();
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(ChestESPRenderer.class);
    }
}