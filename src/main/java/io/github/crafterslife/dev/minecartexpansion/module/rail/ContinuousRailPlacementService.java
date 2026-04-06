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

import io.github.crafterslife.dev.minecartexpansion.translation.MessageService;
import io.github.crafterslife.dev.minecartexpansion.utilities.PlayerInventoryUtil;
import io.github.crafterslife.dev.minecartexpansion.utilities.VectorUtil;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
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
     * レールを延伸し、プレイヤーがメインハンドに持っているレールアイテムを消費します。
     *
     * <p>このメソッドは、ベースとなるレールからプレイヤーが見ている方向の東西南北にレールを伸ばそうと試みます。
     * レールの延伸に成功した場合、プレイヤーのメインハンドのアイテムを消費します。</p>
     *
     * @param player レールの延伸を試行するプレイヤー
     * @param startRail スタート位置となるレールブロック
     */
    public void placeContinuousRailAndConsumeRailItem(final Player player, final Block startRail) {

        // メインハンドのレールアイテムからレールのブロックデータを生成する。
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if (!(mainHandItem.getType().createBlockData() instanceof Rail rail)) {
            throw new IllegalStateException("The main hand item must be rail-type: " + mainHandItem.getType().key());
        }

        // プレイヤーの座標 (ピッチ) からBlockFaceの東西南北を計算する。
        final Vector direction = player.getLocation().getDirection();
        final BlockFace cardinalDirection = VectorUtil.cardinalDirection(direction);

        // 次に繋げるレールの場所を計算する。
        final RailPlacementResult result = this.pathCalculator.findNextPlacementLocation(startRail, cardinalDirection, rail);

        switch (result) {
            case RailPlacementResult.Success success -> this.replaceRail(player, success.foundPath(), rail);
            case RailPlacementResult.DistanceLimit ignored -> player.sendActionBar(this.messageService.railPlaceDistanceLimit());
            case RailPlacementResult.NoSpace ignored -> player.sendActionBar(this.messageService.railPlaceNoSpace());
        }
    }

    /**
     * 指定されたブロックをレールに置換し、プレイヤーがメインハンドに持っているレールアイテムを消費します。
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
