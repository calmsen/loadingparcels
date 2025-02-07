package ru.calmsen.loadingparcels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import ru.calmsen.loadingparcels.model.domain.OutboxMessage;
import ru.calmsen.loadingparcels.repository.OutboxRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final StreamBridge streamBridge;

    @Transactional
    public void processOutboxMessage(OutboxMessage message) {
        streamBridge.send(
                String.format("%s-out-0", message.getMessageType()),
                buildKafkaMessage(message)
        );
        outboxRepository.deleteById(message.getId());
    }

    private Message<String> buildKafkaMessage(OutboxMessage message) {
        return MessageBuilder
                .withPayload(message.getPayload())
                .setHeader(KafkaHeaders.KEY, message.getUser().getBytes(StandardCharsets.UTF_8))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }

    public List<OutboxMessage> findMessages(int limit) {
        return outboxRepository.findByOrderByCreatedAtAsc(PageRequest.of(0, limit));
    }
}
