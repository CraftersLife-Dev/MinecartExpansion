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

import com.ezylang.evalex.Expression;
import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

/**
 * Configurateライブラリのためのカスタム {@link TypeSerializer} です。
 * <p>
 * 設定ファイルにおいて、文字列として保存された {@link Expression Expression} オブジェクトをシリアライズおよびデシリアライズします。
 */
@NullMarked
public final class ExpressionSerializer implements TypeSerializer<Expression> {

    /**
     * 設定ノードから文字列を取得し、新しい {@link Expression} オブジェクトを作成します。
     *
     * @param type デシリアライズ対象の型情報
     * @param node 設定ファイル上のノード
     * @return デシリアライズされた {@link Expression}
     */
    @Override
    public Expression deserialize(final Type type, final ConfigurationNode node) {
        final var nodeString = node.getString();
        return new Expression(nodeString);
    }

    /**
     * {@link Expression} オブジェクトをその文字列表現として設定ノードにシリアライズします。
     *
     * @param type シリアライズ対象の型情報
     * @param obj シリアライズする {@link Expression} オブジェクト（nullの可能性あり）
     * @param node 設定ファイル上のノード
     * @throws SerializationException シリアライズ中にエラーが発生した場合
     */
    @Override
    public void serialize(final Type type, @Nullable final Expression obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            node.set(obj.getExpressionString());
        }
    }
}
