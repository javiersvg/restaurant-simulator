package org.javiersvg.restaurantsimulator;

interface Visitor {
    void visit(CustomerArrivedEvent customerArrivedEvent);

    void visit(CustomerLeaveEvent customerLeaveEvent);
}