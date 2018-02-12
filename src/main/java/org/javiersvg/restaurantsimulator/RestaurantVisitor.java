package org.javiersvg.restaurantsimulator;

import java.util.List;
import java.util.Queue;
import java.util.Random;

public class RestaurantVisitor implements Visitor {
    private final Random random;
    private final int customerLeaveBound;
    private List<Integer> attentionTimes;
    private boolean[] tables;
    private Queue<Event> queue;
    private Queue<Customer> customerQueue;

    RestaurantVisitor(Random random,
                      int customerLeaveBound,
                      List<Integer> attentionTimes,
                      boolean[] tables,
                      Queue<Event> queue,
                      Queue<Customer> customerQueue) {
        this.random = random;
        this.customerLeaveBound = customerLeaveBound;
        this.attentionTimes = attentionTimes;
        this.tables = tables;
        this.queue = queue;
        this.customerQueue = customerQueue;
    }

    @Override
    public void visit(CustomerArrivedEvent customerArrivedEvent) {
        Customer customer = new Customer(customerArrivedEvent.startTime());
        for (int index = 0; index <tables.length; index++) {
            if (!tables[index]) {
                int customerLeaveEventStartTime = customerArrivedEvent.startTime() + random.nextInt(customerLeaveBound);
                queue.add(new CustomerLeaveEvent(customerLeaveEventStartTime, customer));
                tables[index] = true;
                return;
            }
        }
        customerQueue.add(customer);
    }

    @Override
    public void visit(CustomerLeaveEvent customerLeaveEvent) {
        attentionTimes.add(customerLeaveEvent.attentionTime());
        for (int index = 0; index <tables.length; index++) {
            if (tables[index]) {
                if (customerQueue.isEmpty()) {
                    tables[index] = false;
                } else {
                    int startTime = customerLeaveEvent.startTime() + random.nextInt(customerLeaveBound);
                    queue.add(new CustomerLeaveEvent(startTime, customerQueue.poll()));
                }
                return;
            }
        }
    }
}
