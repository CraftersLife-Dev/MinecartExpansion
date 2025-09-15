package io.github.crafterslife.dev.minecartboost.minecraft.paper.listeners;

import io.github.crafterslife.dev.minecartboost.configuration.configurations.PrimaryConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class RailSpeedListener implements Listener {
    private final PrimaryConfig primaryConfig;

    public RailSpeedListener(PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    @EventHandler
    public void onMinecartMove(VehicleMoveEvent event) {
        var vehicle = event.getVehicle();

        if(!(vehicle instanceof Minecart minecart)) return;

        Block railBlock = minecart.getLocation().getBlock();
        if (railBlock.getType().toString().contains("RAIL")) {
            Block underBlock = railBlock.getRelative(0,-1,0);

            Vector velocity = minecart.getVelocity();

            var multiplyBlocks = this.primaryConfig.velocityModifyBlocks();
            var maxSpeed = this.primaryConfig.maxSpeed();

            if (multiplyBlocks.containsKey(underBlock.getType())) {
                var multiply = multiplyBlocks.get(underBlock.getType());
                minecart.setMaxSpeed(maxSpeed);
                minecart.setVelocity(velocity.multiply(multiply));
            }
        }
    }
}
