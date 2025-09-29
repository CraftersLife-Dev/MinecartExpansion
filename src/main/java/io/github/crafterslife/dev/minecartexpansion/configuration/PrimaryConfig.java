/*
 * MinecartBoost
 *
 * Copyright (c) 2025. すだち
 *                     Contributors []
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
package io.github.crafterslife.dev.minecartexpansion.configuration;

import com.ezylang.evalex.Expression;
import io.github.crafterslife.dev.minecartexpansion.configuration.annotations.ConfigName;
import io.github.crafterslife.dev.minecartexpansion.utilities.VectorExpression;
import java.util.Map;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * プラグインのメイン設定。
 */
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@NullMarked
@ConfigSerializable
@ConfigName("config.yml")
@Header("""
        プラグインのメイン設定です。
        """)
public final class PrimaryConfig {

    private int railContinuousPlacementDistance = 5;
    private double minecartMaxSpeed = 10.0;
    private Map<Material, VectorExpression> minecartVelocityModifiers = Map.of(
            Material.GOLD_BLOCK, new VectorExpression(
                    new Expression("x * 1.25"),
                    new Expression("y * 1.25"),
                    new Expression("z * 1.25")
            ),
            Material.IRON_BLOCK, new VectorExpression(
                    new Expression("x * 0.95"),
                    new Expression("y * 0.95"),
                    new Expression("z * 0.95")
            )
    );

    /**
     * 連続レール配置機能における最大延長距離を取得します。
     *
     * @return 最大配置レール数
     */
    public int railContinuousPlacementDistance() {
        return railContinuousPlacementDistance;
    }

    /**
     * トロッコに適用される最大速度を取得します。
     *
     * @return 最大速度
     */
    public double minecartMaxSpeed() {
        return minecartMaxSpeed;
    }

    /**
     * レール下に存在する特定のブロックタイプと、それに対応するトロッコ速度の変更式マップを取得します。
     *
     * @return ブロックと速度変更式のマップ
     */
    public Map<Material, VectorExpression> minecartVelocityModifiers() {
        return minecartVelocityModifiers;
    }
}
