package io.github.crafterslife.dev.minecartexpansion.module.minecart;

import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import org.bukkit.entity.Minecart;
import org.jspecify.annotations.NullMarked;

/**
 * トロッコ（Minecart）の最大速度を設定ファイルの値に変更するサービス。
 */
@NullMarked
public final class MinecartMaxSpeedChanger {

    private final PrimaryConfig primaryConfig;

    /**
     * 新しい {@code MinecartMaxSpeedChanger} のインスタンスを生成します。
     *
     * @param primaryConfig 設定情報を提供する {@link PrimaryConfig}
     */
    public MinecartMaxSpeedChanger(final PrimaryConfig primaryConfig) {
        this.primaryConfig = primaryConfig;
    }

    /**
     * 指定されたトロッコの最大速度を設定ファイルの値に設定します。
     *
     * @param minecart 最大速度を変更する対象の {@link Minecart} エンティティ
     */
    public void changeMaxSpeed(final Minecart minecart) {
        minecart.setMaxSpeed(this.primaryConfig.minecartMaxSpeed());
    }
}
