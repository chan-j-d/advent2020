import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day23_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String cupsString = scanner.nextLine();
        Cups cups = new Cups();
        for (int i = 0; i < cupsString.length(); i++) {
            cups.addCup(Integer.parseInt(cupsString.charAt(i) + ""));
        }

        for (int i = 10; i <= 100; i++) {
            cups.addCup(i);
        }

        Set<List<Integer>> visitedSets = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            cups.simulateTurn();
            System.out.println(cups);
            if (i % 1000 == 0) {
                System.out.println(i);
            }

            if (visitedSets.contains(cups.cups)) {
                System.out.println(cups.cups);
                break;
            } else {
                visitedSets.add(cups.cups);
            }
        }

        System.out.println(getTwoBesideOne(cups.cups));

    }

    static List<Integer> getTwoBesideOne(List<Integer> list) {
        int index = list.indexOf(1);
        int firstIndex = index + 1 >= list.size() ? 0 : index + 1;
        int secondIndex = firstIndex + 1 >= list.size() ? 0 : firstIndex + 1;
        return List.of(list.get(firstIndex), list.get(secondIndex));
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
                    if (cups.contains(1000000)) {
                        referenceValue = 1000000;
                    } else if (cups.contains(999999)) {
                        referenceValue = 999999;
                    } else if (cups.contains(999998)) {
                        referenceValue = 999998;
                    } else {
                        referenceValue = 999997;
                    }
                }
            } while (!cups.contains(referenceValue));
            System.out.println(referenceValue);

            int indexOfRefValue = cups.indexOf(referenceValue);
            if (indexOfRefValue == cups.size() - 1) {
                indexOfRefValue = -1;
            }
            for (int i = 1; i <= 3; i++) {
                cups.add(indexOfRefValue + i, threeValues.poll());
                if (indexOfRefValue < markedCupIndex) {
                    markedCupIndex++;
                }
            }

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
