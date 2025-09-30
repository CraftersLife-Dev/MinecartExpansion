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
package io.github.crafterslife.dev.minecartexpansion.translation;

import io.github.namiuni.doburoku.annotation.Locales;
import io.github.namiuni.doburoku.annotation.annotations.Key;
import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
import io.github.namiuni.doburoku.annotation.annotations.Value;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーなどに送信するメッセージを管理するためのサービスインターフェース。
 */
@NullMarked
@ApiStatus.NonExtendable
@ResourceBundle(baseName = "translations/message")
public interface MessageService {

    /**
     * レール延伸機能の使用時に、設置スペースが足りないときに送信するメッセージ。
     *
     * @return メッセージインスタンス
     */
    @Key("minecartexpansion.railplace.nospace")
    @Value(locale = Locales.EN_US, content = "<warn>No space available !")
    @Value(locale = Locales.JA_JP, content = "<warn>レールを設置するためのスペースがありません！")
    Actionbar railPlaceNoSpace();

    /**
     * レール延伸機能の使用時に、設置可能な最長距離に達しているときに送信するメッセージ。
     *
     * @return メッセージインスタンス
     */
    @Key("minecartexpansion.railplace.distance_limit")
    @Value(locale = Locales.EN_US, content = "<warn>The maximum distance for rail installation has been reached!")
    @Value(locale = Locales.JA_JP, content = "<warn>レールを設置できる最大距離に達しています！")
    Actionbar railPlaceDistanceLimit();
}
