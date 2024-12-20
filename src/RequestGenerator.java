import java.util.Random;
import java.util.concurrent.BlockingQueue;

// Генератор запросов
class RequestGenerator implements Runnable {
    private BlockingQueue<ElevatorRequest> requestQueue;
    private Random random = new Random();
    private int numFloors;

    public RequestGenerator(BlockingQueue<ElevatorRequest> requestQueue, int numFloors) {
        this.requestQueue = requestQueue;
        this.numFloors = numFloors;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(2000) + 1000); // Генерация запросов через случайные интервалы
                int floor = random.nextInt(numFloors) + 1;
                boolean goingUp = random.nextBoolean();
                ElevatorRequest request = new ElevatorRequest(floor, goingUp);
                requestQueue.put(request);
                System.out.println("Новый запрос: этаж " + floor + ", движение " + (goingUp ? "вверх" : "вниз"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
