package io.github.crafterslife.dev.minecartexpansion.module.rail;

import io.github.crafterslife.dev.minecartexpansion.translation.MessageService;
import io.github.crafterslife.dev.minecartexpansion.utilities.PlayerInventoryUtil;
import io.github.crafterslife.dev.minecartexpansion.utilities.VectorUtil;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * プレイヤーによる連続的なレール配置のロジックを処理するサービス。
 * <p>
 * プレイヤーの向きを基に次の配置場所を計算し、配置結果に応じてプレイヤーにメッセージを送信し、インベントリのアイテムを消費します。
 */
@NullMarked
public final class ContinuousRailPlacementService {

    private final RailPlacementPathCalculator pathCalculator;
    private final MessageService messageService;

    /**
     * 新しい {@code ContinuousRailPlacementService} のインスタンスを生成します。
     *
     * @param pathCalculator レール配置パスの計算に使用される {@link RailPlacementPathCalculator}
     * @param messageService プレイヤーにメッセージを送信するために使用される {@link MessageService}
     */
    public ContinuousRailPlacementService(
            final RailPlacementPathCalculator pathCalculator,
            final MessageService messageService
    ) {
        this.pathCalculator = pathCalculator;
        this.messageService = messageService;
    }

    /**
     * レールを延伸します。
     *
     * <p>このメソッドは、ベースとなるレールからプレイヤーが見ている方向の東西南北にレールを伸ばそうと試みます。</p>
     *
     * @param player レールの延伸を試行するプレイヤー
     * @param startRail スタート位置となるレールブロック
     * @param railItem 消費するレールアイテム
     */
    public void placeContinuousRail(final Player player, final Block startRail, final ItemStack railItem) {

        // プレイヤーの座標 (ピッチ) からBlockFaceの東西南北を計算する。
        final Vector direction = player.getLocation().getDirection();
        final BlockFace cardinalDirection = VectorUtil.cardinalDirection(direction);

        // レールアイテムからブロックデータを生成する。
        final BlockData railBlockData = railItem.getType().createBlockData();

        // 次に繋げるレールの場所を計算する。
        final RailPlacementResult result = this.pathCalculator.findNextPlacementLocation(startRail, cardinalDirection, railBlockData);

        switch (result) {
            case RailPlacementResult.Success success -> this.replaceRail(player, success.foundPath(), railBlockData);
            case RailPlacementResult.DistanceLimit ignored -> this.messageService.railPlaceDistanceLimit().send(player);
            case RailPlacementResult.NoSpace ignored -> this.messageService.railPlaceNoSpace().send(player);
        }
    }

    /**
     * 指定されたブロックをレールに置換し、プレイヤーのインベントリからアイテムを消費します。
     *
     * @param player レールを設置するプレイヤー
     * @param targetBlock 置換対象のブロック
     * @param railBlockData レールのブロックデータ
     */
    private void replaceRail(
            final Player player,
            final Block targetBlock,
            final BlockData railBlockData
    ) {
        targetBlock.setBlockData(railBlockData);
        // プレイヤーがサバイバルモードの場合のみ、アイテムを消費する。
        if (player.getGameMode() == GameMode.SURVIVAL) {
            PlayerInventoryUtil.consumeMainHandItem(player);
        }
    }
}
