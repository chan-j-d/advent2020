import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day10_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> joltages = scanner.tokens()
                .map(x -> Integer.parseInt(x))
                .collect(Collectors.toList());

        joltages.add(0);
        joltages.sort((x, y) -> x - y);
        joltages.add(joltages.get(joltages.size() - 1) + 3);

        System.out.println(joltages);

        System.out.println(getAnswer(joltages));
    }


    static int getAnswer(List<Integer> list) {
        int diff;

        int oneDiffCount = 0;
        int threeDiffCount = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            diff = list.get(i + 1) - list.get(i);
            if (diff == 1) {
                oneDiffCount++;
            } else if (diff == 3) {
                threeDiffCount++;
            }
        }
         return oneDiffCount * threeDiffCount;
    }



}
