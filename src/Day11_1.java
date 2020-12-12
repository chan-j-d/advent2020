import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11_1 {

    static char SQUARE_FLOOR = '.';
    static char SQUARE_OCCUPIED = '#';
    static char SQUARE_EMPTY = 'L';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        FerrySeats seats = new FerrySeats();
        while (scanner.hasNext()) {
            seats.addRow(scanner.nextLine().chars().mapToObj(x -> (char) x)
                    .collect(Collectors.toList()));
        }

        boolean hasChanged;
        do {
            hasChanged = false;
            for (int i = 0; i < seats.getNumRows(); i++) {
                for (int j = 0; j < seats.getNumColumns(); j++) {
                    boolean isToBeFlipped = seats.toFlip(i, j);
                    if (isToBeFlipped) {
                        hasChanged = true;
                        seats.markTileToBeFlipped(i, j);
                    }
                }
            }
            seats.processFlipping();
        } while (hasChanged);

        int seatCount = seats.countOccupiedSeats();
        System.out.println(seatCount);
    }

    static class FerrySeats {

        List<List<Character>> seats;
        List<List<Boolean>> seatsToBeFlipped;
        int currentRow;

        FerrySeats() {
            seats = new ArrayList<>();
            seatsToBeFlipped = new ArrayList<>();
            currentRow = 0;
        }

        void addRow(List<Character> listChars) {
            seats.add(new ArrayList<>(listChars));
            seatsToBeFlipped.add(new ArrayList<>(Stream.generate(() -> false)
                    .limit(getNumColumns())
                    .collect(Collectors.toList())));
        }

        int getNumRows() {
            return seats.size();
        }

        int getNumColumns() {
            return seats.get(0).size();
        }

        char getStateAt(int row, int column) {
            return seats.get(row).get(column);
        }

        boolean toFlip(int row, int column) {
            char seatState = getStateAt(row, column);
            if (seatState == SQUARE_EMPTY) {
                return getAdjacentCount(row, column) == 0;
            } else if (seatState == SQUARE_FLOOR) {
                return false;
            } else if (seatState == SQUARE_OCCUPIED) {
                return getAdjacentCount(row, column) >= 5;
            } else {
                throw new IllegalArgumentException("Not a valid state");
            }
        }

        int getAdjacentCount(int row, int column) {
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if (isAdjacentDirectionOccupied(row, column, i, j)) {
                        count++;
                    }
                }
            }
            return count;
        }

        boolean isAdjacentDirectionOccupied(int row, int column, int rowChange, int columnChange) {
            int i = row, j = column;
            while (true) {
                i = i + rowChange;
                j = j + columnChange;
                if (isOutOfBounds(i, j)) {
                    return false;
                }

                char state = getStateAt(i, j);
                if (state == SQUARE_OCCUPIED) {
                    return true;
                } else if (state == SQUARE_EMPTY) {
                    return false;
                } else if (state == SQUARE_FLOOR) {
                    continue;
                } else {
                    throw new IllegalArgumentException("illegal state");
                }
            }
        }

        boolean isOutOfBounds(int row, int column) {
            return (row < 0 || row >= getNumRows()) || (column < 0 || column >= getNumColumns());
        }

        void markTileToBeFlipped(int row, int column) {
            seatsToBeFlipped.get(row).set(column, true);
        }

        void flipTile(int row, int column) {
            char state = getStateAt(row, column);
            if (state == SQUARE_EMPTY) {
                seats.get(row).set(column, SQUARE_OCCUPIED);
            } else if (state == SQUARE_OCCUPIED) {
                seats.get(row).set(column, SQUARE_EMPTY);
            } else {
                throw new IllegalArgumentException("Not a valid state");
            }
        }

        void processFlipping() {
            for (int i = 0; i < getNumRows(); i++) {
                for (int j = 0; j < getNumColumns(); j++) {
                    if (seatsToBeFlipped.get(i).get(j)) {
                        seatsToBeFlipped.get(i).set(j, false);
                        flipTile(i, j);
                    }
                }
            }
        }

        int countOccupiedSeats() {
            int count = 0;
            for (int i = 0; i < getNumRows(); i++) {
                for (int j = 0; j < getNumColumns(); j++) {
                    if (getStateAt(i, j) == SQUARE_OCCUPIED) {
                        count++;
                    }
                }
            }
            return count;
        }

        @Override
        public String toString() {
            return seats.stream().reduce("", (x, y) -> x + "\n" + y, (x, y) -> x + y);
        }

    }

}
