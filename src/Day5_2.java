import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5_2 {

    private static final char ONE_IDENTIFIER_ROW = 'B';
    private static final char ONE_IDENTIFIER_COLUMN = 'R';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Set<Integer> integerSet = Stream.iterate(0, x -> x + 1)
                .limit(992l)
                .collect(Collectors.toSet());

        while (scanner.hasNext()) {
            SeatInput nextSeatInput = new SeatInput(scanner.nextLine());
            int nextSeatId = nextSeatInput.getSeatId();
            integerSet.remove(nextSeatId);
        }

        System.out.println(integerSet);
    }


    static class SeatInput {

        private static final int NUM_IDENTIFIER_ROW = 7;
        private static final int NUM_IDENTIFIER_COLUMN = 3;

        String rowIdentifier;
        String columnIdentifier;

        SeatInput(String input) {
            input = input.trim();
            rowIdentifier = input.substring(0, NUM_IDENTIFIER_ROW);
            columnIdentifier = input.substring(NUM_IDENTIFIER_ROW);
        }

        int getRow() {
            return binaryCalculator(rowIdentifier, ONE_IDENTIFIER_ROW);
        }

        int getColumn() {
            return binaryCalculator(columnIdentifier, ONE_IDENTIFIER_COLUMN);
        }

        int getSeatId() {
            System.out.println(rowIdentifier + columnIdentifier);
            System.out.println("row: " + getRow() + ", column: " + getColumn());
            return getRow() * 8 + getColumn();
        }

        int binaryCalculator(String binaryString, char oneRep) {
            int binaryStringLength = binaryString.length();
            int sum = 0;
            int powerCount = 0;
            for (int i = binaryStringLength - 1; i >= 0; i--) {
                boolean isCharOne = binaryString.charAt(i) == oneRep;
                sum = sum + (isCharOne ? (int) Math.pow(2, powerCount) : 0);
                powerCount++;
            }

            return sum;
        }


    }


}
