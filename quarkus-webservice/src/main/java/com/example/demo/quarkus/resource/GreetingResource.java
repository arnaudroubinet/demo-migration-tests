package com.example.demo.quarkus.resource;

import com.example.demo.quarkus.model.ComplexGreetingRequest;
import com.example.demo.quarkus.model.ComplexGreetingResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.*;

@Path("/api/greetings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @POST
    @Path("/complex")
    public ComplexGreetingResponse complexGreeting(ComplexGreetingRequest request) {
        long startTime = System.currentTimeMillis();

        // Build the greeting message based on the complex configuration
        StringBuilder messageBuilder = new StringBuilder();
        
        ComplexGreetingRequest.GreetingConfiguration config = request.getConfiguration();
        List<String> transformations = new ArrayList<>();
        
        // Apply prefix
        if (config.getPrefix() != null && !config.getPrefix().isEmpty()) {
            messageBuilder.append(config.getPrefix()).append(" ");
            transformations.add("PREFIX_APPLIED");
        }
        
        // Build greeting based on language
        String greeting = buildGreetingByLanguage(config.getLanguage());
        messageBuilder.append(greeting);
        transformations.add("LANGUAGE_TRANSLATED");
        
        // Add username from context
        ComplexGreetingRequest.UserContext userContext = request.getUserContext();
        if (userContext != null && userContext.getUsername() != null) {
            messageBuilder.append(" ").append(userContext.getUsername());
            transformations.add("USERNAME_ADDED");
        }
        
        // Apply suffix
        if (config.getSuffix() != null && !config.getSuffix().isEmpty()) {
            messageBuilder.append(" ").append(config.getSuffix());
            transformations.add("SUFFIX_APPLIED");
        }
        
        // Apply uppercase transformation
        String message = messageBuilder.toString();
        if (config.isUppercase()) {
            message = message.toUpperCase();
            transformations.add("UPPERCASE_APPLIED");
        }
        
        // Apply max length constraint
        if (config.getMaxLength() > 0 && message.length() > config.getMaxLength()) {
            message = message.substring(0, config.getMaxLength()) + "...";
            transformations.add("TRUNCATED");
        }
        
        // Add timestamp if requested
        if (config.isIncludeTimestamp()) {
            message = message + " [" + LocalDateTime.now() + "]";
            transformations.add("TIMESTAMP_ADDED");
        }
        
        long processingTime = System.currentTimeMillis() - startTime;
        
        // Build processing metadata
        ComplexGreetingResponse.ProcessingMetadata metadata = new ComplexGreetingResponse.ProcessingMetadata(
            processingTime,
            "server-" + UUID.randomUUID().toString().substring(0, 8),
            "1.0.0",
            0,
            Map.of(
                "requestPriority", request.getPriority().toString(),
                "tagsCount", String.valueOf(request.getTags() != null ? request.getTags().size() : 0),
                "metadataKeys", request.getMetadata() != null ? String.join(",", request.getMetadata().keySet()) : ""
            )
        );
        
        // Build additional data
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("requestTimestamp", request.getTimestamp());
        additionalData.put("userRoles", userContext != null ? userContext.getRoles() : Collections.emptyList());
        additionalData.put("tags", request.getTags());
        
        // Build response
        ComplexGreetingResponse response = new ComplexGreetingResponse(
            message,
            metadata,
            ComplexGreetingResponse.ResponseStatus.SUCCESS,
            transformations,
            additionalData,
            LocalDateTime.now()
        );
        
        return response;
    }
    
    @GET
    @Path("/simple/{name}")
    public ComplexGreetingResponse simpleGreeting(@PathParam("name") String name) {
        // Even the "simple" endpoint returns a complex response
        long startTime = System.currentTimeMillis();
        
        String message = "Hello " + name + "!";
        long processingTime = System.currentTimeMillis() - startTime;
        
        ComplexGreetingResponse.ProcessingMetadata metadata = new ComplexGreetingResponse.ProcessingMetadata(
            processingTime,
            "server-simple",
            "1.0.0",
            0,
            Map.of("endpoint", "simple")
        );
        
        return new ComplexGreetingResponse(
            message,
            metadata,
            ComplexGreetingResponse.ResponseStatus.SUCCESS,
            List.of("SIMPLE_GREETING"),
            Map.of("inputName", name),
            LocalDateTime.now()
        );
    }
    
    private String buildGreetingByLanguage(String language) {
        if (language == null) {
            return "Hello";
        }
        
        return switch (language.toLowerCase()) {
            case "fr", "french" -> "Bonjour";
            case "es", "spanish" -> "Hola";
            case "de", "german" -> "Guten Tag";
            case "it", "italian" -> "Ciao";
            case "jp", "japanese" -> "こんにちは";
            default -> "Hello";
        };
    }
}
