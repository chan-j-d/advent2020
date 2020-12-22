import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day13_2 {

    private static final String NOT_AVAILBLE = "x";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[,]");

        List<IntPair> idIndexMaps = new ArrayList<>();

        int index = 0;
        boolean isFirst = true;
        int firstValue = -1;
        long sum = 0;
        long factor = 1;

        while (scanner.hasNext()) {
            String nextValue = scanner.next();
            if (nextValue.equals(NOT_AVAILBLE)) {
                index++;
                continue;
            }

            int value = Integer.parseInt(nextValue.trim());
            if (isFirst) {
                isFirst = false;
                firstValue = value;
                factor = firstValue;
                index++;
            } else {
                System.out.println("sum: " + sum + ", factor: " + factor + ", value: " + value + ", index: " + index);
                long multiplier = getMultiplierForLeftover(factor, value,  (sum % value) + index++);
                sum = sum + multiplier * factor;
                factor = factor * value;
            }
        }
        System.out.println(sum);

    }

    static long getMultiplierForLeftover(long multiplier, int id, long leftover) {
        long returnMultiplier = 1;
        long value = multiplier + leftover;
        while (true) {
            if (value == 0) {
                return returnMultiplier;
            }

            value = value + multiplier;
            returnMultiplier++;
            if (value >= id) {
                value = value - (value / id) * id;
            }
        }
    }

    static class IncrementChecker {

        int multiplier;

        Map<Integer, Map<Integer, Long>> idToMultiplierMap;

        IncrementChecker(int multiplier) {
            this.multiplier = multiplier;
            idToMultiplierMap = new HashMap<>();
        }

        long getMultiplier(int id, int leftover) {
            if (idToMultiplierMap.containsKey(id)) {
                Map<Integer, Long> leftoverToMultiplierMap = idToMultiplierMap.get(id);
                if (leftoverToMultiplierMap.containsKey(leftover)) {
                    return leftoverToMultiplierMap.get(multiplier);
                }
            }
            return 0L;
        }

        long getMultiplierForSpecificLeftover(int id, int leftover) {
            Map<Integer, Integer> leftoverToCountMap = new HashMap<>();
            if (!idToMultiplierMap.containsKey(id)) {
                idToMultiplierMap.put(id, new HashMap<>());
            }
            Map<Integer, Long> leftoverToMultiplierMap = idToMultiplierMap.get(id);
            return 0L;
        }

    }

    static class IntPair {
        BigInteger first;
        BigInteger second;

        IntPair(int first, int second) {
            this.first = BigInteger.valueOf(first);
            this.second = BigInteger.valueOf(second);
        }
    }

}
