package org.javiersvg.restaurantsimulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestaurantSimulator {

    private static final Random random = new Random();
    private static final PriorityQueue<Event> prototypeQueue = new PriorityQueue<>(Comparator.comparingInt(Event::startTime));

    public static void main(String... args) {
        int maxNumberOfDailyCustomers = Integer.parseInt(args[0]);
        int customerArriveBound = Integer.parseInt(args[1]) * 60;
        int customerLeaveBound = Integer.parseInt(args[2]);
        boolean[] tables = new boolean[Integer.parseInt(args[3])];


        Queue<Event> eventQueue = getIndependentEventQueue(maxNumberOfDailyCustomers, customerArriveBound);
        Queue<Customer> customerQueue = new LinkedList<>();
        ArrayList<Integer> attentionTimes = new ArrayList<>();
        RestaurantVisitor restaurantVisitor = new RestaurantVisitor(random, customerLeaveBound, attentionTimes, tables, eventQueue, customerQueue);

        IntSummaryStatistics statistics = new DiscreteSimulator().simulate(eventQueue, restaurantVisitor,
                () -> {
                    if (!customerQueue.isEmpty()) {
                        throw new RuntimeException("customers left unattended");
                    }
                    return attentionTimes.stream().collect(Collectors.summarizingInt(value -> value));
                });
        System.out.println(statistics);
    }

    private static PriorityQueue<Event> getIndependentEventQueue(int maxNumberOfDailyCustomers, int customerArriveBound) {
        return Stream.generate(() -> random.nextInt(customerArriveBound))
                .limit(maxNumberOfDailyCustomers)
                .map(CustomerArrivedEvent::new)
                .collect(Collectors.toCollection(() -> prototypeQueue));
    }
}

