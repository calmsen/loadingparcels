package ru.calmsen.loadingparcels.schedule;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.service.OutboxService;

@Component
@RequiredArgsConstructor
public class ProcessOutboxMessagesScheduler {
    private final OutboxService outboxService;

    @Scheduled(fixedRateString = "${schedule.process-outbox-messages.interval}")
    @SchedulerLock(name = "process-outbox-messages")
    public void processOutboxMessages() {
        for(var message : outboxService.findAll()) {
            outboxService.publishOutboxMessage(message);
        }
    }
}
