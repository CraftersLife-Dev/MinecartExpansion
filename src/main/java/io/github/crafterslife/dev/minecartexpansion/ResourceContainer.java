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
import io.github.crafterslife.dev.minecartexpansion.translation.MessageService;
import io.github.crafterslife.dev.minecartexpansion.translation.TranslatorHolder;
import io.github.namiuni.kotonoha.translatable.message.KotonohaMessage;
import io.github.namiuni.kotonoha.translatable.message.configuration.FormatTypes;
import io.github.namiuni.kotonoha.translatable.message.policy.argument.TranslationArgumentAdaptationPolicy;
import io.github.namiuni.kotonoha.translatable.message.policy.argument.tag.TagNameResolver;
import io.github.namiuni.kotonoha.translatable.message.utility.TranslationArgumentAdapter;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.bukkit.Material;
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
        TranslatorHolder translatorHolder,
        MessageService messageService
) {

    /**
     * このレコードのインスタンスを生成する。
     *
     * @param context リソースの初期化に必要なプラグインコンテキスト
     * @return このレコードのインスタンス
     */
    public static ResourceContainer create(final PluginProviderContext context) {
        final TranslatorHolder translatorHolder = ResourceContainer.createTranslatorHolder(context);
        final MessageService messageService = ResourceContainer.createMessageService();
        final PrimaryConfig primaryConfig = ResourceContainer.createConfig(context, PrimaryConfig.class, new PrimaryConfig());
        final String configName = PrimaryConfig.class.getAnnotation(ConfigName.class).value();
        context.getLogger().info("設定を読み込みました: {}", configName);

        return new ResourceContainer(primaryConfig, translatorHolder, messageService);
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

    private static TranslatorHolder createTranslatorHolder(final PluginProviderContext context) {
        return new TranslatorHolder(context.getDataDirectory());
    }

    private static MessageService createMessageService() {
        final var argumentPolicy = TranslationArgumentAdaptationPolicy.miniMessage(
                TranslationArgumentAdapter.standard(),
                TagNameResolver.annotationOrParameterNameResolver()
        );
        final var config = FormatTypes.MINI_MESSAGE.withArgumentPolicy(argumentPolicy);

        return KotonohaMessage.createProxy(MessageService.class, config);
    }
}
