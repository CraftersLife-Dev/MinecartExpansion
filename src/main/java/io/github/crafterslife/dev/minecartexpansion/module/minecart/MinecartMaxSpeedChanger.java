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
package io.github.crafterslife.dev.minecartexpansion.module.minecart;

import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import org.bukkit.entity.Minecart;
import org.jspecify.annotations.NullMarked;

/**
 * トロッコ（Minecart）の最大速度を設定ファイルの値に変更するサービス。
 */
@NullMarked
public final class MinecartMaxSpeedChanger {

    private final PrimaryConfig primaryConfig;

    /**
     * 新しい {@code MinecartMaxSpeedChanger} のインスタンスを生成します。
     *
     * @param primaryConfig 設定情報を提供する {@link PrimaryConfig}
     */
    public MinecartMaxSpeedChanger(final PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    /**
     * 指定されたトロッコの最大速度を設定ファイルの値に設定します。
     *
     * @param minecart 最大速度を変更する対象の {@link Minecart} エンティティ
     */
    public void changeMaxSpeed(final Minecart minecart) {
        minecart.setMaxSpeed(this.primaryConfig.minecartMaxSpeed());
    }
}
