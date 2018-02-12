package org.javiersvg.restaurantsimulator;

class Customer {
    private final int arriveTime;

    Customer(int clock) {
        this.arriveTime = clock;
    }

    int attentionTime(int clock) {
        return clock - this.arriveTime;
    }
}
