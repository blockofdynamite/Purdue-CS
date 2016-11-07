import java.util.Arrays;

class CarLot implements Inventory<Car> {

    private int capacity;
    private Car[] cars;

    public CarLot(int capacity) {
        cars = new Car[capacity];
        Arrays.fill(cars, null);
        this.capacity = capacity;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int numVehicles() {
        return cars.length;
    }

    @Override
    public boolean add(Car vehicle) {
        for (int i = 0; i < capacity; i++) {
            if (cars[i] == null) {
                cars[i] = vehicle;
                return true;
            }
        }
        return false;
    }

    @Override
    public Car get(int location) {
        return cars[location];
    }

    @Override
    public Car remove(int location) {
        Car toReturn = cars[location];
        cars[location] = null;
        return toReturn;
    }

    @Override
    public boolean[] searchByMake(String make) {
        boolean[] carMakes = new boolean[cars.length];
        for (int i = 0; i < cars.length; i++) {
            try {
                if (cars[i].getMake().equals(make)) {
                    carMakes[i] = true;
                }
            } catch (NullPointerException e) {

            }
        }

        return carMakes;
    }

}