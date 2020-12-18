import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day15_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[,\n\r]");
        Simulator simulator = new Simulator(scanner.tokens()
                .filter(x -> x.matches("[0-9]+"))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray());

        System.out.println(simulator.simulate(30000000));
    }


    static class Simulator {

        int count;
        Map<Integer, Integer> numberTurnMap;
        int prevNumber;

        Simulator(int... startingNumbers) {
            numberTurnMap = new HashMap<>();
            count = 1;
            for (int i = 0; i < startingNumbers.length - 1; i++) {
                numberTurnMap.put(startingNumbers[i], count++);
            }
            prevNumber = startingNumbers[startingNumbers.length - 1];
        }

        int generateNextNumber() {
            if (!numberTurnMap.containsKey(prevNumber)) {
                numberTurnMap.put(prevNumber, count++);
                prevNumber = 0;
            } else {
                int turnDiff = count - numberTurnMap.get(prevNumber);
                numberTurnMap.put(prevNumber, count++);
                prevNumber = turnDiff;
            }

            return prevNumber;
        }

        int simulate(int countLimit) {
            while (count < countLimit - 1) {
                generateNextNumber();
            }
            return generateNextNumber();
        }

    }
}
