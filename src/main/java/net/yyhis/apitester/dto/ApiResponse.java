package net.yyhis.apitester.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class ApiResponse {
    private String threadId;
    private final String body;
    private final int statusCode;
    private final long contentLength;
    private final long executionTime;

    public ApiResponse(String body, int statusCode, long contentLength, long executionTime) {
        this.body = body;
        this.statusCode = statusCode;
        this.contentLength = contentLength;
        this.executionTime = executionTime;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    @Override
    public String toString() {
        return "ApiResponse {" +
                "threadId='" + threadId  +
                ", statusCode=" + statusCode +
                ", contentLength=" + contentLength + " bytes" +
                ", executionTime=" + executionTime + " ns" +
                ", body=" + body +
                '}';
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "threadId", threadId,
            "statusCode", statusCode,
            "contentLength", contentLength + " bytes",
            "executionTime", executionTime / 1_000_000 + " ms"
            );
    }
}

