import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestaurantSimulator {

    private static int clock;
    private static final Random random = new Random();

    public static void main(String... args) {
        int maxNumberOfDailyCustomers = Integer.parseInt(args[0]);
        int customerArriveBound = Integer.parseInt(args[1]) * 60;
        int customerLeaveBound = Integer.parseInt(args[2]);
        PriorityQueue<Event> prototypeQueue = new PriorityQueue<>(Comparator.comparingInt(Event::startTime));

        PriorityQueue<Event> queue = Stream.generate(() -> random.nextInt(customerArriveBound))
                .limit(maxNumberOfDailyCustomers)
                .map(startTime -> getCustomerArrivedEvent(startTime, customerLeaveBound))
                .collect(Collectors.toCollection(() -> prototypeQueue));

        do {
            clock += queue.peek().startTime();
            queue.poll().handle(queue, clock);
        } while (!queue.isEmpty());
    }

    private static CustomerArrivedEvent getCustomerArrivedEvent(int startTime, int customerLeaveBound) {
        return new CustomerArrivedEvent(random, startTime, customerLeaveBound);
    }
}

interface Event {
    int startTime();
    void handle(Queue<Event> queue, int clock);
}

class CustomerArrivedEvent implements Event {

    private final Random random;
    private final int startTime;
    private final int customerLeaveBound;

    CustomerArrivedEvent(Random random, int startTime, int customerLeaveBound) {
        this.random = random;
        this.startTime = startTime;
        this.customerLeaveBound = customerLeaveBound;
    }

    @Override
    public int startTime() {
        return startTime;
    }

    @Override
    public void handle(Queue<Event> queue, int clock) {
        queue.add(new CustomerLeavesEvent(clock + random.nextInt(customerLeaveBound)));
        System.out.println("customer comes in: " + clock);
    }
}

class CustomerLeavesEvent implements Event {
    private final int startTime;

    CustomerLeavesEvent(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public int startTime() {
        return startTime;
    }

    @Override
    public void handle(Queue<Event> queue, int clock) {
        System.out.println("customer goes out: " + clock);
    }
}