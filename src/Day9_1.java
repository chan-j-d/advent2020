import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> first25Int = Stream.iterate(0, x -> x < 25, x -> x + 1)
                .map(x -> scanner.nextInt())
                .collect(Collectors.toList());

        Tracker tracker = new Tracker(first25Int);

        while (scanner.hasNext()) {
            int nextValue = scanner.nextInt();

            if (!tracker.isValidNextNumber(nextValue)) {
                System.out.println(nextValue);
                return;
            }

            tracker.addNumber(nextValue);
        }
    }

    static class Tracker {

        Set<Integer> availableSums;
        Map<Integer, Set<Integer>> valueToSumMap;
        List<Integer> valueOrder;

        Tracker(List<Integer> first25Numbers) {
            availableSums = new HashSet<>();
            valueToSumMap = new HashMap<>();
            valueOrder = new ArrayList<>(first25Numbers);

            Stream.iterate(0, x -> x < 25, x -> x + 1)
                    .forEach(x -> {
                        Set<Integer> integerSet = Stream.iterate(x + 1, y -> y < 25, y -> y + 1)
                                .map(y -> valueOrder.get(x) + valueOrder.get(y))
                                .peek(y -> availableSums.add(y))
                                .collect(Collectors.toSet());
                        valueToSumMap.put(valueOrder.get(x), integerSet);
                    });
            System.out.println(availableSums);
            System.out.println(valueToSumMap);
        }

        boolean isValidNextNumber(int number) {
            return availableSums.contains(number);
        }

        void addNumber(int number) {
            int firstValue = valueOrder.remove(0);
            availableSums.remove(valueToSumMap.remove(firstValue));

            valueOrder.stream()
                    .forEach(x -> {
                        int sum = x + number;
                        availableSums.add(sum);
                        valueToSumMap.get(x).add(sum);});
            valueOrder.add(number);
            valueToSumMap.put(number, new HashSet<>());
        }
    }

}
