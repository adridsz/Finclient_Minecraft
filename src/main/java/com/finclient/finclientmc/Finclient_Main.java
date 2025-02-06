package com.finclient.finclientmc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Random;

@Mod("finclient")
public class Finclient_Main {

    // Instancia del cliente y generador aleatorio
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Random random = new Random();

    // Distancia máxima de ataque (en bloques)
    private static final double MAX_ATTACK_DISTANCE = 6.8D;

    // Estado del mod y retardo entre ataques (en ticks)
    private static boolean enabled = false;
    private static int ticksUntilNextAttack = getRandomAttackDelay();

    // Tecla para activar/desactivar el mod (R por defecto)
    private static final KeyBinding TOGGLE_KEY = new KeyBinding(
            "key.finclient.toggle",
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.finclient"
    );

    public Finclient_Main() {
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(TOGGLE_KEY);
    }

    // Renderiza un pequeño HUD indicando que el killaura está activado
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!enabled || mc.player == null || mc.gameSettings.hideGUI)
            return;

        String text = "Killaura Activado";
        mc.fontRenderer.drawStringWithShadow(text, 4, 4, 0xFFFFFF);
    }

    // Evento de tick del cliente: se revisa la tecla, se buscan objetivos en el FOV y se ataca
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.player == null || mc.world == null)
            return;

        // Alterna el mod al presionar la tecla de activación
        if (TOGGLE_KEY.isPressed()) {
            enabled = !enabled;
        }
        if (!enabled) return;

        // Control de retardo entre ataques
        if (ticksUntilNextAttack > 0) {
            ticksUntilNextAttack--;
            return;
        }

        // Obtiene el vector de dirección del jugador
        Vec3d lookVec = mc.player.getLookVec();

        // Define el campo de visión (FOV) en grados; por ejemplo, 60° (30° a cada lado)
        float fov = 60.0F;
        double cosLimit = Math.cos(Math.toRadians(fov / 2.0));

        // Obtiene todas las entidades vivas dentro de un AABB centrado en el jugador
        AxisAlignedBB aabb = mc.player.getBoundingBox().grow(MAX_ATTACK_DISTANCE);
        List<Entity> targets = mc.world.getEntitiesWithinAABB(LivingEntity.class, aabb, entity ->
                entity != mc.player &&
                        entity instanceof LivingEntity &&
                        ((LivingEntity) entity).deathTime == 0
        );

        // Recorre las entidades detectadas y ataca si están en el FOV
        for (Entity target : targets) {
            // Posición de los ojos del jugador
            Vec3d playerEyePos = mc.player.getEyePosition(1.0F);
            // Posición objetivo: se toma la posición de la entidad y se le suma la mitad de su altura de ojos
            Vec3d targetPos = target.getPositionVec().add(0, ((LivingEntity) target).getEyeHeight() * 0.5, 0);
            // Vector desde el jugador hasta el objetivo (normalizado)
            Vec3d toTarget = targetPos.subtract(playerEyePos).normalize();

            // Si el producto punto es mayor o igual que cosLimit, el ángulo es menor que (FOV/2)
            if (lookVec.dotProduct(toTarget) >= cosLimit && mc.player.getDistance(target) <= MAX_ATTACK_DISTANCE) {
                if (mc.playerController != null) {
                    mc.playerController.attackEntity(mc.player, target);
                    mc.player.swingArm(Hand.MAIN_HAND);
                    ticksUntilNextAttack = getRandomAttackDelay();
                    // Si se desea atacar solo un enemigo por tick, se puede salir del loop
                    break;
                }
            }
        }
    }

    // Calcula un retardo aleatorio entre ataques basado en el CPS (Clicks Por Segundo)
    private static int getRandomAttackDelay() {
        int minCPS = 8;
        int maxCPS = 15;
        int cps = minCPS + random.nextInt(maxCPS - minCPS + 1);
        return Math.max(1, 20 / cps);
    }
}