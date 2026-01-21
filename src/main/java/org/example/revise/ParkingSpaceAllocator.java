package org.example.revise;

import java.util.*;
import java.util.stream.Collectors;

public class ParkingSpaceAllocator {

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>(Arrays.asList(
                new Car("CAR1", 18),
                new Car("CAR2", 24),
                new Car("SUV1", 16),
                new Car("TRUCK", 28),
                new Car("VAN", 21)
        ));

        List<Parking> parkingList = new ArrayList<>(Arrays.asList(
                new Parking("A1", 20),
                new Parking("A2", 25),
                new Parking("A3", 18),
                new Parking("B1", 30),
                new Parking("B2", 22)
        ));

        parkCars(cars, parkingList);
    }

    public static Map<String, String> parkCars(List<Car> cars, List<Parking> parkingList) {
        cars.sort(Comparator.comparing(Car::getSize));
        parkingList.sort(Comparator.comparing(Parking::getSize));
        // Parking id -> Car id
        Map<String, String> parkingAllocation = new HashMap<String, String>();

        // for each car, find the minimum parking lot not occupied that can be used
        int wastedSpace = 0;
        for (Car car : cars) {
            wastedSpace += findMinimumSpace(parkingList, car, parkingAllocation);
        }

        System.out.println(parkingAllocation.size() + " vehicles parked");
        System.out.println("Total wasted space: " + wastedSpace);
        System.out.print("Assignments:");
        outputAssignments(parkingAllocation);

        return parkingAllocation;

    }

    private static int findMinimumSpace(List<Parking> parkingList, Car car, Map<String, String> allocation) {
        for (Parking parking : parkingList) {
            if (allocation.containsKey(parking.getId())) continue;
            if(car.getSize() > parking.getSize()) continue;
            allocation.put(parking.getId(), car.getId());
            return parking.getSize() - car.getSize();
        }
        return 0;
    }

    private static void outputAssignments(Map<String, String> allocation) {
        Map<String, String> sortedAllocation = allocation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        sortedAllocation.forEach((key, value) -> {
            System.out.print(value + "->" + key + " ");
        });
    }
}


class Car {
    public String id;
    public int size;
    public Car(String id, int size) {
        this.id = id;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}

class Parking {
    public String id;
    public int size;
    public Parking(String id, int size) {
        this.id = id;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}