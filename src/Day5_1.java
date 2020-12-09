import java.util.Scanner;

public class Day5_1 {

    private static final char ONE_IDENTIFIER_ROW = 'B';
    private static final char ONE_IDENTIFIER_COLUMN = 'R';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int currentHighestSeatId = -1;
        while (scanner.hasNext()) {
            SeatInput nextSeatInput = new SeatInput(scanner.nextLine());
            int nextSeatId = nextSeatInput.getSeatId();
            if (nextSeatId > currentHighestSeatId) {
                currentHighestSeatId = nextSeatId;
            }
        }

        System.out.println(currentHighestSeatId);
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
