package net.thorminate.lifecounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.thorminate.lifecounter.client.hud.LifeCounterHudRenderer;
import net.thorminate.lifecounter.client.storage.LifeCounterClientStorage;
import net.thorminate.lifecounter.server.network.LivesDataPayload;

import java.util.Map;
import java.util.UUID;

public class LifeCounterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(LivesDataPayload.ID, LivesDataPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(LivesDataPayload.ID, ((livesDataPayload, context) ->{
            Map<UUID, Integer> playerLives = livesDataPayload.playerLives();
            LifeCounterClientStorage.setPlayerLives(playerLives);
        }));

        HudRenderCallback.EVENT.register((context, tickCounter) -> LifeCounterHudRenderer.render(context));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> LifeCounterClientStorage.setPlayerLives(null));
    }
}
