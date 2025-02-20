package dev.booky.http.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {

    T get() throws E;
}
