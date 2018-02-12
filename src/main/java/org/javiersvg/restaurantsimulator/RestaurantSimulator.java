package org.javiersvg.restaurantsimulator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestaurantSimulator {

    private static final Random random = new Random();
    private final PriorityQueue<Event> prototypeQueue = new PriorityQueue<>(Comparator.comparingInt(Event::startTime));

    public static void main(String... args) {
        int maxNumberOfDailyCustomers = Integer.parseInt(args[0]);
        int customerArriveBound = Integer.parseInt(args[1]) * 60;
        int customerLeaveBound = Integer.parseInt(args[2]);
        boolean[] tables = new boolean[Integer.parseInt(args[3])];
        IntSummaryStatistics statistics = new RestaurantSimulator()
                .simulate(maxNumberOfDailyCustomers, customerArriveBound, customerLeaveBound, tables);
        System.out.println(statistics);
    }

    private IntSummaryStatistics simulate(int maxNumberOfDailyCustomers, int customerArriveBound, int customerLeaveBound, boolean[] tables) {
        Queue<Customer> customers = new ArrayDeque<>();
        List<Integer> attentionTimes = new ArrayList<>();
        PriorityQueue<Event> queue = Stream.generate(() -> random.nextInt(customerArriveBound))
                .limit(maxNumberOfDailyCustomers)
                .map(startTime -> getCustomerArrivedEvent(startTime, customerLeaveBound, attentionTimes))
                .collect(Collectors.toCollection(() -> prototypeQueue));

        do {
            Event event = queue.peek();
            int clock = event.startTime();
            event.handle(queue, clock, tables, customers);
            queue.poll();
        } while (!queue.isEmpty());

        if (!customers.isEmpty()) {
            throw new RuntimeException("customers left unattended");
        }

        return attentionTimes.stream().collect(Collectors.summarizingInt(value -> value));
    }

    private CustomerArrivedEvent getCustomerArrivedEvent(int startTime, int customerLeaveBound, List<Integer> attendedCustomers) {
        return new CustomerArrivedEvent(random, startTime, customerLeaveBound, attendedCustomers);
    }
}

interface Event {
    int startTime();
    void handle(Queue<Event> queue, int clock, boolean[] tables, Queue<Customer> customers);
}



