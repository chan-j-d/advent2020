import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day23_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String cupsString = scanner.nextLine();
        Cups cups = new Cups();
        for (int i = 0; i < cupsString.length(); i++) {
            cups.addCup(Integer.parseInt(cupsString.charAt(i) + ""));
        }

        for (int i = 0; i < 100; i++) {
            cups.simulateTurn();
        }

    }

    static class Cups {

        List<Integer> cups;
        int markedCupIndex;

        Cups() {
            cups = new ArrayList<>();
            markedCupIndex = 0;
        }

        void addCup(int cup) {
            cups.add(cup);
        }

        void simulateTurn() {
            Queue<Integer> threeValues = new LinkedList<>();
            int referenceValue = cups.get(markedCupIndex);
            int indexToRemove = markedCupIndex + 1;
            for (int i = 0; i < 3; i++) {
                if (indexToRemove >= cups.size()) {
                    indexToRemove = 0;
                }
                threeValues.add(cups.remove(indexToRemove));
            }

            do {
                referenceValue = referenceValue - 1;
                if (referenceValue == -1) {
                    referenceValue = cups.stream()
                            .max(Comparator.naturalOrder()).get();
                }
                System.out.println(referenceValue);
            } while (!cups.contains(referenceValue));

            int indexOfRefValue = cups.indexOf(referenceValue);
            System.out.println("extracted numbers" + threeValues);
            for (int i = 1; i <= 3; i++) {
                cups.add(indexOfRefValue + i, threeValues.poll());
                if (indexOfRefValue < markedCupIndex) {
                    markedCupIndex++;
                }
            }
            System.out.println(cups);

            markedCupIndex = markedCupIndex + 1;
            if (markedCupIndex >= cups.size()) {
                markedCupIndex = 0;
            }

        }

        public String toString() {
            return this.cups.toString();
        }

        List<Integer> getCups() {
            return cups;
        }




    }
}
