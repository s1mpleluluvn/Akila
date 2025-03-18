package com.akila.config;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.audit.annotation.ClientServer;
import org.apache.logging.log4j.audit.annotation.HeaderPrefix;
import org.apache.logging.log4j.core.util.UuidUtil;

import java.util.UUID;

@HeaderPrefix("vls-context-")
public class RequestContext {

    @ClientServer
    public static final String IP_ADDRESS = "ipAddress";

    @ClientServer
    public static final String REQUEST_ID = "requestId";

    @ClientServer
    public static final String USER_ID = "userId";

    public static void setIpAddress(String address) {
        ThreadContext.put(IP_ADDRESS, address);
    }

    public static String getIpAddress() {
        return ThreadContext.get(IP_ADDRESS);
    }

    public static String getRequestId() {
        String uuidStr = ThreadContext.get(REQUEST_ID);
        UUID uuid;
        if (uuidStr == null) {
            uuid = UuidUtil.getTimeBasedUuid();
            ThreadContext.put(REQUEST_ID, uuid.toString());
        }
        return uuidStr;
    }

    public static void setUserId(String userId) {
        ThreadContext.put(USER_ID, userId);
    }

    public static String getUserId() {
        return ThreadContext.get(USER_ID);
    }

    /**
     * The methods in this class are not required by framework components that
     * use the RequestContext properties. They are provided as a convenience for
     * applications. If they are not provided the properties can be accessed
     * directly through the Log4j ThreadContext Map using the keys above.
     */
    public static void clear() {
        ThreadContext.clearMap();
    }
}
