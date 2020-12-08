import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Day3_1 {

    private static final char TREE = '#';
    private static final char OPEN = '.';

    private static final int RIGHT_STEP_SIZE = 3;
    private static final int DOWN_STEP_SIZE = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TreeMap map = initialise(scanner);
        int numRows = map.getNumRows();
        int numColumns = map.getNumColumns();
        int row = 0;
        int column = 0;

        int count = 0;
        while (row != numRows - 1) {
            row = row + DOWN_STEP_SIZE;
            column = column + RIGHT_STEP_SIZE;
            if (column >= numColumns) {
                column = column - numColumns;
            }
            if (map.isTreeAt(row, column)) {
                count++;
            }
        }
        System.out.println(count);
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


}
