/*
 * MinecartExpansion
 *
 * Copyright (c) 2025. すだち
 *                     Contributors [Namiu (うにたろう)]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
