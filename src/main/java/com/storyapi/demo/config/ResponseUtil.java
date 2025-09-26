package com.storyapi.demo.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> success(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    /**
     * Create success response with just data
     */
    public static Map<String, Object> success(Object data) {
        return success(data, "Operation successful");
    }

    public static Map<String, Object> error(String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errorCode", errorCode);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    public static Map<String, Object> error(String message) {
        return error(message, "GENERAL_ERROR");
    }

    public static Map<String, Object> paginated(Object data, int currentPage, int totalPages, long totalElements) {
        Map<String, Object> response = success(data);
        response.put("pagination",
                Map.of("currentPage", currentPage, "totalPages", totalPages, "totalElements", totalElements));
        return response;
    }
}
