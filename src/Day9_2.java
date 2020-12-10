import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SumTracker tracker = new SumTracker();

        while (scanner.hasNext()) {
            int nextValue = scanner.nextInt();
            tracker.add(nextValue);

            boolean isSumFound = tracker.isEqualToSum();
            if (isSumFound) {
                List<Integer> intSumList = tracker.getIntList();
                System.out.println(intSumList);
                System.out.println(intSumList.stream().min((x, y) -> x - y).get()
                        + intSumList.stream().max((x, y) -> x - y).get());
                return;
            }
        }
    }

    static class SumTracker {

        int SUM_TOTAL = 32321523;

        List<Integer> intList;
        int total;

        SumTracker() {
            intList = new ArrayList<>();
            total = 0;
        }

        boolean isEqualToSum() {
            return total == SUM_TOTAL;
        }

        void add(int number) {
            total = total + number;
            intList.add(number);

            while (total > SUM_TOTAL) {
                int firstValue = intList.remove(0);
                total = total - firstValue;
            }
        }

        List<Integer> getIntList() {
            return intList;
        }

    }

}
