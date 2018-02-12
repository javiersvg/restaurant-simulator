package org.javiersvg.restaurantsimulator;

import java.util.List;
import java.util.Queue;
import java.util.Random;

class CustomerArrivedEvent implements Event {

    private final Random random;
    private final int startTime;
    private final int customerLeaveBound;
    private List<Integer> attentionTimes;

    CustomerArrivedEvent(Random random, int startTime, int customerLeaveBound, List<Integer> attentionTimes) {
        this.random = random;
        this.startTime = startTime;
        this.customerLeaveBound = customerLeaveBound;
        this.attentionTimes = attentionTimes;
    }

    @Override
    public int startTime() {
        return startTime;
    }

    @Override
    public void handle(Queue<Event> queue, int clock, boolean[] tables, Queue<Customer> customers) {
        for (int index = 0; index <tables.length; index++) {
            if (!tables[index]) {
                queue.add(new CustomerLeaveEvent(clock + random.nextInt(customerLeaveBound), random, customerLeaveBound, attentionTimes));
                tables[index] = true;
                return;
            }
        }
        customers.add(new Customer(clock));
    }
}
