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
