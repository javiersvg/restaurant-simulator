package org.javiersvg.restaurantsimulator;

class CustomerArrivedEvent implements Event {

    private final int startTime;

    CustomerArrivedEvent(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public int startTime() {
        return startTime;
    }

    @Override
    public void handle(Visitor visitor) {
        visitor.visit(this);
    }
}
