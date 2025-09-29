package io.github.crafterslife.dev.minecartexpansion.utilities;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Vector操作に関するユーティリティクラスです。
 */
@NullMarked
public final class VectorUtil {

    private VectorUtil() {
    }

    /**
     * Bukkitの {@code Vector} を東西南北の {@link BlockFace} へと変換します。
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
