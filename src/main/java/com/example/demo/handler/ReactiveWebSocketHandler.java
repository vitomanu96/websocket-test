package com.example.demo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        final Mono<Void> input = webSocketSession.receive()
                .doOnNext((message) -> {
                    System.out.println("PAYLOAD " + message.getPayloadAsText());
                }).then();


        final Flux<String> source = Flux
                .interval(Duration.ofSeconds(1))
                .map(aLong -> "Message " + aLong);

        final Mono<Void> output =
                webSocketSession.send(source.map(webSocketSession::textMessage));

        return Mono.zip(input, output).then();
    }
}
