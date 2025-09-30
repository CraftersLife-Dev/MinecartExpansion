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
import org.jspecify.annotations.NullMarked;

/**
 * レール配置の試行結果を表す Sealed Interface です。
 */
@NullMarked
public sealed interface RailPlacementResult permits
        RailPlacementResult.Success,
        RailPlacementResult.NoSpace,
        RailPlacementResult.DistanceLimit {

    /**
     * レールを配置できる場所が見つかったことを示します。
     *
     * @param foundPath レールを配置すべきブロック
     */
    record Success(Block foundPath) implements RailPlacementResult {
    }

    /**
     * レールを配置するための空間が見つからなかったことを示します。
     */
    record NoSpace() implements RailPlacementResult {
    }

    /**
     * 連続配置の最大距離制限に達したことを示します。
     */
    record DistanceLimit() implements RailPlacementResult {
    }
}
