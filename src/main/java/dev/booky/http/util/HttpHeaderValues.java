package dev.booky.http.util;

import dev.booky.http.protocol.HttpHeaders;
import dev.booky.http.protocol.HttpHeaders.ParameterizedHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NullMarked
public final class HttpHeaderValues {

    private final Map<TransferEncodingType, TransferEncoding> allowedEncodings;
    private final @Nullable TransferEncoding transferEncoding;

    private final @Nullable MimeType contentType;
    private final List<MimeType> acceptedContentTypes;

    public HttpHeaderValues(
            final Map<TransferEncodingType, TransferEncoding> allowedEncodings,
            final @Nullable TransferEncoding transferEncoding,
            final @Nullable MimeType contentType,
            final List<MimeType> acceptedContentTypes
    ) {
        this.allowedEncodings = Map.copyOf(allowedEncodings);
        this.transferEncoding = transferEncoding;
        this.contentType = contentType;
        this.acceptedContentTypes = acceptedContentTypes;
    }

    public static HttpHeaderValues fromHeaders(final HttpHeaders headers) {
        // https://www.rfc-editor.org/rfc/rfc2616#section-14.39
        final Map<TransferEncodingType, TransferEncoding> allowedEncodings = headers.getParameterizedHeaders("TE")
                .map(TransferEncoding::fromHeader)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(TransferEncoding::type, Function.identity()));

        // https://www.rfc-editor.org/rfc/rfc2616#section-14.41
        final ParameterizedHeader transferEncodingHeader = headers.getParameterizedHeader("Transfer-Encoding");
        final TransferEncoding transferEncoding = TransferEncoding.fromHeader(transferEncodingHeader);

        final ParameterizedHeader contentTypeHeader = headers.getParameterizedHeader("Content-Type");
        final MimeType contentType = MimeType.fromHeader(contentTypeHeader);

        return new HttpHeaderValues(allowedEncodings, transferEncoding, , );
    }

    public record TransferEncoding(
            TransferEncodingType type,
            OptionalDouble quality
    ) {

        public static @Nullable TransferEncoding fromHeader(final @Nullable ParameterizedHeader header) {
            if (header == null) {
                return null;
            }
            final TransferEncodingType type = TransferEncodingType.fromId(header.value());
            if (type == null) {
                return null;
            }
            final String qualityStr = header.parameters().get("q");
            final OptionalDouble quality = qualityStr == null ? OptionalDouble.empty() :
                    OptionalDouble.of(Double.parseDouble(qualityStr));
            return new TransferEncoding(type, quality);
        }
    }

    public Map<TransferEncodingType, TransferEncoding> getAllowedEncodings() {
        return this.allowedEncodings;
    }

    public @Nullable TransferEncoding getTransferEncoding() {
        return this.transferEncoding;
    }

    public enum TransferEncodingType {

        COMPRESS("compress"),
        DEFLATE("deflate"),
        GZIP("gzip"),
        TRAILERS("trailers"),
        ;

        private static final Map<String, TransferEncodingType> BY_ID = Arrays.stream(values())
                .collect(Collectors.toUnmodifiableMap(TransferEncodingType::getId, Function.identity()));
        private final String id;

        TransferEncodingType(final String id) {
            this.id = id.toLowerCase();
        }

        public static @Nullable TransferEncodingType fromId(final String id) {
            return BY_ID.get(id.toLowerCase(Locale.ROOT));
        }

        public String getId() {
            return this.id;
        }
    }
}
