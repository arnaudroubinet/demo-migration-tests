package com.example.demo.quarkus.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Unnecessarily complex response object for a simple hello world service
 */
public class ComplexGreetingResponse {
    
    private String message;
    private ProcessingMetadata processingMetadata;
    private ResponseStatus status;
    private List<String> appliedTransformations;
    private Map<String, Object> additionalData;
    private LocalDateTime responseTimestamp;

    public ComplexGreetingResponse() {
    }

    public ComplexGreetingResponse(String message, ProcessingMetadata processingMetadata,
                                    ResponseStatus status, List<String> appliedTransformations,
                                    Map<String, Object> additionalData, LocalDateTime responseTimestamp) {
        this.message = message;
        this.processingMetadata = processingMetadata;
        this.status = status;
        this.appliedTransformations = appliedTransformations;
        this.additionalData = additionalData;
        this.responseTimestamp = responseTimestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProcessingMetadata getProcessingMetadata() {
        return processingMetadata;
    }

    public void setProcessingMetadata(ProcessingMetadata processingMetadata) {
        this.processingMetadata = processingMetadata;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public List<String> getAppliedTransformations() {
        return appliedTransformations;
    }

    public void setAppliedTransformations(List<String> appliedTransformations) {
        this.appliedTransformations = appliedTransformations;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    public LocalDateTime getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(LocalDateTime responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }

    public static class ProcessingMetadata {
        private long processingTimeMs;
        private String serverId;
        private String version;
        private int retryCount;
        private Map<String, String> debugInfo;

        public ProcessingMetadata() {
        }

        public ProcessingMetadata(long processingTimeMs, String serverId, String version,
                                   int retryCount, Map<String, String> debugInfo) {
            this.processingTimeMs = processingTimeMs;
            this.serverId = serverId;
            this.version = version;
            this.retryCount = retryCount;
            this.debugInfo = debugInfo;
        }

        public long getProcessingTimeMs() {
            return processingTimeMs;
        }

        public void setProcessingTimeMs(long processingTimeMs) {
            this.processingTimeMs = processingTimeMs;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }

        public Map<String, String> getDebugInfo() {
            return debugInfo;
        }

        public void setDebugInfo(Map<String, String> debugInfo) {
            this.debugInfo = debugInfo;
        }
    }

    public enum ResponseStatus {
        SUCCESS, PARTIAL_SUCCESS, ERROR, TIMEOUT
    }
}
