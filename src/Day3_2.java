import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3_2 {

    private static final char TREE = '#';
    private static final char OPEN = '.';

    private static final List<IntPair> INT_PAIR_LIST =
            List.of(new IntPair(1, 1), new IntPair(3, 1),
                    new IntPair(5, 1), new IntPair(7, 1),
                    new IntPair(1, 2));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TreeMap map = initialise(scanner);

        int currentCount = 1;
        for (IntPair pair : INT_PAIR_LIST) {
            currentCount = currentCount * getTreeCountForPath(map, pair.first, pair.second);
        }
        System.out.println(currentCount);
    }

    static int getTreeCountForPath(TreeMap map, int rightStepSize, int downStepSize) {
        int numRows = map.getNumRows();
        int numColumns = map.getNumColumns();
        int row = 0;
        int column = 0;
        int count = 0;
        while (row != numRows - 1) {
            row = row + downStepSize;
            column = column + rightStepSize;
            if (column >= numColumns) {
                column = column - numColumns;
            }
            if (map.isTreeAt(row, column)) {
                count++;
            }
        }
        return count;
    }

    static TreeMap initialise(Scanner scanner) {
        TreeMap map = new TreeMap();
        while (scanner.hasNext()) {
            String newRow = scanner.nextLine();
            map.addRow(newRow);
        }
        return map;
    }

    static class TreeMap {
        List<List<Character>> treeMap;
        int currentLine;

        TreeMap() {
            treeMap = new ArrayList<>();
            currentLine = 0;
        }

        void addRow(String treeRow) {
            treeMap.add(new ArrayList<>());
            for (int i = 0; i < treeRow.length(); i++) {
                treeMap.get(currentLine).add(treeRow.charAt(i));
            }
            currentLine++;
        }

        char getSymbol(int row, int column) {
            return treeMap.get(row).get(column);
        }

        boolean isTreeAt(int row, int column) {
            return getSymbol(row, column) == TREE;
        }

        int getNumRows() {
            return treeMap.size();
        }

        int getNumColumns() {
            return treeMap.get(0).size();
        }

    }

    static class IntPair {
        int first;
        int second;

        IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }


}
