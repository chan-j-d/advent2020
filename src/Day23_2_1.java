import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day23_2_1 {

    static int MAX_SIZE = 1000000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String cupsString = scanner.nextLine();
        Cups cups = new Cups();
        for (int i = 0; i < cupsString.length(); i++) {
            cups.addCup(Integer.parseInt(cupsString.charAt(i) + ""));
        }

        for (int i = 10; i <= MAX_SIZE; i++) {
            cups.addCup(i);
        }
        cups.registerLastCup();

        for (int i = 0; i < MAX_SIZE * 10; i++) {
            cups.simulateTurn();
            if (i % 1000 == 0) {
                System.out.println(i);
            }

        }

        System.out.println(cups.getAnswer());


    }

    static List<Integer> getTwoBesideOne(List<Integer> list) {
        int index = list.indexOf(1);
        int firstIndex = index + 1 >= list.size() ? 0 : index + 1;
        int secondIndex = firstIndex + 1 >= list.size() ? 0 : firstIndex + 1;
        return List.of(list.get(firstIndex), list.get(secondIndex));
    }

    static class Cups {

        Map<Integer, Cup> cups;
        Cup startingCup;
        Cup previousCup;

        Cups() {
            cups = new HashMap<>();
            startingCup = null;
            previousCup = null;
        }

        void addCup(int cup) {
            if (startingCup == null) {
                startingCup = new Cup(cup);
                previousCup = startingCup;
                cups.put(cup, startingCup);
            } else {
                Cup tempCup = new Cup(cup);
                cups.put(cup, tempCup);
                connectToRightOf(previousCup, tempCup);
                previousCup = tempCup;
            }
        }

        void registerLastCup() {
            connectToRightOf(previousCup, startingCup);
        }

        void simulateTurn() {
            Cup firstCupToRemove = startingCup.right;
            Cup lastCupToRemove = firstCupToRemove.right.right;
            Cup firstCupToReconnect = lastCupToRemove.right;

            List<Integer> listOfRemovedIds = List.of(firstCupToRemove.id, firstCupToRemove.right.id,
                    lastCupToRemove.id);
            int idRef = startingCup.id;
            do {
                idRef--;
                if (idRef == 0) {
                    idRef = MAX_SIZE;
                }
            } while (listOfRemovedIds.contains(idRef));
            Cup refCup = cups.get(idRef);
            Cup refEndCup = refCup.right;
            connectToRightOf(refCup, firstCupToRemove);
            connectToRightOf(lastCupToRemove, refEndCup);
            connectToRightOf(startingCup, firstCupToReconnect);

            startingCup = startingCup.right;
        }

        static void connectToRightOf(Cup cup1, Cup cup2) {
            cup1.right = cup2;
            cup2.left = cup1;
        }

        long getAnswer() {
            Cup oneCup = cups.get(1);
            return (long) oneCup.right.id * (long) oneCup.right.right.id;
        }


    }

    static class Cup {

        int id;
        Cup left;
        Cup right;

        Cup(int id) {
            this.id = id;
        }

    }
}
