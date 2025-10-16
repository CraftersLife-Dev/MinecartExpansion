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

import io.github.crafterslife.dev.minecartexpansion.module.minecart.MinecartMaxSpeedChanger;
import io.github.crafterslife.dev.minecartexpansion.module.minecart.MinecartVelocityModifier;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MinecartEventHandler implements Listener {

    private final MinecartVelocityModifier minecartVelocityModifier;
    private final MinecartMaxSpeedChanger minecartMaxSpeedChanger;

    public MinecartEventHandler(
            final MinecartVelocityModifier minecartVelocityModifier,
            final MinecartMaxSpeedChanger minecartMaxSpeedChanger
    ) {
        this.minecartVelocityModifier = minecartVelocityModifier;
        this.minecartMaxSpeedChanger = minecartMaxSpeedChanger;
    }

    @EventHandler
    private void onVehicleMove(final VehicleMoveEvent event) {
        // 移動した乗り物がトロッコでなおかつレール上に存在する場合、トロッコの速度を変更する。
        if (event.getVehicle() instanceof Minecart minecart) {
            final Block underBlock = minecart.getLocation().getBlock();
            if (underBlock.getBlockData() instanceof Rail) {
                this.minecartVelocityModifier.modifyVelocity(minecart, underBlock);
            }
        }
    }

    @EventHandler
    private void onEntityPlace(final EntityPlaceEvent event) {
        // 設置されたエンティティがトロッコの場合、トロッコの最高速度を変更する。
        if (event.getEntity() instanceof Minecart minecart) {
            this.minecartMaxSpeedChanger.changeMaxSpeed(minecart);
        }
    }
}
