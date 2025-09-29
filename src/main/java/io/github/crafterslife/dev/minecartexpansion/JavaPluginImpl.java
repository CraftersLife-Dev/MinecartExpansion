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
package io.github.crafterslife.dev.minecartexpansion;

import io.github.crafterslife.dev.minecartexpansion.event.MinecartEventHandler;
import io.github.crafterslife.dev.minecartexpansion.event.RailEventHandler;
import io.github.crafterslife.dev.minecartexpansion.module.rail.ContinuousRailPlacementService;
import io.github.crafterslife.dev.minecartexpansion.module.minecart.MinecartMaxSpeedChanger;
import io.github.crafterslife.dev.minecartexpansion.module.minecart.MinecartVelocityModifier;
import io.github.crafterslife.dev.minecartexpansion.module.rail.RailPlacementPathCalculator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

/**
 * <p>Paperプラグインのメインクラスです。</p>
 *
 * <p>このクラスは、プラグインのメインのビジネスロジックとライフサイクルを管理します。</p>
 */
@NullMarked
public final class JavaPluginImpl extends JavaPlugin {

    private final ResourceContainer resourceContainer;

    /**
     * このクラスの新しいインスタンスを生成します。
     *
     * @param serviceContainer サービスロジックコンテナ
     */
    public JavaPluginImpl(final ResourceContainer serviceContainer) {
        this.resourceContainer = serviceContainer;
    }

    @Override
    public void onEnable() {
        final var maxSpeedChanger = new MinecartMaxSpeedChanger(this.resourceContainer.primaryConfig());
        final var velocityModifier = new MinecartVelocityModifier(this.getComponentLogger(), this.resourceContainer.primaryConfig());
        final var minecartListener = new MinecartEventHandler(velocityModifier, maxSpeedChanger);
        Bukkit.getPluginManager().registerEvents(minecartListener, this);

        final var pathCalculator = new RailPlacementPathCalculator(this.resourceContainer.primaryConfig());
        final var railPlacementManager = new ContinuousRailPlacementService(pathCalculator, this.resourceContainer.messageService());
        final var railListener = new RailEventHandler(railPlacementManager);
        Bukkit.getPluginManager().registerEvents(railListener,this);
    }
}
