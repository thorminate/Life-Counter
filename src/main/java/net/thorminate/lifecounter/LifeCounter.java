package net.thorminate.lifecounter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.thorminate.lifecounter.server.LifeCounterIndex;
import net.thorminate.lifecounter.server.command.LifeCounterCommand;
import net.thorminate.lifecounter.server.network.LivesDataPayload;

public class LifeCounter implements ModInitializer {
	public static final String MOD_ID = "life-counter";

	/*
	 Skip LOGGER  as it is never used right now.
	 public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	*/

    @Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((
				dispatcher,
				registryAccess,
				environment
		) -> LifeCounterCommand.register(dispatcher));

		ServerPlayConnectionEvents.JOIN.register((networkHandler, sender, server) -> LifeCounterIndex.syncDataWithPlayers(server));

		PayloadTypeRegistry.playS2C().register(LivesDataPayload.ID, LivesDataPayload.CODEC);
	}
}