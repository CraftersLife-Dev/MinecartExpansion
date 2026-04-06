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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslatorHolder implements Supplier<Translator> {

    private final Path translationsDir;
    private final AtomicReference<Translator> translator;

    public TranslatorHolder(final Path dataDirectory) {
        this.translationsDir = dataDirectory.resolve("translations");

        try {
            Files.createDirectories(this.translationsDir);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }

        final Translator initial = TranslationLoader.load(this.translationsDir);
        this.translator = new AtomicReference<>(initial);
        GlobalTranslator.translator().addSource(initial);
    }

    public Translator reload() {
        final Translator result = TranslationLoader.load(this.translationsDir);

        GlobalTranslator.translator().removeSource(this.translator.getAndSet(result));
        GlobalTranslator.translator().addSource(result);

        return result;
    }

    @Override
    public Translator get() {
        return this.translator.get();
    }
}
