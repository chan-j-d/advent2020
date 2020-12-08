import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Day1_2 {

    private static final int FINAL_SUM = 2020;

    public static void main(String[] args) {
        HashSet<Integer> readIntegers = new HashSet<>();
        HashMap<Integer, Integer> intermediateSum = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int readInt = scanner.nextInt();
            int requiredIntermediateSum = FINAL_SUM - readInt;
            if (intermediateSum.containsKey(requiredIntermediateSum)) {
                System.out.println(intermediateSum.get(requiredIntermediateSum) * readInt);
                return;
            }
            readIntegers.stream()
                    .forEach(x -> intermediateSum.put(x + readInt, x * readInt));
            readIntegers.add(readInt);

        }
    }


}
