// Запрос для вызова лифта
class ElevatorRequest {
    int floor;
    boolean goingUp;

    public ElevatorRequest(int floor, boolean goingUp) {
        this.floor = floor;
        this.goingUp = goingUp;
    }
}