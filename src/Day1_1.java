import java.util.HashSet;
        import java.util.Scanner;

public class Day1_1 {

    private static final int FIX_SUM = 2020;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashSet<Integer> setOfExpenses = new HashSet<>();
        while (scanner.hasNext()) {
            int readInt = scanner.nextInt();
            setOfExpenses.add(readInt);
            int requiredInt = FIX_SUM - readInt;
            if (setOfExpenses.contains(requiredInt)) {
                System.out.println(requiredInt * readInt);
                return;
            }
        }
    }

}
