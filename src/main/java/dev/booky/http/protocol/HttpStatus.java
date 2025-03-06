package dev.booky.http.protocol;

import org.jetbrains.annotations.ApiStatus;

// Ein Http Antwort-Status-Code, mit einigen standardisierten Codes
public final class HttpStatus {

    // Informational responses (100 – 199): https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#informational_responses
    public static final HttpStatus STATUS_CONTINUE = new HttpStatus(100, "Continue");
    public static final HttpStatus STATUS_SWITCHING_PROTOCOLS = new HttpStatus(101, "Switching Protocols");
    @Deprecated
    public static final HttpStatus STATUS_PROCESSING = new HttpStatus(102, "Processing"); // WebDAV
    public static final HttpStatus STATUS_EARLY_HINTS = new HttpStatus(103, "Early Hints");

    // Successful responses (200 – 299): https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#successful_responses
    public static final HttpStatus STATUS_OK = new HttpStatus(200, "OK");
    public static final HttpStatus STATUS_CREATED = new HttpStatus(201, "Created");
    public static final HttpStatus STATUS_ACCEPTED = new HttpStatus(202, "Accepted");
    public static final HttpStatus STATUS_NON_AUTHORITATIVE_INFORMATION = new HttpStatus(203, "Non-Authoritative Information");
    public static final HttpStatus STATUS_NO_CONTENT = new HttpStatus(204, "No Content");
    public static final HttpStatus STATUS_RESET_CONTENT = new HttpStatus(205, "Reset Content");
    public static final HttpStatus STATUS_PARTIAL_CONTENT = new HttpStatus(206, "Partial Content");
    public static final HttpStatus STATUS_MULTI_STATUS = new HttpStatus(207, "Multi-Status"); // WebDAV
    public static final HttpStatus STATUS_ALREADY_REPORTED = new HttpStatus(208, "Already Reported"); // WebDAV
    public static final HttpStatus STATUS_IM_USED = new HttpStatus(226, "IM Used"); // HTTP Delta encoding

    // Redirection responses (300 – 399): https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#redirection_responses
    public static final HttpStatus STATUS_MULTIPLE_CHOICES = new HttpStatus(300, "Multiple Choices");
    public static final HttpStatus STATUS_MOVED_PERMANENTLY = new HttpStatus(301, "Moved Permanently");
    public static final HttpStatus STATUS_FOUND = new HttpStatus(302, "Found");
    public static final HttpStatus STATUS_SEE_OTHER = new HttpStatus(303, "See Other");
    public static final HttpStatus STATUS_NOT_MODIFIED = new HttpStatus(304, "Not Modified");
    @Deprecated
    public static final HttpStatus STATUS_USE_PROXY = new HttpStatus(305, "Use Proxy");
    public static final HttpStatus STATUS_TEMPORARY_REDIRECT = new HttpStatus(307, "Temporary Redirect");
    public static final HttpStatus STATUS_PERMANENT_REDIRECT = new HttpStatus(308, "Permanent Redirect");

