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
package io.github.crafterslife.dev.minecartexpansion;

import com.ezylang.evalex.Expression;
import io.github.crafterslife.dev.minecartexpansion.configuration.ConfigurateHelper;
import io.github.crafterslife.dev.minecartexpansion.configuration.Header;
import io.github.crafterslife.dev.minecartexpansion.configuration.PrimaryConfig;
import io.github.crafterslife.dev.minecartexpansion.configuration.UncheckedConfigurateException;
import io.github.crafterslife.dev.minecartexpansion.configuration.annotations.ConfigName;
import io.github.crafterslife.dev.minecartexpansion.configuration.serializers.ExpressionSerializer;
import io.github.crafterslife.dev.minecartexpansion.configuration.serializers.MaterialSerializer;
import io.github.crafterslife.dev.minecartexpansion.integration.MiniPlaceholdersExpansion;
import io.github.crafterslife.dev.minecartexpansion.translation.spi.Actionbar;
import io.github.crafterslife.dev.minecartexpansion.translation.DynamicResourceBundleControl;
import io.github.crafterslife.dev.minecartexpansion.translation.spi.Message;
import io.github.crafterslife.dev.minecartexpansion.translation.MessageService;
import io.github.crafterslife.dev.minecartexpansion.translation.TranslationStoreInitializer;
import io.github.namiuni.doburoku.standard.DoburokuStandard;
import io.github.namiuni.doburoku.standard.argument.MiniMessageArgumentTransformer;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * 設定や翻訳などのリソースをまとめたコンテナ。
 *
 * @param primaryConfig  プラグインの設定
 * @param messageService メッセージサービス
 */
@NullMarked
@SuppressWarnings("UnstableApiUsage")
public record ResourceContainer(
        PrimaryConfig primaryConfig,
        MessageService messageService
) {

    /**
     * このレコードのインスタンスを生成する。
     *
     * @param context リソースの初期化に必要なプラグインコンテキスト
     * @return このレコードのインスタンス
     */
    public static ResourceContainer create(final PluginProviderContext context) {
        final MessageService messageService = ResourceContainer.createMessageService(context);

        final PrimaryConfig primaryConfig = ResourceContainer.createConfig(context, PrimaryConfig.class, new PrimaryConfig());
        final String configName = PrimaryConfig.class.getAnnotation(ConfigName.class).value();
        context.getLogger().info("設定を読み込みました: {}", configName);

        return new ResourceContainer(primaryConfig, messageService);
    }

    private static <T> T createConfig(
            final PluginProviderContext context,
            final Class<T> model,
            final T base
    ) throws UncheckedConfigurateException {
        try {
            final T config = ConfigurateHelper.builder(model)
                    .defaultConfiguration(base)
                    .configurationLoader(YamlConfigurationLoader.builder()
                            .nodeStyle(NodeStyle.BLOCK)
                            .defaultOptions(options -> options
                                    .shouldCopyDefaults(true)
                                    .header(model.getAnnotation(Header.class).value())
                                    .serializers(builder -> builder
                                            .registerAll(ConfigurateComponentSerializer.configurate().serializers())
                                            .register(Expression.class, new ExpressionSerializer())
                                            .register(Material.class, new MaterialSerializer())
                                    ))
                            .path(context.getDataDirectory().resolve(model.getAnnotation(ConfigName.class).value()))
                            .build())
                    .build();

            return config;
        } catch (final ConfigurateException exception) {
            throw new UncheckedConfigurateException(exception);
        }
    }

    private static MessageService createMessageService(final PluginProviderContext context) {
        final ResourceBundle.Control control = new DynamicResourceBundleControl(context.getDataDirectory());
        final Collection<Locale> installedLocales = TranslationStoreInitializer.initialize(
                context,
                MessageService.class,
                control
        );
        final List<TextComponent> localeNames = installedLocales.stream()
                .map(Locale::getDisplayName)
                .map(Component::text)
                .toList();
        final Component formattedLocaleNames = Component.join(JoinConfiguration.arrayLike(), localeNames);
        context.getLogger().info("{}件の翻訳を読み込みました: {}", installedLocales.size(), formattedLocaleNames);

        return DoburokuStandard.of(MessageService.class)
                .argument(registry -> registry.plus(Player.class, (parameter, player) -> player.displayName()),
                        MiniMessageArgumentTransformer.create())
                .result(registry -> registry
                        .plus(Message.class, (method, component) -> audience -> {
                            // MiniPlaceholdersのプレースホルダーと解決に必要なオーディエンスを追加
                            final List<ComponentLike> arguments = new ArrayList<>(component.arguments());
                            arguments.add(Argument.tagResolver(MiniPlaceholdersExpansion.audiencePlaceholders()));
                            arguments.add(Argument.target(audience));

                            // TranslatableComponentを生成してオーディエンスに送信
                            final TranslatableComponent result = Component.translatable(component.key(), arguments);
                            audience.sendMessage(result);
                        })
                        .plus(Actionbar.class, (method, component) -> audience -> {
                            // MiniPlaceholdersのプレースホルダーと解決に必要なオーディエンスを追加
                            final List<ComponentLike> arguments = new ArrayList<>(component.arguments());
                            arguments.add(Argument.tagResolver(MiniPlaceholdersExpansion.audiencePlaceholders()));
                            arguments.add(Argument.target(audience));

                            // TranslatableComponentを生成してオーディエンスに送信
                            final TranslatableComponent result = Component.translatable(component.key(), arguments);
                            audience.sendActionBar(result);
                        }))
                .brew();
    }
}
