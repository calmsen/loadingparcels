package ru.calmsen.loadingparcels.schedule;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.service.OutboxService;

@Component
public class ProcessOutboxMessagesScheduler {
    private final OutboxService outboxService;
    private final int batchSize;

    public ProcessOutboxMessagesScheduler(
            OutboxService outboxService,
            @Value("${schedule.process-outbox-messages.batch-size}")
            int batchSize
    ) {
        this.outboxService = outboxService;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedRateString = "${schedule.process-outbox-messages.interval}")
    @SchedulerLock(name = "process-outbox-messages")
    public void processOutboxMessages() {
        for (var message : outboxService.findMessages(batchSize)) {
            outboxService.processOutboxMessage(message);
        }
    }
}
