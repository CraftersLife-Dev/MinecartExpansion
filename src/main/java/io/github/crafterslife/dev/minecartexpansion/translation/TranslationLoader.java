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
package io.github.crafterslife.dev.minecartexpansion.translation;

import io.github.namiuni.kotonoha.annotations.Key;
import io.github.namiuni.kotonoha.annotations.Message;
import io.github.namiuni.kotonoha.translator.KotonohaTranslationStore;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class TranslationLoader {

    // JIS Z 9103 https://ja.wikipedia.org/wiki/JIS%E5%AE%89%E5%85%A8%E8%89%B2
    private static final TextColor RED = TextColor.color(0xFF4B00);
    private static final TextColor YELLOW = TextColor.color(0xF2E700);
    private static final TextColor GREEN = TextColor.color(0x00B06B);
    private static final TextColor BLUE = TextColor.color(0x1971FF);

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .tag("error", Tag.styling(RED))
                    .tag("warn", Tag.styling(YELLOW))
                    .tag("info", Tag.styling(GREEN))
                    .tag("debug", Tag.styling(BLUE))
                    .build())
            .build();

    private static final net.kyori.adventure.key.Key TRANSLATION_KEY = net.kyori.adventure.key.Key.key("template_plugin", "messages");
    private static final String FILE_PREFIX = "messages_";
    private static final String FILE_SUFFIX = ".properties";

    private TranslationLoader() {
    }

    static Translator load(final Path translationsDir) {
        final var store = KotonohaTranslationStore.miniMessage(TRANSLATION_KEY, MINI_MESSAGE);

        record LocaleFile(@Nullable Locale locale, Path file) {
        }

        try (Stream<Path> files = Files.list(translationsDir)) {
            files.filter(Files::isRegularFile)
                    .filter(TranslationLoader::isTranslationFile)
                    .map(file -> new LocaleFile(extractLocale(file), file))
                    .filter(localeFile -> Objects.nonNull(localeFile.locale))
                    .forEach(localeFile -> store.registerAll(localeFile.locale(), localeFile.file(), false));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }

        for (final Method method : MessageService.class.getMethods()) {
            final String key = method.getAnnotation(Key.class).value();
            for (final Message annotation : method.getAnnotationsByType(Message.class)) {
                final Locale locale = annotation.locale().asLocale();
                final String message = annotation.content();
                if (!store.contains(key, locale)) {
                    store.register(key, locale, message);
                }
            }
        }

        return store;
    }

    private static boolean isTranslationFile(final Path file) {
        final String name = file.getFileName().toString();
        return name.startsWith(FILE_PREFIX) && name.endsWith(FILE_SUFFIX);
    }

    private static @Nullable Locale extractLocale(final Path file) {
        final String name = file.getFileName().toString();
        final String localeTag = name.substring(FILE_PREFIX.length(), name.length() - FILE_SUFFIX.length());
        return Translator.parseLocale(localeTag);
    }
}
