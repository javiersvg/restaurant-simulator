package org.javiersvg.restaurantsimulator;

import java.util.List;
import java.util.Queue;
import java.util.Random;

class CustomerLeaveEvent implements Event {
    private final int startTime;
    private final Random random;
    private final int customerLeaveBound;
    private List<Integer> attentionTimes;

    CustomerLeaveEvent(int startTime, Random random, int customerLeaveBound, List<Integer> attentionTimes) {
        this.startTime = startTime;
        this.random = random;
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
            if (tables[index]) {
                if (customers.isEmpty()) {
                    tables[index] = false;
                } else {
                    queue.add(new CustomerLeaveEvent(clock + random.nextInt(customerLeaveBound), random, customerLeaveBound, attentionTimes));
                    attentionTimes.add(customers.poll().attentionTime(clock));
                }
                return;
            }
        }
    }
}
