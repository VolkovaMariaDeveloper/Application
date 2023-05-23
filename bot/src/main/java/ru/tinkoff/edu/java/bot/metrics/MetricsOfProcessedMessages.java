package ru.tinkoff.edu.java.bot.metrics;

import io.micrometer.core.instrument.Metrics;

public class MetricsOfProcessedMessages {
    public static void incrementMessages(){
        Metrics.counter("numberOfProcessedMessages").increment();
    }
}
