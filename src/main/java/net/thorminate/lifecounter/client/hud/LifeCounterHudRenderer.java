package net.thorminate.lifecounter.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.thorminate.lifecounter.client.storage.LifeCounterClientStorage;

import java.util.Map;
import java.util.UUID;

public class LifeCounterHudRenderer {
    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null) return;

        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();

        Map<UUID, Integer> playerLives = LifeCounterClientStorage.getPlayerLives();

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;

            UUID uuid = player.getUuid();

            int lives = playerLives.getOrDefault(uuid, 10);
            Vec3d playerPos = player.getPos().add(0, player.getHeight() + 0.5, 0);

            double distance = cameraPos.squaredDistanceTo(playerPos);
            if (distance > 100) continue;

            MatrixStack matrices = context.getMatrices();
            matrices.push();

            matrices.translate(playerPos.x - cameraPos.x, playerPos.y - cameraPos.y, playerPos.z - cameraPos.z);
            matrices.multiply(camera.getRotation());
            matrices.scale(-0.025f, -0.025f, -0.025f);

            String livesText = "❤ " + lives;
            int textWidth = client.textRenderer.getWidth(livesText) / 2;

            context.drawTextWithShadow(client.textRenderer, Text.literal(livesText), -textWidth, 0, 0x00FF00);

            matrices.pop();
        }
    }
}
