import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13_2 {

    private static final String NOT_AVAILBLE = "x";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[,]");

        List<IntPair> idIndexMaps = new ArrayList<>();
        int index = 0;
        while (scanner.hasNext()) {
            String nextValue = scanner.next();
            if (nextValue.equals(NOT_AVAILBLE)) {
                index++;
                continue;
            }

            int value = Integer.parseInt(nextValue.trim());
            idIndexMaps.add(new IntPair(value, index++));

        }

        BigInteger multiplier = BigInteger.ONE;
        while (true) {

            BigInteger startingTValue = idIndexMaps.get(0).first.multiply(multiplier);
            multiplier = multiplier.add(BigInteger.ONE);
            boolean isNotFound = false;
            for (IntPair pair : idIndexMaps) {
                BigInteger id = pair.first;
                BigInteger listIndex = pair.second;

                System.out.println(id + ": " + listIndex);
                if (!(startingTValue.add(listIndex)).mod(id).equals(BigInteger.ZERO)) {
                    isNotFound = true;
                    break;
                }
            }

            if (isNotFound) {
                continue;
            }


            System.out.println(startingTValue);
            return;
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
