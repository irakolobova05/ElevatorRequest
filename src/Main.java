import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numFloors = 10;
        int numElevators = 3;

        BlockingQueue<ElevatorRequest> globalQueue = new LinkedBlockingQueue<>();
        List<Elevator> elevators = new ArrayList<>();
        List<Thread> elevatorThreads = new ArrayList<>();

        // Создание лифтов и их потоков
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i + 1);
            elevators.add(elevator);
            Thread thread = new Thread(elevator);
            elevatorThreads.add(thread);
            thread.start();
        }

        // Запуск генератора запросов
        Thread generatorThread = new Thread(new RequestGenerator(globalQueue, numFloors));
        generatorThread.start();

        // Распределение запросов между лифтами
        while (true) {
            ElevatorRequest request = globalQueue.take();
            Elevator nearestElevator = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                synchronized (elevator) {
                    int distance = Math.abs(request.floor - elevator.currentFloor);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestElevator = elevator;
                    }
                }
            }

            if (nearestElevator != null) {
                nearestElevator.addRequest(request);
                System.out.println("[Диспетчер] На этаж " + request.floor + " движется лифт " + nearestElevator.id);
            }
        }
    }
}
