package dev.booky.http.util;

import dev.booky.http.protocol.HttpHeaders.ParameterizedHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;

@NullMarked
public class MimeType {

    public static final MimeType TYPE_HTML_UTF8 = new MimeType("text", "html", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_PLAIN_UTF8 = new MimeType("text", "plain", Map.of("charset", "utf-8"));
    public static final MimeType TYPE_OCTET_STREAM = new MimeType("application", "octet-stream");

    // some types from https://developer.mozilla.org/en-US/docs/Web/HTTP/MIME_types/Common_types
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
    // TODO continue

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

    public static @Nullable MimeType fromHeader(final @Nullable ParameterizedHeader header) {
        if (header == null) {
            return null;
        }
        final String headerVal = header.value();
        final int splitIdx = headerVal.indexOf('/');
        if (splitIdx == -1) {
            throw new IllegalArgumentException("Invalid mime type: " + headerVal);
        }
        final String type = headerVal.substring(0, splitIdx).stripTrailing();
        final String subtype = headerVal.substring(splitIdx + 1).stripLeading();
        final Map<String, String> properties = header.parameters();
        return new MimeType(type, subtype, properties);
    }

    public static MimeType guessFromPathName(final Path path) {
        final String pathName = path.getFileName().toString();
        final int fileExtIdx = pathName.lastIndexOf('.');
        if (fileExtIdx == -1) {
            return TYPE_BIN;
        }
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
            default -> TYPE_BIN; // "bin" and default
        };
    }

    public boolean isAnyType() {
        return "*".equals(this.type) && "*".equals(this.subtype);
    }

    public boolean isAnySubType() {
        return "*".equals(this.subtype);
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
}
