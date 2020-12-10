import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day10_2 {

    static Map<Integer, Long> startIndexSolutions = new HashMap<>();

    static List<Integer> intList;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> joltages = scanner.tokens()
                .map(x -> Integer.parseInt(x))
                .collect(Collectors.toList());

        joltages.add(0);
        joltages.sort((x, y) -> x - y);
        joltages.add(joltages.get(joltages.size() - 1) + 3);
        intList = joltages;

        System.out.println(getNumSolutions(0));
        System.out.println(intList);
        System.out.println(startIndexSolutions);
    }

    static long getNumSolutions(int startingIndex) {
        if (startIndexSolutions.containsKey(startingIndex)) {
            return startIndexSolutions.get(startingIndex);
        } else if (startingIndex == intList.size() - 1) {
            return 1;
        } else if (startingIndex == -1) {
            return 0;
        }

        int firstValue = intList.get(startingIndex);

        long solutionForThisIndex = intList.stream()
                .filter(x -> x > firstValue)
                .filter(x -> x <= firstValue + 3)
                //.peek(x -> System.out.println("value included: " + x))
                .map(x -> getNumSolutions(intList.indexOf(x)))
                //.peek(x -> System.out.println("solution count: " + x))
                .reduce(0l, (x, y) -> x + y);

        startIndexSolutions.put(startingIndex, solutionForThisIndex);
        return solutionForThisIndex;

    }

}
