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
