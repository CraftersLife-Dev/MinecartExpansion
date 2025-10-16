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

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Bukkitの {@link Vector} 操作に関するユーティリティクラスです。
 */
@NullMarked
public final class VectorUtil {

    private VectorUtil() {
    }

    /**
     * {@code Vector} を東西南北の {@link BlockFace} へと変換します。
     * <p>
     * このメソッドは、ベクトルのX成分とZ成分の絶対値を比較し、より大きい成分の方向をそのベクトルの主要な方角として判断します。
     * </p>
     *
     * @param vector 変換対象のベクトル
     * @return 最も近い方角を表す {@code BlockFace} (NORTH, EAST, SOUTH, WEST)
     */
    public static BlockFace cardinalDirection(final Vector vector) {
        final double x = vector.getX();
        final double z = vector.getZ();

        if (Math.abs(x) > Math.abs(z)) {
            // Xが正なら東、負なら西
            return x > 0 ? BlockFace.EAST : BlockFace.WEST;
        } else {
            // Z成分の絶対値がX成分以上の場合、ベクトルは南北方向により強く向いている
            // Zが正なら南（Minecraftの座標系）、負なら北
            return z > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
        }
    }
}
