import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day20_1 {

    static final char PIXEL_ONE = '#';
    static final char PIXEL_ZERO = '.';

    static List<Tile> tiles = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n\r\n");
        while (scanner.hasNext()) {
            String nextLine = scanner.next();
            String[] details = nextLine.split("\n", 2);
            int tileNumber = Integer.parseInt(details[0].split(" ")[1].split(":")[0]);
            tiles.add(new Tile(tileNumber, details[1]));
        }

        Map<Integer, Integer> edgeValueCountMap = new HashMap<>();
        tiles.stream()
                .map(tile -> tile.getEdgeValues())
                .forEach(list -> list.stream()
                        .forEach(x -> {
                            if (edgeValueCountMap.containsKey(x)) {
                                edgeValueCountMap.put(x, edgeValueCountMap.get(x) + 1);
                            } else {
                                edgeValueCountMap.put(x, 1);
                            }
                        }));
        System.out.println(edgeValueCountMap);

        Map<Integer, Integer> idToCountMap = new HashMap<>();
        tiles.stream()
                .forEach(tile -> {
                    idToCountMap.put(tile.id, (int) tile.getEdgeValues().stream()
                            .filter(x -> edgeValueCountMap.get(x) == 1)
                            .count());
                });

        System.out.println(idToCountMap.entrySet().stream()
                .filter(set -> set.getValue() == 2)
                .collect(Collectors.toList()));

        System.out.println(3083L * 1709L * 1499L * 1789L);


    }

    static class Tile {

        int id;
        boolean[][] layout;
        List<Integer> edgeValues1;
        List<Integer> edgeValues2;

        Tile(int id, boolean[][] layout) {
            this.id = id;
            this.layout = layout;
            edgeValues1 = new ArrayList<>();
            edgeValues2 = new ArrayList<>();
            init();
        }

        Tile(int id, String layoutString) {
            this(id, processLayoutString(layoutString));
        }

        static boolean[][] processLayoutString(String layoutString) {
            boolean[][] finalLayout = new boolean[10][];
            Scanner scanner = new Scanner(layoutString);
            for (int i = 0; i < 10; i++) {
                String nextLine = scanner.nextLine();
                finalLayout[i] = new boolean[10];
                for (int j = 0; j < 10; j++) {
                    finalLayout[i][j] = nextLine.charAt(j) == PIXEL_ONE;
                }
            }
            scanner.close();
            return finalLayout;
        }

        void init() {
            boolean[] pair1Row1 = layout[0];
            boolean[] pair1Row2 = layout[9];
            boolean[] pair2Row1 = new boolean[10];
            boolean[] pair2Row2 = new boolean[10];
            for (int i = 0; i < 10; i++) {
                pair2Row1[i] = layout[i][0];
                pair2Row2[i] = layout[i][9];
            }
            edgeValues1 = List.of(getBinaryValueOf(pair1Row1), getBinaryValueOf(pair1Row2));
            edgeValues2 = List.of(getBinaryValueOf(pair2Row1), getBinaryValueOf(pair2Row2));
        }

        static int getBinaryValueOf(boolean[] rowLayout) {
            int left = 0;
            int right = 0;
            for (int i = 0; i < rowLayout.length; i++) {
                left = (left << 1) + (rowLayout[i] ? 1 : 0);
                right = (right << 1) + (rowLayout[rowLayout.length - i - 1] ? 1 : 0);
            }

            return Math.min(left, right);
        }

        List<Integer> getEdgeValues() {
            return List.of(edgeValues1.get(0), edgeValues1.get(1),
                    edgeValues2.get(0), edgeValues2.get(1));
        }

        @Override
        public String toString() {
            return "Tile Number: " + id
                    + Arrays.stream(layout)
                            .map(array -> Arrays.toString(array))
                            .reduce("", (s1, s2) -> s1 + "\n" + s2)
                    + "\nEdge values: "
                    + edgeValues1 + " " + edgeValues2;
        }



    }

}
