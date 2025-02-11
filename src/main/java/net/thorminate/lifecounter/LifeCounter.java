package net.thorminate.lifecounter;

import net.fabricmc.api.ModInitializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import static net.thorminate.lifecounter.server.LifeCounterIndex.init;

public class LifeCounter implements ModInitializer {
	/*
	 Skip LOGGER and MOD_ID as it is never used right now.
	 public static final String MOD_ID = "life-counter";
	 public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	*/

    @Override
	public void onInitialize() {
		init();
	}
}