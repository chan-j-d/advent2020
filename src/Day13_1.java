import java.util.Scanner;

public class Day13_1 {

    private static final String NOT_AVAILBLE = "x";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int time = Integer.parseInt(scanner.nextLine());
        scanner.useDelimiter("[,]");
        int minimum = time;
        int id = -1;
        while (scanner.hasNext()) {
            String nextValue = scanner.next();
            if (nextValue.equals(NOT_AVAILBLE)) {
                continue;
            }

            int value = Integer.parseInt(nextValue.trim());

            int newMinimum = getRemainder(time, value);

            System.out.println(value + ": " + newMinimum);

            if (newMinimum < minimum) {
                minimum = newMinimum;
                id = value;
            }
        }

        System.out.println(minimum * id);


    }

    static int getRemainder(int threshold, int value) {
        return value - threshold % value;
    }




}
