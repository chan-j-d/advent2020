import java.util.Scanner;

public class Day2_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        while (scanner.hasNext()) {
            String lineInput = scanner.nextLine();
            if (isValid(lineInput)) {
                count++;
            }
        }
        System.out.println(count);
    }

    static boolean isValid(String lineInput) {
        String[] details = lineInput.split(": ");
        String password = details[1];
        String[] requirements = details[0].split(" ");
        char letterConstraint = requirements[1].charAt(0);
        String[] valueConstraints = requirements[0].split("-");
        int lowerBound = Integer.parseInt(valueConstraints[0]);
        int upperBound = Integer.parseInt(valueConstraints[1]);

        int letterConstraintCount = (int) password.chars()
                .filter(c -> c == letterConstraint)
                .count();
        return letterConstraintCount <= upperBound && letterConstraintCount >= lowerBound;
    }

}
