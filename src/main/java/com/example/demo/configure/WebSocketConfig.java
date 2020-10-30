package com.example.demo.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        // These are endpoints the client can subscribes to.
        config.enableSimpleBroker("/queue/topic");
        // Message received with one of those below destinationPrefixes will be automatically router to controllers @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        // Handshake endpoint
        registry.addEndpoint("stomp"); // If you want to you can chain setAllowedOrigins("*")
    }
}
