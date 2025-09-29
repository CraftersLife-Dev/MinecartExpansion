package io.github.crafterslife.dev.minecartexpansion.module.minecart;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import io.github.crafterslife.dev.minecartexpansion.utilities.VectorExpression;
import java.util.Map;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * トロッコ（Minecart）の速度を、レール下のブロックに基づいて変更する。
 * <p>
 * 設定ファイルで定義されたブロックの上にトロッコがある場合、設定された式に従って速度ベクトルを変更します。
 */
@NullMarked
public final class MinecartVelocityModifier {

    private final ComponentLogger logger;
    private final PrimaryConfig primaryConfig;

    /**
     * 新しい {@code MinecartVelocityModifier} のインスタンスを生成します。
     *
     * @param logger ログ出力に使用する {@link ComponentLogger}
     * @param primaryConfig 設定情報を提供する {@link PrimaryConfig}
     */
    public MinecartVelocityModifier(final ComponentLogger logger, final PrimaryConfig primaryConfig) {
        this.logger = logger;
        this.primaryConfig = primaryConfig;
    }

    /**
     * トロッコの速度を、現在走行しているレールブロックの下にあるブロックに基づいて変更します。
     *
     * <p>レール下のブロックが設定で定義された速度変更ブロックである場合、トロッコの速度が更新されます。</p>
     *
     * @param minecart 速度を変更する対象の {@link Minecart} エンティティ
     * @param runningRailBlock トロッコが現在走行しているレールブロック
     */
    public void modifyVelocity(final Minecart minecart, final Block runningRailBlock) {
        final Map<Material, VectorExpression> velocityModifyBlocks = this.primaryConfig.minecartVelocityModifiers();

        // レールが敷設してあるブロックが設定ファイルにて速度変更ブロックとして定義されている場合、トロッコの速度を変更する。
        final Block railBaseBlock = runningRailBlock.getRelative(BlockFace.DOWN);
        final Material railBaseType = railBaseBlock.getType();
        if (velocityModifyBlocks.containsKey(railBaseType)) {
            final VectorExpression velocityExpression = velocityModifyBlocks.get(railBaseType);
            final Vector currentVelocity = minecart.getVelocity();

            try {
                final Vector newVelocity = velocityExpression.evaluate(currentVelocity);
                minecart.setVelocity(newVelocity);
            } catch (final EvaluationException | ParseException exception) {
                this.logger.error("トロッコの速度変更に失敗しました。", exception);
            }
        }
    }
}
