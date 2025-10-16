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
package io.github.crafterslife.dev.minecartexpansion.configuration.serializers;

import java.lang.reflect.Type;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

/**
 * Configurateライブラリのためのカスタム {@link TypeSerializer} です。
 * <p>
 * 設定ファイルにおいて、Minecraftのキー形式（例: {@code minecraft:stone}）の文字列を使用して、
 * {@link Material} オブジェクトをシリアライズおよびデシリアライズします。
 */
@NullMarked
public final class MaterialSerializer implements TypeSerializer<Material> {

    /**
     * 設定ノードから {@link Key} を取得し、それを使用して {@link Registry#MATERIAL} から対応する
     * {@link Material} オブジェクトを取得します。
     *
     * @param type デシリアライズ対象の型情報
     * @param node 設定ファイル上のノード
     * @return デシリアライズされた {@link Material}
     * @throws SerializationException ノードからキーが取得できない、または対応するマテリアルが見つからない場合
     */
    @Override
    public Material deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final var nodeKey = Objects.requireNonNull(node.get(Key.class));
        return Objects.requireNonNull(Registry.MATERIAL.get(nodeKey));
    }

    /**
     * {@link Material} オブジェクトのキーを設定ノードにシリアライズします。
     *
     * @param type シリアライズ対象の型情報
     * @param obj シリアライズする {@link Material} オブジェクト（nullの可能性あり）
     * @param node 設定ファイル上のノード
     * @throws SerializationException シリアライズ中にエラーが発生した場合
     */
    @Override
    public void serialize(final Type type, @Nullable final Material obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            node.set(obj.key());
        }
    }
}
