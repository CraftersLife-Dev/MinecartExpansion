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
package io.github.crafterslife.dev.minecartboost.configuration.configurations;

import io.github.crafterslife.dev.minecartboost.configuration.Header;
import io.github.crafterslife.dev.minecartboost.configuration.annotations.ConfigName;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

/**
 * プラグインのメイン設定。
 */
@NullMarked
@ConfigSerializable
@ConfigName("config.yml")
@Header("""
        プラグインのメイン設定です。
        """)
public record PrimaryConfig(
        int maxSpeed,
        Map<Material,Double> velocityModifyBlocks
) {
    /**
     * メイン設定のデフォルト設定。
     */
    public static final PrimaryConfig DEFAULT = new PrimaryConfig(
            10,
            Map.of(Material.GOLD_BLOCK,1.25,
                    Material.IRON_BLOCK,0.95)
    );
}
