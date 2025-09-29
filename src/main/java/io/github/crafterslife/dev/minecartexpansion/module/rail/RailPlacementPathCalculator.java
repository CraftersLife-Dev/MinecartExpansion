package io.github.crafterslife.dev.minecartexpansion.module.rail;

import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import java.util.Optional;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * 連続的なレール配置のための次の配置場所を計算するサービスです。
 * <p>
 * 既存のレールをたどるか、新規の配置可能なブロックを見つけます。
 */
@NullMarked
public final class RailPlacementPathCalculator {

    private static final List<BlockFace> SEARCH_OFFSETS = List.of(
            BlockFace.SELF,
            BlockFace.UP,
            BlockFace.DOWN
    );

    private final PrimaryConfig primaryConfig;

    /**
     * 新しい {@code RailPlacementPathCalculator} のインスタンスを生成します。
     *
     * @param primaryConfig 設定値（最大配置距離など）を提供する {@link PrimaryConfig}
     */
    public RailPlacementPathCalculator(final PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    /**
     * 指定された開始ブロックから、指定された方向に次にレールを配置する、または延伸する位置を見つけます。
     *
     * @param startBlock 開始位置となるレールブロック
     * @param direction 探索する方向（東西南北）
     * @param targetBlockData 配置を試みるレールブロックデータ
     * @return 探索結果を示す {@link RailPlacementResult}
     */
    public RailPlacementResult findNextPlacementLocation(
            final Block startBlock,
            final BlockFace direction,
            final BlockData targetBlockData
    ) {
        return this.calculatePath(startBlock, 1, direction, targetBlockData);
    }

    /**
     * レール配置パスを再帰的に計算します。
     *
     * @param currentBlock 現在探索中のベースブロック
     * @param currentDistance 開始レールからの現在の距離
     * @param direction 探索する方向
     * @param targetBlockData 配置を試みるレールブロックデータ
     * @return 探索結果を示す {@link RailPlacementResult}
     */
    private RailPlacementResult calculatePath(
            final Block currentBlock,
            final int currentDistance,
            final BlockFace direction,
            final BlockData targetBlockData
    ) {
        // レールを延伸できる最大距離に達していれば距離制限に達したことを示す結果を返す。
        final int maxDistance = this.primaryConfig.railContinuousPlacementDistance();
        if (maxDistance <= currentDistance) {
            return new RailPlacementResult.DistanceLimit();
        }

        final Block nextBaseBlock = currentBlock.getRelative(direction);

        // 次の位置で既存のレールを検索し、見つかった場合は再帰的に継続
        final Optional<Block> existingRail = this.findExistingRail(nextBaseBlock);
        if (existingRail.isPresent()) {
            final int nextDistance = currentDistance + 1;
            return this.calculatePath(existingRail.get(), nextDistance, direction, targetBlockData);
        }

        // 配置可能な位置を検索
        final Optional<Block> placeable = this.findPlaceableLocation(nextBaseBlock, targetBlockData);
        if (placeable.isPresent()) {
            // 配置可能なブロックが見つかった場合は成功
            return new RailPlacementResult.Success(placeable.get());
        } else {
            // 配置可能な空間が見つからなかった場合は失敗
            return new RailPlacementResult.NoSpace();
        }
    }

    /**
     * ベースブロックとその上下に既存のレールが存在するかを検索します。
     *
     * @param baseBlock 検索の基準となるブロック
     * @return 既存のレールブロック（存在する場合）
     */
    private Optional<Block> findExistingRail(final Block baseBlock) {
        return SEARCH_OFFSETS.stream()
                .map(baseBlock::getRelative)
                .filter(RailValidator::isRailBlock)
                .findFirst();
    }

    /**
     * ベースブロックとその上下に、指定されたレールデータが配置可能な位置を検索します。
     *
     * @param baseBlock 検索の基準となるブロック
     * @param targetBlockData 配置を試みるレールブロックデータ
     * @return レールが配置可能なブロック（存在する場合）
     */
    private Optional<Block> findPlaceableLocation(final Block baseBlock, final BlockData targetBlockData) {
        return SEARCH_OFFSETS.stream()
                .map(baseBlock::getRelative)
                .filter(block -> this.canPlaceBlockData(block, targetBlockData))
                .findFirst();
    }

    /**
     * 指定されたブロックに、指定されたブロックデータが配置可能であり、かつそのブロックが置き換え可能であるかを判定します。
     *
     * @param block 判定対象のブロック
     * @param blockData 配置を試みるブロックデータ
     * @return 配置可能かつ置き換え可能であれば {@code true}
     */
    private boolean canPlaceBlockData(final Block block, final BlockData blockData) {
        return block.canPlace(blockData) && block.isReplaceable();
    }
}
