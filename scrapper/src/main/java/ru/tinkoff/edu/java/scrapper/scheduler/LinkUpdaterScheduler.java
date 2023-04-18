package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
   // private final LinkService linkService;
    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
       // linkService.getAllLinks();


    Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);
        logger.info("simple log!");
    }
}
