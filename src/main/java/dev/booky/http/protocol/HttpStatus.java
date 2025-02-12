package dev.booky.http.protocol;

// default status codes taken from https://http.cat/
public final class HttpStatus {

    // 1XX
    public static final HttpStatus STATUS_CONTINUE = new HttpStatus(100, "Continue");
    public static final HttpStatus STATUS_SWITCHING_PROTOCOLS = new HttpStatus(101, "Switching Protocols");
    public static final HttpStatus STATUS_PROCESSING = new HttpStatus(102, "Processing");
    public static final HttpStatus STATUS_EARLY_HINTS = new HttpStatus(103, "Early Hints");

    // 2XX
    public static final HttpStatus STATUS_OK = new HttpStatus(200, "OK");
    public static final HttpStatus STATUS_CREATED = new HttpStatus(201, "Created");
    public static final HttpStatus STATUS_ACCEPTED = new HttpStatus(202, "Accepted");
    public static final HttpStatus STATUS_NON_AUTHORITATIVE_INFORMATION = new HttpStatus(203, "Non-Authoritative Information");
    public static final HttpStatus STATUS_NO_CONTENT = new HttpStatus(204, "No Content");
    public static final HttpStatus STATUS_RESET_CONTENT = new HttpStatus(205, "Reset Content");
    public static final HttpStatus STATUS_PARTIAL_CONTENT = new HttpStatus(206, "Partial Content");
    public static final HttpStatus STATUS_MULTI_STATUS = new HttpStatus(207, "Multi-Status");
    public static final HttpStatus STATUS_ALREADY_REPORTED = new HttpStatus(208, "Already Reported");
    public static final HttpStatus STATUS_TRANSFORMATION_APPLIED = new HttpStatus(214, "Transformation Applied");
    public static final HttpStatus STATUS_IM_USED = new HttpStatus(226, "IM Used");

    // 3XX
    public static final HttpStatus STATUS_MULTIPLE_CHOICES = new HttpStatus(300, "Multiple Choices");
    public static final HttpStatus STATUS_MOVED_PERMANENTLY = new HttpStatus(301, "Moved Permanently");
    public static final HttpStatus STATUS_FOUND = new HttpStatus(302, "Found");
    public static final HttpStatus STATUS_SEE_OTHER = new HttpStatus(303, "See Other");
    public static final HttpStatus STATUS_NOT_MODIFIED = new HttpStatus(304, "Not Modified");
    public static final HttpStatus STATUS_USE_PROXY = new HttpStatus(305, "Use Proxy");
    public static final HttpStatus STATUS_TEMPORARY_REDIRECT = new HttpStatus(307, "Temporary Redirect");
    public static final HttpStatus STATUS_PERMANENT_REDIRECT = new HttpStatus(308, "Permanent Redirect");

