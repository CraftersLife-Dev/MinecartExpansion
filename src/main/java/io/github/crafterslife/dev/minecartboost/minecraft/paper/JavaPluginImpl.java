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
package io.github.crafterslife.dev.minecartboost.minecraft.paper;

import io.github.crafterslife.dev.minecartboost.ResourceContainer;
import io.github.crafterslife.dev.minecartboost.minecraft.paper.listeners.RailExtendListener;
import io.github.crafterslife.dev.minecartboost.minecraft.paper.listeners.RailSpeedListener;
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
        /* templateServiceをリスナーに渡す */
        Bukkit.getPluginManager().registerEvents(new RailSpeedListener(resourceContainer.primaryConfig()), this);
        Bukkit.getPluginManager().registerEvents(new RailExtendListener(resourceContainer.primaryConfig(),resourceContainer),this);
    }
}
