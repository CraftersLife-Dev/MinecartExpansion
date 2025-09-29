package io.github.crafterslife.dev.minecartexpansion.module.rail;

import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

/**
 * レール配置の試行結果を表す Sealed Interface です。
 */
@NullMarked
public sealed interface RailPlacementResult permits
        RailPlacementResult.Success,
        RailPlacementResult.NoSpace,
        RailPlacementResult.DistanceLimit {

    /**
     * レールを配置できる場所が見つかったことを示します。
     *
     * @param foundPath レールを配置すべきブロック
     */
    record Success(Block foundPath) implements RailPlacementResult {
    }

    /**
     * レールを配置するための空間が見つからなかったことを示します。
     */
    record NoSpace() implements RailPlacementResult {
    }

    /**
     * 連続配置の最大距離制限に達したことを示します。
     */
    record DistanceLimit() implements RailPlacementResult {
    }
}
