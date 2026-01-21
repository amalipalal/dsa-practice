package org.example.revise;

import java.util.*;
import java.util.stream.Collectors;

public class ParkingSpaceAllocator {

    public static void main(String[] args) {
        List<Parking> parkingList = formatParkingInput("P1 15 P2 20 P3 25");
        List<Car> cars = formatCarInput("V1 22 V2 18 V3 14 V4 12");

        parkCars(cars, parkingList);
    }

    public static List<Car> formatCarInput(String carInput) {
        String[] input = carInput.split("\\s");
        List<Car> output = new ArrayList<>();
        for (int idPosition = 0; idPosition < input.length - 1; idPosition+=2) {
            output.add(new Car(input[idPosition], Integer.parseInt(input[idPosition + 1])));
        }
        return output;
    }

    public static List<Parking> formatParkingInput(String parkingInput) {
        String[] input = parkingInput.split("\\s");
        List<Parking> output = new ArrayList<>();
        for (int idPosition = 0; idPosition < input.length - 1; idPosition+=2) {
            output.add(new Parking(input[idPosition], Integer.parseInt(input[idPosition + 1])));
        }
        return output;
    }

    public static Map<String, String> parkCars(List<Car> cars, List<Parking> parkingList) {
        cars.sort(Comparator.comparing(Car::size).reversed());
        parkingList.sort(Comparator.comparing(Parking::size));
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
            if (allocation.containsKey(parking.id())) continue;
            if(car.size() > parking.size()) continue;
            allocation.put(parking.id(), car.id());
            return parking.size() - car.size();
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

record Car(
        String id,
        int size
) {}

record Parking(
        String id,
        int size
) {}