    // 4XX
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
    public static final HttpStatus STATUS_PAYLOAD_TOO_LARGE = new HttpStatus(413, "Payload Too Large");
    public static final HttpStatus STATUS_REQUEST_URI_TOO_LONG = new HttpStatus(414, "Request-URI Too Long");
    public static final HttpStatus STATUS_UNSUPPORTED_MEDIA_TYPE = new HttpStatus(415, "Unsupported Media Type");
    public static final HttpStatus STATUS_REQUEST_RANGE_NOT_SATISFIABLE = new HttpStatus(416, "Request Range Not Satisfiable");
    public static final HttpStatus STATUS_EXPECTATION_FAILED = new HttpStatus(417, "Expectation Failed");
    public static final HttpStatus STATUS_IM_A_TEAPOT = new HttpStatus(418, "I'm a teapot");
    public static final HttpStatus STATUS_PAGE_EXPIRED = new HttpStatus(419, "Page Expired");
    public static final HttpStatus STATUS_ENHANCE_YOUR_CALM = new HttpStatus(420, "Enhance Your Calm");
    public static final HttpStatus STATUS_MISDIRECTED_REQUEST = new HttpStatus(421, "Misdirected Request");
    public static final HttpStatus STATUS_UNPROCESSABLE_ENTITY = new HttpStatus(422, "Unprocessable Entity");
    public static final HttpStatus STATUS_LOCKED = new HttpStatus(423, "Locked");
    public static final HttpStatus STATUS_FAILED_DEPENDENCY = new HttpStatus(424, "Failed Dependency");
    public static final HttpStatus STATUS_TOO_EARLY = new HttpStatus(425, "Too Early");
    public static final HttpStatus STATUS_UPGRADE_REQUIRED = new HttpStatus(426, "Upgrade Required");
    public static final HttpStatus STATUS_PRECONDITION_REQUIRED = new HttpStatus(428, "Precondition Required");
    public static final HttpStatus STATUS_TOO_MANY_REQUESTS = new HttpStatus(429, "Too Many Requests");
    public static final HttpStatus STATUS_REQUEST_HEADER_FIELDS_TOO_LARGE = new HttpStatus(431, "Request Header Fields Too Large");
    public static final HttpStatus STATUS_NO_RESPONSE = new HttpStatus(444, "No Response");
    public static final HttpStatus STATUS_BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS = new HttpStatus(450, "Blocked by Windows Parental Controls");
    public static final HttpStatus STATUS_UNAVAILABLE_FOR_LEGAL_REASONS = new HttpStatus(451, "Unavailable For Legal Reasons");
    public static final HttpStatus STATUS_SSL_CERTIFICATE_ERROR = new HttpStatus(495, "SSL Certificate Error");
    public static final HttpStatus STATUS_SSL_CERTIFICATE_REQUIRED = new HttpStatus(496, "SSL Certificate Required");
    public static final HttpStatus STATUS_HTTP_REQUEST_SENT_TO_HTTPS_PORT = new HttpStatus(497, "HTTP Request Sent to HTTPS Port");
    public static final HttpStatus STATUS_TOKEN_EXPIRED_OR_INVALID = new HttpStatus(498, "Token expired/invalid");
    public static final HttpStatus STATUS_CLIENT_CLOSED_REQUEST = new HttpStatus(499, "Client Closed Request");

    // 5XX
    public static final HttpStatus STATUS_INTERNAL_SERVER_ERROR = new HttpStatus(500, "Internal Server Error");
    public static final HttpStatus STATUS_NOT_IMPLEMENTED = new HttpStatus(501, "Not Implemented");
    public static final HttpStatus STATUS_BAD_GATEWAY = new HttpStatus(502, "Bad Gateway");
    public static final HttpStatus STATUS_SERVICE_UNAVAILABLE = new HttpStatus(503, "Service Unavailable");
    public static final HttpStatus STATUS_GATEWAY_TIMEOUT = new HttpStatus(504, "Gateway Timeout");
    public static final HttpStatus STATUS_VARIANT_ALSO_NEGOTIATES = new HttpStatus(506, "Variant Also Negotiates");
    public static final HttpStatus STATUS_INSUFFICIENT_STORAGE = new HttpStatus(507, "Insufficient Storage");
    public static final HttpStatus STATUS_LOOP_DETECTED = new HttpStatus(508, "Loop Detected");
    public static final HttpStatus STATUS_BANDWIDTH_LIMIT_EXCEEDED = new HttpStatus(509, "Bandwidth Limit Exceeded");
    public static final HttpStatus STATUS_NOT_EXTENDED = new HttpStatus(510, "Not Extended");
    public static final HttpStatus STATUS_NETWORK_AUTHENTICATION_REQUIRED = new HttpStatus(511, "Network Authentication Required");
    public static final HttpStatus STATUS_WEB_SERVER_IS_DOWN = new HttpStatus(521, "Web Server Is Down");
    public static final HttpStatus STATUS_CONNECTION_TIMED_OUT = new HttpStatus(522, "Connection Timed Out");
    public static final HttpStatus STATUS_ORIGIN_IS_UNREACHABLE = new HttpStatus(523, "Origin Is Unreachable");
    public static final HttpStatus STATUS_SSL_HANDSHAKE_FAILED = new HttpStatus(525, "SSL Handshake Failed");
    public static final HttpStatus STATUS_SITE_FROZEN = new HttpStatus(530, "Site Frozen");
    public static final HttpStatus STATUS_NETWORK_CONNECT_TIMEOUT_ERROR = new HttpStatus(599, "Network Connect Timeout Error");

    private final int code;
    private final String status;

    public HttpStatus(final int code, final String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return this.code;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.code + " " + this.status;
    }
}
