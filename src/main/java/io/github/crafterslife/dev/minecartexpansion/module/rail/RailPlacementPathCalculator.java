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
package io.github.crafterslife.dev.minecartexpansion.module.rail;

import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import java.util.Optional;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.Predicate;

/**
 * 連続的なレール配置のための次の配置場所を計算するサービスです。
 * <p>
 * 既存のレールをたどるか、新規の配置可能なブロックを見つけます。
 */
@NullMarked
public final class RailPlacementPathCalculator {

    private static final List<BlockFace> VERTICAL_OFFSETS = List.of(
            BlockFace.SELF,
            BlockFace.UP,
            BlockFace.DOWN
    );

    private static final List<BlockFace> HORIZONTAL_DIRECTIONS = List.of(
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST
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
     * @param startRailBlock 開始位置となるレールブロック
     * @param direction 探索する方向（東西南北）
     * @param targetRailData 配置を試みるレールブロックデータ
     * @return 探索結果を示す {@link RailPlacementResult}
     * @throws IllegalArgumentException 方向が水平方向でない場合
     */
    public RailPlacementResult findNextPlacementLocation(
            final Block startRailBlock,
            final BlockFace direction,
            final Rail targetRailData
    ) {
        if (!RailValidator.isRailBlock(startRailBlock)) {
            throw new IllegalArgumentException("Block must be rail block: " + startRailBlock.getType().key());
        }

        if (!HORIZONTAL_DIRECTIONS.contains(direction)) {
            throw new IllegalArgumentException("Direction must be horizontal: " + direction);
        }

        Block currentBlock = startRailBlock;
        final int maxDistance = this.primaryConfig.railContinuousPlacementDistance();

        for (int distance = 1; distance <= maxDistance; distance++) {
            currentBlock = currentBlock.getRelative(direction);

            // 既存のレールを検索し、存在した場合は次の座標を検索する
            final Optional<Block> existingRail = this.findBlockWithOffset(currentBlock, RailValidator::isRailBlock);
            if (existingRail.isPresent()) {
                currentBlock = existingRail.get();
                continue;
            }

            // 配置可能な座標を探す
            return this.findBlockWithOffset(currentBlock, block -> this.canPlaceRailData(block, targetRailData))
                    .<RailPlacementResult>map(RailPlacementResult.Success::new)
                    .orElseGet(RailPlacementResult.NoSpace::new);
        }

        return new RailPlacementResult.DistanceLimit();
    }

    /**
     * ベースブロックとその上下で、指定された条件を満たすブロックを検索します。
     *
     * @param baseBlock 検索の基準となるブロック
     * @param predicate ブロックが条件を満たすかを判定する述語
     * @return 条件を満たすブロック（存在する場合）
     */
    private Optional<Block> findBlockWithOffset(
            final Block baseBlock,
            final Predicate<Block> predicate
    ) {
        return VERTICAL_OFFSETS.stream()
                .map(baseBlock::getRelative)
                .filter(predicate)
                .findFirst();
    }

    /**
     * 指定されたブロックに、指定されたレールブロックデータが配置可能であり、かつそのブロックが置き換え可能であるかを判定します。
     *
     * @param block 判定対象のブロック
     * @param railData 配置を試みるレールブロックデータ
     * @return 配置可能かつ置き換え可能であれば {@code true}
     */
    private boolean canPlaceRailData(final Block block, final Rail railData) {
        return block.canPlace(railData) && block.isReplaceable();
    }
}
