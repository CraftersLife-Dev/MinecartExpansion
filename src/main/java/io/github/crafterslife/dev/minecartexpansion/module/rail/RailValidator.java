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
