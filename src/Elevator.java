import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

// Класс, представляющий лифт
class Elevator implements Runnable {
    public int id;
    int currentFloor = 0;
    private PriorityBlockingQueue<ElevatorRequest> requests = new PriorityBlockingQueue<>(
            10,
            Comparator.comparingInt(req -> Math.abs(req.floor - currentFloor))
    );
    private boolean running = true;

    public Elevator(int id) {
        this.id = id;
    }

    public synchronized void addRequest(ElevatorRequest request) {
        requests.add(request);
    }

    @Override
    public void run() {
        while (running) {
            try {
                ElevatorRequest request = requests.poll(1, TimeUnit.SECONDS);
                if (request != null) {
                    System.out.println("[Лифт " + id + "] Движется на этаж " + request.floor);
                    moveToFloor(request.floor);
                    System.out.println("[Лифт " + id + "] Достиг этаж " + request.floor);
                } else {
                    System.out.println("[Лифт " + id + "] Ждет запроса...");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void moveToFloor(int floor) {
        while (currentFloor != floor) {
            if (currentFloor < floor) currentFloor++;
            else currentFloor--;

            System.out.println("[Лифт " + id + "] на этаже " + currentFloor);
            try {
                Thread.sleep(300); // Симуляция времени на перемещение
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public synchronized void stop() {
        running = false;
    }
}
