import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day6_2 {

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
                continue;
            }

            currentGroupAnswers.addAnswers(nextLine);
        }

        sum = sum + currentGroupAnswers.getCount();

        System.out.println(sum);
    }

    static class GroupAnswers {

        List<Set<Character>> listOfPersonAnswers;
        int numPersonsInGroup;

        GroupAnswers() {
            listOfPersonAnswers = new ArrayList<>();
            numPersonsInGroup = 0;
        }

        int getCount() {
            System.out.println(listOfPersonAnswers);
            return (int) listOfPersonAnswers.get(0)
                    .stream()
                    .filter(c -> listOfPersonAnswers.stream()
                            .allMatch(set -> set.contains(c)))
                    .count();
        }

        int getNumPersonsInGroup() {
            return numPersonsInGroup;
        }

        void addAnswers(String answers) {
            Set<Character> newPersonAnswers = new HashSet<>();
            for (int i = 0; i < answers.length(); i++) {
                newPersonAnswers.add(answers.charAt(i));
            }
            listOfPersonAnswers.add(newPersonAnswers);

            numPersonsInGroup++;
        }

    }


}

