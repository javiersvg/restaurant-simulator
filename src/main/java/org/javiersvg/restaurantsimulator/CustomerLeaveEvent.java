package org.javiersvg.restaurantsimulator;

class CustomerLeaveEvent implements Event {
    private final int startTime;
    private Customer customer;

    CustomerLeaveEvent(int startTime, Customer customer) {
        this.startTime = startTime;
        this.customer = customer;
    }

    @Override
    public int startTime() {
        return startTime;
    }

    @Override
    public void handle(Visitor visitor) {
        visitor.visit(this);
    }

    Integer attentionTime() {
        return this.customer.attentionTime(this.startTime());
    }
}
