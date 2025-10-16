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
package io.github.crafterslife.dev.minecartexpansion.event;

import io.github.crafterslife.dev.minecartexpansion.module.rail.ContinuousRailPlacementService;
import io.github.crafterslife.dev.minecartexpansion.module.rail.RailValidator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RailEventHandler implements Listener {

    private final ContinuousRailPlacementService placementService;

    public RailEventHandler(final ContinuousRailPlacementService placementService) {
        this.placementService = placementService;
    }

    @EventHandler
    public void onRailClick(final PlayerInteractEvent event) {
        if (!isValidRailPlacementEvent(event)) {
            return;
        }

        final Player player = event.getPlayer();
        final Block clickedBlock = event.getClickedBlock();

        //noinspection DataFlowIssue
        this.placementService.placeContinuousRailAndConsumeRailItem(player, clickedBlock);
    }

    private boolean isValidRailPlacementEvent(final PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getPlayer().isSneaking() &&
                event.getClickedBlock() != null &&
                RailValidator.isRailBlock(event.getClickedBlock()) &&
                RailValidator.isRailItem(event.getPlayer().getInventory().getItemInMainHand());
    }
}
