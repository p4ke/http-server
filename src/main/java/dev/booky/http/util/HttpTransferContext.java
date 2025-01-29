package dev.booky.http.util;

import dev.booky.http.protocol.HttpHeaders;
import dev.booky.http.protocol.HttpHeaders.ParameterizedHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NullMarked
public class HttpTransferContext {

    private final Set<TransferEncoding> allowedEncodings;


    public static HttpTransferContext fromHeaders(final HttpHeaders headers) {
        // https://www.rfc-editor.org/rfc/rfc2616#section-14.39
        // TODO deduplicate types
        Set<TransferEncoding> allowedEncodings = headers.getParameterizedHeaders("TE")
                .map(TransferEncoding::fromHeader)
                .filter(Objects::nonNull)
                .collect(() -> EnumSet.noneOf(TransferEncoding.class), Collection::add, Collection::addAll);
    }

    public record TransferEncoding(
            TransferEncodingType type,
            OptionalDouble quality
    ) {

        public static @Nullable TransferEncoding fromHeader(final ParameterizedHeader header) {
            final TransferEncodingType type = TransferEncodingType.fromId(header.value());
            if (type == null) {
                return
            }
            final String qualityStr = header.parameters().get("q");
            final OptionalDouble quality = qualityStr == null ? OptionalDouble.empty() :
                    OptionalDouble.of(Double.parseDouble(qualityStr));
            return new TransferEncoding(type, quality);
        }
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

        public static @Nullable HttpTransferContext.TransferEncodingType fromId(final String id) {
            return BY_ID.get(id.toLowerCase(Locale.ROOT));
        }

        public String getId() {
            return this.id;
        }
    }
}
