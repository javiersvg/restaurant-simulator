package org.javiersvg.restaurantsimulator;

import java.util.IntSummaryStatistics;
import java.util.Queue;
import java.util.function.Supplier;

class DiscreteSimulator {

    IntSummaryStatistics simulate(Queue<Event> eventsQueue,
                                  Visitor visitor,
                                  Supplier<IntSummaryStatistics> summarizer) {
        do {
            eventsQueue.peek().handle(visitor);
            eventsQueue.poll();
        } while (!eventsQueue.isEmpty());
        return summarizer.get();
    }
}
