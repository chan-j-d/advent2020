import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day6_1 {

    private static final String LINE_SEPARATOR = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[\n]");

        int sum = 0;
        GroupAnswers currentGroupAnswers = new GroupAnswers();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals(LINE_SEPARATOR)) {
                sum = sum + currentGroupAnswers.getCount();
                currentGroupAnswers = new GroupAnswers();
            }

            currentGroupAnswers.addAnswers(nextLine);
        }

        sum = sum + currentGroupAnswers.getCount();

        System.out.println(sum);
    }

    static class GroupAnswers {

        Set<Character> setOfAnswers;
        int numPersonsInGroup;

        GroupAnswers() {
            setOfAnswers = new HashSet<>();
            numPersonsInGroup = 0;
        }

        int getCount() {
            return setOfAnswers.size();
        }

        int getNumPersonsInGroup() {
            return numPersonsInGroup;
        }

        void addAnswers(String answers) {
            for (int i = 0; i < answers.length(); i++) {
                setOfAnswers.add(answers.charAt(i));
            }
            numPersonsInGroup++;
        }

    }


}

