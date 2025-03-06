package dev.booky.http.util;

import org.jspecify.annotations.NullMarked;

// Ein Hilfsinterface, basierend auf der Java-Klasse "Supplier", welche
// allerdings einen generischen Fehler werfen kann - dies erlaubt
// das schönere Verarbeiten von Fehlern, welche von Methoden-Referenzen
// geworfen werden können
@NullMarked
@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {

    T get() throws E;
}
