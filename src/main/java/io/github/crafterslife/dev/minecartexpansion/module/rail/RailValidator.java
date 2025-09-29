package io.github.crafterslife.dev.minecartexpansion.module.rail;

import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * {@link Block} または {@link ItemStack} が レールを表すかどうかを検証するためのユーティリティクラス。
 */
@NullMarked
public final class RailValidator {

    private RailValidator() {
    }

    public static boolean isRailBlock(final Block block) {
        return block.getBlockData() instanceof Rail;
    }

    public static boolean isRailItem(final ItemStack item) {
        return item.getType().createBlockData() instanceof Rail;
    }
}
