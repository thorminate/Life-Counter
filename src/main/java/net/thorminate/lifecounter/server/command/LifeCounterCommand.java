package net.thorminate.lifecounter.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.command.argument.EntityArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.text.Text;
import net.thorminate.lifecounter.server.LifeCounterIndex;
import org.jetbrains.annotations.Nullable;

public class LifeCounterCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lives")
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0, 10))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes((context) ->
                            set(
                                context.getSource(),
                                IntegerArgumentType.getInteger(context, "amount"),
                                EntityArgumentType.getPlayer(context, "player")
                            )
                        )
                    )
                )
            )
            .then(CommandManager.literal("get")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .executes((context) ->
                        get(
                            context.getSource(),
                            EntityArgumentType.getPlayer(context, "player")
                        )
                    )
                )
                .executes((commandContext -> get(commandContext.getSource(), commandContext.getSource().getPlayer())))
            )
            .then(CommandManager.literal("add")
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0, 10))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes((context) ->
                            add(
                                context.getSource(),
                                IntegerArgumentType.getInteger(context, "amount"),
                                EntityArgumentType.getPlayer(context, "player")
                            )
                        )
                    )
                )
            )
            .then(CommandManager.literal("remove")
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0, 10))
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes((context) ->
                            remove(
                                context.getSource(),
                                IntegerArgumentType.getInteger(context, "amount"),
                                EntityArgumentType.getPlayer(context, "player")
                            )
                        )
                    )
                )
            )
        );
    }

    private static int set(ServerCommandSource source, int amount, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            source.sendError(Text.literal("Player is invalid."));
            return 0;
        }

        if (amount > 10) {
            source.sendError(Text.literal("The amount may not be higher than 10."));
            return 0;
        } else if (amount <= 0) {
            source.sendError(Text.literal("The amount may not be lower than 0."));
        }

        LifeCounterIndex.setPlayerLives(source.getServer(), player.getUuid(), amount);

        source.sendFeedback(() -> Text.literal(player.getName().getString() + "'s lives have been set to " + amount), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int get(ServerCommandSource source, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            source.sendError(Text.literal("Player is invalid."));
            return 0;
        }

        int playerLives = LifeCounterIndex.getPlayerLives(source.getServer(), player.getUuid());

        source.sendFeedback(() -> Text.literal(player.getName().getString() + " has " + playerLives + " lives"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int add(ServerCommandSource source, int amount, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            source.sendError(Text.literal("Player is invalid."));
            return 0;
        }

        int playerLives = LifeCounterIndex.getPlayerLives(source.getServer(), player.getUuid());

        int finalAmount = playerLives + amount;
        if (finalAmount > 10) {
            source.sendError(Text.literal("The result may not be above 10. (The result is " + finalAmount + ")"));
            return 0;
        }

        LifeCounterIndex.setPlayerLives(source.getServer(), player.getUuid(), finalAmount);

        source.sendFeedback(() -> Text.literal("Successfully added " + amount + " lives to " + player.getName().getString() + "'s life count. Their life count is now " + finalAmount + "."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int remove(ServerCommandSource source, int amount, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            source.sendError(Text.literal("Player is invalid."));
            return 0;
        }

        int playerLives = LifeCounterIndex.getPlayerLives(source.getServer(), player.getUuid());

        int finalAmount = playerLives - amount;
        if (finalAmount <= 0) {
            source.sendError(Text.literal("The result may not be or below 0. (The result is " + finalAmount + ")"));
            return 0;
        }

        LifeCounterIndex.setPlayerLives(source.getServer(), player.getUuid(), finalAmount);

        source.sendFeedback(() -> Text.literal("Successfully removed " + amount + " lives to " + player.getName().getString() + "'s life count. Their life count is now " + finalAmount + "."), false);
        return Command.SINGLE_SUCCESS;
    }
}
