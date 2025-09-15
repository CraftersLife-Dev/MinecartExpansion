package io.github.crafterslife.dev.minecartboost.minecraft.paper.listeners;

import io.github.crafterslife.dev.minecartboost.ResourceContainer;
import io.github.crafterslife.dev.minecartboost.configuration.configurations.PrimaryConfig;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RailExtendListener implements Listener {
    private final ResourceContainer resourceContainer = null;

    public RailExtendListener(PrimaryConfig primaryConfig) {
    }

    private static final int MAX_PLACE = 1000;
    private static final String NO_SPACE_MESSAGE = "指定範囲内に設置可能な場所がありません！";

    @EventHandler
    public void onRailClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        Block clicked = event.getClickedBlock();
        if (clicked == null || !isRail(clicked.getType())) return;

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() == Material.AIR) return;
        if (!isRail(hand.getType()) || hand.getAmount() <= 0) return;

        Vector direction = getHorizontalDirection(player);
        Block placeable = findFirstPlaceable(clicked, direction);

        if (placeable == null) {
            player.sendMessage(NO_SPACE_MESSAGE);
            return;
        }

        placeable.setType(Material.RAIL);
        consumeOneIfNeeded(player, hand);
    }

    private static boolean isRail(Material m) {
        return m.toString().contains("RAIL");
    }

    private static Vector getHorizontalDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        int dir = Math.round(yaw / 90f) % 4;
        return switch (dir) {
            case 1 -> new Vector(-1, 0, 0);
            case 2 -> new Vector(0, 0, -1);
            case 3 -> new Vector(1, 0, 0);
            default -> new Vector(0, 0, 1);
        };
    }

    private static Block findFirstPlaceable(Block clicked, Vector direction) {
        Location base = clicked.getLocation().clone();
        for (int i = 1; i <= RailExtendListener.MAX_PLACE; i++) {
            Location targetLoc = base.clone().add(direction.clone().multiply(i));
            Block target = targetLoc.getBlock();
            Block under = target.getRelative(0, -1, 0);

            if (target.getType() == Material.AIR && under.getType().isSolid()) {
                return target;
            }
        }
        return null;
    }

    private static void consumeOneIfNeeded(Player player, ItemStack hand) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        int amount = hand.getAmount() - 1;
        if (amount <= 0) {
            player.getInventory().setItemInMainHand(null);
        } else {
            hand.setAmount(amount);
        }
    }
}
