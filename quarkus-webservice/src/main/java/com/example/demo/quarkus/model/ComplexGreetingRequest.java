package com.example.demo.quarkus.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Unnecessarily complex request object for a simple hello world service
 */
public class ComplexGreetingRequest {
    
    private UserContext userContext;
    private GreetingConfiguration configuration;
    private List<String> tags;
    private Map<String, String> metadata;
    private RequestPriority priority;
    private LocalDateTime timestamp;

    public ComplexGreetingRequest() {
    }

    public ComplexGreetingRequest(UserContext userContext, GreetingConfiguration configuration,
                                   List<String> tags, Map<String, String> metadata,
                                   RequestPriority priority, LocalDateTime timestamp) {
        this.userContext = userContext;
        this.configuration = configuration;
        this.tags = tags;
        this.metadata = metadata;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public GreetingConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GreetingConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class UserContext {
        private String userId;
        private String username;
        private String email;
        private List<String> roles;
        private Map<String, Object> preferences;

        public UserContext() {
        }

        public UserContext(String userId, String username, String email, List<String> roles, Map<String, Object> preferences) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.roles = roles;
            this.preferences = preferences;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Map<String, Object> getPreferences() {
            return preferences;
        }

        public void setPreferences(Map<String, Object> preferences) {
            this.preferences = preferences;
        }
    }

    public static class GreetingConfiguration {
        private String language;
        private boolean includeTimestamp;
        private boolean uppercase;
        private String prefix;
        private String suffix;
        private int maxLength;

        public GreetingConfiguration() {
        }

        public GreetingConfiguration(String language, boolean includeTimestamp, boolean uppercase,
                                      String prefix, String suffix, int maxLength) {
            this.language = language;
            this.includeTimestamp = includeTimestamp;
            this.uppercase = uppercase;
            this.prefix = prefix;
            this.suffix = suffix;
            this.maxLength = maxLength;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public boolean isIncludeTimestamp() {
            return includeTimestamp;
        }

        public void setIncludeTimestamp(boolean includeTimestamp) {
            this.includeTimestamp = includeTimestamp;
        }

        public boolean isUppercase() {
            return uppercase;
        }

        public void setUppercase(boolean uppercase) {
            this.uppercase = uppercase;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }
    }

    public enum RequestPriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