    // Client error responses (400 – 499): https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#client_error_responses
    public static final HttpStatus STATUS_BAD_REQUEST = new HttpStatus(400, "Bad Request");
    public static final HttpStatus STATUS_UNAUTHORIZED = new HttpStatus(401, "Unauthorized");
    public static final HttpStatus STATUS_PAYMENT_REQUIRED = new HttpStatus(402, "Payment Required");
    public static final HttpStatus STATUS_FORBIDDEN = new HttpStatus(403, "Forbidden");
    public static final HttpStatus STATUS_NOT_FOUND = new HttpStatus(404, "Not Found");
    public static final HttpStatus STATUS_METHOD_NOT_ALLOWED = new HttpStatus(405, "Method Not Allowed");
    public static final HttpStatus STATUS_NOT_ACCEPTABLE = new HttpStatus(406, "Not Acceptable");
    public static final HttpStatus STATUS_PROXY_AUTHENTICATION_REQUIRED = new HttpStatus(407, "Proxy Authentication Required");
    public static final HttpStatus STATUS_REQUEST_TIMEOUT = new HttpStatus(408, "Request Timeout");
    public static final HttpStatus STATUS_CONFLICT = new HttpStatus(409, "Conflict");
    public static final HttpStatus STATUS_GONE = new HttpStatus(410, "Gone");
    public static final HttpStatus STATUS_LENGTH_REQUIRED = new HttpStatus(411, "Length Required");
    public static final HttpStatus STATUS_PRECONDITION_FAILED = new HttpStatus(412, "Precondition Failed");
    public static final HttpStatus STATUS_CONTENT_TOO_LARGE = new HttpStatus(413, "Content Too Large");
    public static final HttpStatus STATUS_URI_TOO_LONG = new HttpStatus(414, "URI Too Long");
    public static final HttpStatus STATUS_UNSUPPORTED_MEDIA_TYPE = new HttpStatus(415, "Unsupported Media Type");
    public static final HttpStatus STATUS_RANGE_NOT_SATISFIABLE = new HttpStatus(416, "Range Not Satisfiable");
    public static final HttpStatus STATUS_EXPECTATION_FAILED = new HttpStatus(417, "Expectation Failed");
    public static final HttpStatus STATUS_IM_A_TEAPOT = new HttpStatus(418, "I'm a teapot");
    public static final HttpStatus STATUS_MISDIRECTED_REQUEST = new HttpStatus(421, "Misdirected Request");
    public static final HttpStatus STATUS_UNPROCESSABLE_CONTENT = new HttpStatus(422, "Unprocessable Content"); // WebDAV
    public static final HttpStatus STATUS_LOCKED = new HttpStatus(423, "Locked"); // WebDAV
    public static final HttpStatus STATUS_FAILED_DEPENDENCY = new HttpStatus(424, "Failed Dependency"); // WebDAV
    @ApiStatus.Experimental
    public static final HttpStatus STATUS_TOO_EARLY = new HttpStatus(425, "Too Early");
    public static final HttpStatus STATUS_UPGRADE_REQUIRED = new HttpStatus(426, "Upgrade Required");
    public static final HttpStatus STATUS_PRECONDITION_REQUIRED = new HttpStatus(428, "Precondition Required");
    public static final HttpStatus STATUS_TOO_MANY_REQUESTS = new HttpStatus(429, "Too Many Requests");
    public static final HttpStatus STATUS_REQUEST_HEADER_FIELDS_TOO_LARGE = new HttpStatus(431, "Request Header Fields Too Large");
    public static final HttpStatus STATUS_UNAVAILABLE_FOR_LEGAL_REASONS = new HttpStatus(451, "Unavailable For Legal Reasons");

    // Server error responses (500 – 599): https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#server_error_responses
    public static final HttpStatus STATUS_INTERNAL_SERVER_ERROR = new HttpStatus(500, "Internal Server Error");
    public static final HttpStatus STATUS_NOT_IMPLEMENTED = new HttpStatus(501, "Not Implemented");
    public static final HttpStatus STATUS_BAD_GATEWAY = new HttpStatus(502, "Bad Gateway");
    public static final HttpStatus STATUS_SERVICE_UNAVAILABLE = new HttpStatus(503, "Service Unavailable");
    public static final HttpStatus STATUS_GATEWAY_TIMEOUT = new HttpStatus(504, "Gateway Timeout");
    public static final HttpStatus STATUS_HTTP_VERSION_NOT_SUPPORTED = new HttpStatus(505, "HTTP Version Not Supported");
    public static final HttpStatus STATUS_VARIANT_ALSO_NEGOTIATES = new HttpStatus(506, "Variant Also Negotiates");
    public static final HttpStatus STATUS_INSUFFICIENT_STORAGE = new HttpStatus(507, "Insufficient Storage"); // WebDAV
    public static final HttpStatus STATUS_LOOP_DETECTED = new HttpStatus(508, "Loop Detected"); // WebDAV
    public static final HttpStatus STATUS_NOT_EXTENDED = new HttpStatus(510, "Not Extended");
    public static final HttpStatus STATUS_NETWORK_AUTHENTICATION_REQUIRED = new HttpStatus(511, "Network Authentication Required");

    private final int code;
    private final String name;

    public HttpStatus(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name;
    }
}
