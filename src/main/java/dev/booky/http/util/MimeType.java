package dev.booky.http.util;

import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.util.Map;

// Eine Hilfsklasse um MIME-Typen zu repräsentieren, welche auch
// einige Standard-MIME-Typen beinhaltet
@NullMarked
public final class MimeType {

    public static final MimeType TYPE_HTML_UTF8 = new MimeType("text", "html", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_PLAIN_UTF8 = new MimeType("text", "plain", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_OCTET_STREAM = new MimeType("application", "octet-stream");

    // Basierend auf den MIME-Typen von https://developer.mozilla.org/en-US/docs/Web/HTTP/MIME_types/Common_types
    public static final MimeType TYPE_AAC = new MimeType("audio", "aac");
    public static final MimeType TYPE_APNG = new MimeType("image", "apng");
    public static final MimeType TYPE_AVIF = new MimeType("image", "avif");
    public static final MimeType TYPE_AVI = new MimeType("video", "x-msvideo");
    public static final MimeType TYPE_BMP = new MimeType("image", "bmp");
    public static final MimeType TYPE_BZIP = new MimeType("application", "x-bzip");
    public static final MimeType TYPE_BZIP2 = new MimeType("application", "x-bzip2");
    public static final MimeType TYPE_CSS_UTF8 = new MimeType("text", "css", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_CSV_UTF8 = new MimeType("text", "csv", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_DOC = new MimeType("application", "msword");
    public static final MimeType TYPE_DOCX = new MimeType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MimeType TYPE_EPUB = new MimeType("application", "epub+zip");
    public static final MimeType TYPE_GZIP = new MimeType("application", "gzip");
    public static final MimeType TYPE_GIF = new MimeType("image", "gif");
    public static final MimeType TYPE_ICO = new MimeType("image", "vnd.microsoft.icon");
    public static final MimeType TYPE_ICS_UTF8 = new MimeType("text", "calendar", Map.of("charset", "utf8"));
    public static final MimeType TYPE_JAR = new MimeType("application", "java-archive");
    public static final MimeType TYPE_JPEG = new MimeType("image", "jpeg");
    public static final MimeType TYPE_JAVASCRIPT = new MimeType("text", "javascript", Map.of("charest", "utf8"));
    public static final MimeType TYPE_JSON_UTF8 = new MimeType("application", "json", Map.of("charest", "utf8"));
    public static final MimeType TYPE_JSON_LD_UTF8 = new MimeType("application", "ld+json", Map.of("charest", "utf8"));
    public static final MimeType TYPE_MIDI = new MimeType("audio", "midi");
    public static final MimeType TYPE_MP3 = new MimeType("audio", "mpeg");
    public static final MimeType TYPE_MP4 = new MimeType("video", "mp4");
    public static final MimeType TYPE_MPEG = new MimeType("video", "mpeg");
    public static final MimeType TYPE_ODP = new MimeType("application", "vnd.oasis.opendocument.presentation");
    public static final MimeType TYPE_ODS = new MimeType("application", "vnd.oasis.opendocument.spreadsheet");
    public static final MimeType TYPE_ODT = new MimeType("application", "vnd.oasis.opendocument.text");
    public static final MimeType TYPE_OGA = new MimeType("audio", "ogg");
    public static final MimeType TYPE_OGV = new MimeType("video", "ogg");
    public static final MimeType TYPE_OGX = new MimeType("application", "ogg");
    public static final MimeType TYPE_OTF = new MimeType("font", "otf");
    public static final MimeType TYPE_PNG = new MimeType("image", "png");
    public static final MimeType TYPE_PDF = new MimeType("application", "pdf");
    public static final MimeType TYPE_PHP_UTF8 = new MimeType("application", "x-httpd-php", Map.of("charset", "utf8"));
    public static final MimeType TYPE_PPT = new MimeType("application", "vnd.ms-powerpoint");
    public static final MimeType TYPE_PPTX = new MimeType("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MimeType TYPE_RAR = new MimeType("application", "vnd.rar");
    public static final MimeType TYPE_RTF_UTF8 = new MimeType("application", "rtf", Map.of("charset", "utf8"));
    public static final MimeType TYPE_SHELL_UTF8 = new MimeType("application", "x-sh", Map.of("charset", "utf8"));
    public static final MimeType TYPE_SVG_UTF8 = new MimeType("image", "svg+xml", Map.of("charset", "utf8"));
    public static final MimeType TYPE_TAR = new MimeType("application", "x-tar");
    public static final MimeType TYPE_TIFF = new MimeType("application", "x-tar");
    public static final MimeType TYPE_TS = new MimeType("video", "mp2t");
    public static final MimeType TYPE_TTF = new MimeType("font", "ttf");
    public static final MimeType TYPE_WAV = new MimeType("audio", "wav");
    public static final MimeType TYPE_WEBA = new MimeType("audio", "webm");
    public static final MimeType TYPE_WEBM = new MimeType("video", "webm");
    public static final MimeType TYPE_WEBP = new MimeType("image", "webp");
    public static final MimeType TYPE_WOFF = new MimeType("font", "woff");
    public static final MimeType TYPE_WOFF2 = new MimeType("font", "woff2");
    public static final MimeType TYPE_XHTML_UTF8 = new MimeType("application", "xhtml+xml", Map.of("charset", "utf8"));
    public static final MimeType TYPE_XLS = new MimeType("application", "vnd.ms-excel");
    public static final MimeType TYPE_XLSX = new MimeType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MimeType TYPE_XML_UTF8 = new MimeType("application", "xml", Map.of("charset", "utf8"));
    public static final MimeType TYPE_XUL_UTF8 = new MimeType("application", "vnd.mozilla.xul+xml", Map.of("charset", "utf8"));
    public static final MimeType TYPE_ZIP = new MimeType("application", "zip");
    public static final MimeType TYPE_7ZIP = new MimeType("application", "x-7z-compressed");

    public static final MimeType TYPE_BIN = TYPE_OCTET_STREAM;
    public static final MimeType TYPE_PLAIN = TYPE_PLAIN_UTF8;

    private final String type;
    private final String subtype;
    private final Map<String, String> properties;

    public MimeType(final String type, final String subtype) {
        this(type, subtype, Map.of());
    }

    public MimeType(
            final String type,
            final String subtype,
            final Map<String, String> properties
    ) {
        this.type = type;
        this.subtype = subtype;
        this.properties = properties;
    }

    // Basierend auf dem Dateinamen wird ein häufig genutzter MIME-Typ ausgewählt
    public static MimeType guessFromPathName(final Path path) {
        // Der Dateiname wird als Zeichenkette extrahiert
        final String pathName = path.getFileName().toString();
        // Es wird der letzte Punkt im Dateinamen gesucht, um
        // die Dateiendung herauszufinden
        final int fileExtIdx = pathName.lastIndexOf('.');
        if (fileExtIdx == -1) {
            // Falls keine Dateiendung vorliegt, wird angenommen, dass es sich
            // hier um eine Binärdatei handelt
            return TYPE_BIN;
        }
        // Falls eine Dateiendung vorliegt, wird basierend auf dieser Endung
        // ein bestimmter MIME-Typ ausgewählt; falls es sich dabei um ein Textformat
        // handelt, wird aktuell immer UTF-8 als Kodierung übermittelt
        final String fileExt = pathName.substring(fileExtIdx + 1);
        return switch (fileExt) {
            case "htm", "html" -> TYPE_HTML_UTF8;
            case "txt", "text" -> TYPE_PLAIN_UTF8;
            case "aac" -> TYPE_AAC;
            case "apng" -> TYPE_APNG;
            case "avif" -> TYPE_AVIF;
            case "avi" -> TYPE_AVI;
            case "bmp" -> TYPE_BMP;
            case "bz" -> TYPE_BZIP;
            case "bz2" -> TYPE_BZIP2;
            case "css" -> TYPE_CSS_UTF8;
            case "csv" -> TYPE_CSV_UTF8;
            case "doc" -> TYPE_DOC;
            case "docx" -> TYPE_DOCX;
            case "epub" -> TYPE_EPUB;
            case "gz" -> TYPE_GZIP;
            case "gif" -> TYPE_GIF;
            case "ico" -> TYPE_ICO;
            case "ics" -> TYPE_ICS_UTF8;
            case "jar" -> TYPE_JAR;
            case "jpg", "jpeg" -> TYPE_JPEG;
            case "js", "cjs", "mjs" -> TYPE_JAVASCRIPT;
            case "json" -> TYPE_JSON_UTF8;
            case "jsonld" -> TYPE_JSON_LD_UTF8;
            case "mid", "midi" -> TYPE_MIDI;
            case "mp3" -> TYPE_MP3;
            case "mp4" -> TYPE_MP4;
            case "mpeg" -> TYPE_MPEG;
            case "odp" -> TYPE_ODP;
            case "ods" -> TYPE_ODS;
            case "odt" -> TYPE_ODT;
            case "oga", "opus" -> TYPE_OGA;
            case "ogv" -> TYPE_OGV;
            case "ogx" -> TYPE_OGX;
            case "otf" -> TYPE_OTF;
            case "png" -> TYPE_PNG;
            case "pdf" -> TYPE_PDF;
            case "php" -> TYPE_PHP_UTF8;
            case "ppt" -> TYPE_PPT;
            case "pptx" -> TYPE_PPTX;
            case "rar" -> TYPE_RAR;
            case "rtf" -> TYPE_RTF_UTF8;
            case "sh" -> TYPE_SHELL_UTF8;
            case "svg" -> TYPE_SVG_UTF8;
            case "tar" -> TYPE_TAR;
            case "tif", "tiff" -> TYPE_TIFF;
            case "ts" -> TYPE_TS;
            case "ttf" -> TYPE_TTF;
            case "wav" -> TYPE_WAV;
            case "weba" -> TYPE_WEBA;
            case "webm" -> TYPE_WEBM;
            case "webp" -> TYPE_WEBP;
            case "woff" -> TYPE_WOFF;
            case "woff2" -> TYPE_WOFF2;
            case "xhtml" -> TYPE_XHTML_UTF8;
            case "xls" -> TYPE_XLS;
            case "xlsx" -> TYPE_XLSX;
            case "xml" -> TYPE_XML_UTF8;
            case "xul" -> TYPE_XUL_UTF8;
            case "zip" -> TYPE_ZIP;
            case "7z" -> TYPE_7ZIP;
            // Unbekannte Dateiendung, es wird angenommen, dass es
            // sich um eine Binärdatei handelt
            default -> TYPE_BIN;
        };
    }

    public String getType() {
        return this.type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public String buildPropertyString() {
        if (this.properties.isEmpty()) {
            // Falls keine Properties existieren, kann einfach ein leerer String zurückgegeben werden
            return "";
        }
        final StringBuilder string = new StringBuilder();
        for (final Map.Entry<String, String> property : this.properties.entrySet()) {
            // Beispiel: ";charset=utf-8" - so kann die resultierende Zeichenkette am Ende
            // einfach an den normalen MIME-Typ angehängt werden
            string.append(';').append(property.getKey()).append('=').append(property.getValue());
        }
        return string.toString();
    }

    @Override
    public String toString() {
        // Beispiele:
        // - "text/html;charset=utf-8" (UTF-8 kodierter Text)
        // - "application/zip" (ZIP komprimiertes Binärarchiv)
        return this.type + '/' + this.subtype + this.buildPropertyString();
    }
}
