package io.github.crafterslife.dev.minecartexpansion.utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーのインベントリ操作に関するユーティリティクラスです。
 */
@NullMarked
public final class PlayerInventoryUtil {

    private PlayerInventoryUtil() {
    }

    /**
     * プレイヤーのメインハンドにあるアイテムを1つ消費します。
     *
     * @param player アイテムを消費する {@link Player}
     */
    public static void consumeMainHandItem(final Player player) {
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        final int newAmount = Math.max(0, mainHandItem.getAmount() - 1);
        mainHandItem.setAmount(newAmount);
    }
}
