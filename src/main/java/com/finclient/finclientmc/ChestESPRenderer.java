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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.world == null || mc.player == null)
            return;

        double renderPosX = mc.getRenderManager().info.getProjectedView().x;
        double renderPosY = mc.getRenderManager().info.getProjectedView().y;
        double renderPosZ = mc.getRenderManager().info.getProjectedView().z;

        GlStateManager.pushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(2.0F);

        for (TileEntity te : mc.world.loadedTileEntityList) {
            if (te instanceof ChestTileEntity) {
                BlockPos pos = te.getPos();
                // Use a bounding box from the block's minimum to maximum corner
                AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1, 1, 1));
                GlStateManager.pushMatrix();
                // Translate the bounding box to render space
                GlStateManager.translated(bb.minX - renderPosX, bb.minY - renderPosY, bb.minZ - renderPosZ);
                drawBoundingBox(bb);
                GlStateManager.popMatrix();
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.popMatrix();
    }

    private static void drawBoundingBox(AxisAlignedBB bb) {
        GL11.glBegin(GL11.GL_LINES);
        // Draw in green.
        GlStateManager.color4f(0.0F, 1.0F, 0.0F, 1.0F);

        double xSize = bb.getXSize();
        double ySize = bb.getYSize();
        double zSize = bb.getZSize();

        // Bottom face.
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(xSize, 0, 0);

        GL11.glVertex3d(xSize, 0, 0);
        GL11.glVertex3d(xSize, 0, zSize);

        GL11.glVertex3d(xSize, 0, zSize);
        GL11.glVertex3d(0, 0, zSize);

        GL11.glVertex3d(0, 0, zSize);
        GL11.glVertex3d(0, 0, 0);

        // Top face.
        GL11.glVertex3d(0, ySize, 0);
        GL11.glVertex3d(xSize, ySize, 0);

        GL11.glVertex3d(xSize, ySize, 0);
        GL11.glVertex3d(xSize, ySize, zSize);

        GL11.glVertex3d(xSize, ySize, zSize);
        GL11.glVertex3d(0, ySize, zSize);

        GL11.glVertex3d(0, ySize, zSize);
        GL11.glVertex3d(0, ySize, 0);

        // Vertical edges.
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(0, ySize, 0);

        GL11.glVertex3d(xSize, 0, 0);
        GL11.glVertex3d(xSize, ySize, 0);

        GL11.glVertex3d(xSize, 0, zSize);
        GL11.glVertex3d(xSize, ySize, zSize);

        GL11.glVertex3d(0, 0, zSize);
        GL11.glVertex3d(0, ySize, zSize);

        GL11.glEnd();
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(ChestESPRenderer.class);
    }
}