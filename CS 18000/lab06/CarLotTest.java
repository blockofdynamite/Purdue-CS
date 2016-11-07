import junit.framework.TestCase;

public class CarLotTest extends TestCase {

    @org.junit.Test
    public void testCapacity() {

    }

    @org.junit.Test
    public void testNumVehicles() {
        CarLot carLot = new CarLot(10);
        carLot.add(new Car("red", "ford"));    // 0
        carLot.add(new Car("blue", "gm"));     // 1
        carLot.add(new Car("yellow", "ford")); // 2
        carLot.add(new Car("red", "chevy"));   // 3
        carLot.add(new Car("blue", "ford"));   // 4
        carLot.add(new Car("orange", "gm"));   // 5
        assertEquals(carLot.capacity(), 10);
    }

    @org.junit.Test
    public void testAdd() {
        CarLot carLot = new CarLot(10);
        carLot.add(new Car("red", "ford"));    // 0
        carLot.add(new Car("blue", "gm"));     // 1
        carLot.add(new Car("yellow", "ford")); // 2
        carLot.add(new Car("red", "chevy"));   // 3
        carLot.add(new Car("blue", "ford"));   // 4
        carLot.add(new Car("orange", "gm"));   // 5
        assertEquals(carLot.add(new Car("honda", "red")), true);
    }

    @org.junit.Test
    public void testGet() {
        CarLot carLot = new CarLot(10);
        carLot.add(new Car("red", "ford"));    // 0
        carLot.add(new Car("blue", "gm"));     // 1
        carLot.add(new Car("yellow", "ford")); // 2
        Car test = new Car("red", "chevy"); // test car
        carLot.add(test);   // 3
        carLot.add(new Car("blue", "ford"));   // 4
        carLot.add(new Car("orange", "gm"));   // 5
        assertEquals(carLot.get(3), test);
    }

    @org.junit.Test
    public void testRemove() {
        CarLot carLot = new CarLot(10);
        carLot.add(new Car("red", "ford"));    // 0
        carLot.add(new Car("blue", "gm"));     // 1
        carLot.add(new Car("yellow", "ford")); // 2
        carLot.add(new Car("red", "chevy"));   // 3
        carLot.add(new Car("blue", "ford"));   // 4
        carLot.add(new Car("orange", "gm"));   // 5
        assertSame(carLot.get(3), carLot.remove(3));
    }

    @org.junit.Test
    public void testSearchByMake() {
        CarLot carLot = new CarLot(10);
        carLot.add(new Car("red", "ford"));    // 0
        carLot.add(new Car("blue", "gm"));     // 1
        carLot.add(new Car("yellow", "ford")); // 2
        carLot.add(new Car("red", "chevy"));   // 3
        carLot.add(new Car("blue", "ford"));   // 4
        carLot.add(new Car("orange", "gm"));   // 5
        boolean[] test = {false, true, false, false, false, true};
        assertEquals(carLot.searchByMake("gm").equals(test), test.equals(carLot.searchByMake("gm")));
    }
}