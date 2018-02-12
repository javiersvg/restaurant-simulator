package org.javiersvg.restaurantsimulator;

interface Event {
    int startTime();
    void handle(Visitor visitor);
}
