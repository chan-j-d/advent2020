import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day20_2 {

    static final int TOP_LEFT_CORNER_ID = 3083;

    static final char PIXEL_ONE = '#';
    static final char PIXEL_ZERO = '.';
    static final int DIMENSIONS = 12;

    static Map<Integer, Tile> tiles = new HashMap<>();
    static final List<IntPair> SEA_MONSTER = List.of(
            new IntPair(1, 0), new IntPair(2, 1), new IntPair(2, 4), new IntPair(1, 5), new IntPair(1, 6),
            new IntPair(2, 7), new IntPair(2, 10), new IntPair(1, 11), new IntPair(1, 12), new IntPair(2, 13),
            new IntPair(2, 16), new IntPair(1, 17), new IntPair(1, 18), new IntPair(1, 19), new IntPair(0, 18));


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n\r\n");
        while (scanner.hasNext()) {
            String nextLine = scanner.next();
            String[] details = nextLine.split("\n", 2);
            int tileNumber = Integer.parseInt(details[0].split(" ")[1].split(":")[0]);
            tiles.put(tileNumber, new Tile(tileNumber, details[1]));
        }

        Map<Integer, Integer> edgeValueToFirstTileMap = new HashMap<>();
        Map<Integer, IntPair> edgeValueToBothTilesMap = new HashMap<>();
        tiles.values().stream()
                .forEach(tile -> {
                    List<Integer> list = tile.getEdgeValues();
                    list.stream()
                            .forEach(x -> {
                                if (edgeValueToFirstTileMap.containsKey(x)) {
                                    edgeValueToBothTilesMap.put(x, new IntPair(tile.id, edgeValueToFirstTileMap.get(x)));
                                } else {
                                    edgeValueToFirstTileMap.put(x, tile.id);
                                }
                            });});

        Tile startingTile = tiles.get(TOP_LEFT_CORNER_ID);
        boolean isLeftConnected = edgeValueToBothTilesMap.containsKey(Tile.getBinaryValueOf(startingTile.getRightAlignColumn()));
        boolean isBottomConnected = edgeValueToBothTilesMap.containsKey(Tile.getBinaryValueOf(startingTile.getBottomAlignColumn()));
        while (!(isLeftConnected && isBottomConnected)) {
            startingTile.rotateLeft();
            isLeftConnected = edgeValueToBothTilesMap.containsKey(Tile.getBinaryValueOf(startingTile.getRightAlignColumn()));
            isBottomConnected = edgeValueToBothTilesMap.containsKey(Tile.getBinaryValueOf(startingTile.getBottomAlignColumn()));
        }
        startingTile.init();

        System.out.println(startingTile);
        for (int i = 0; i < DIMENSIONS - 1; i++) {
            boolean isFirstLeft = true;
            Tile nextStartingTile = null;
            for (int j = 0; j < DIMENSIONS - 1; j++) {
                Tile rightConnectedTile = alignRightTile(startingTile, edgeValueToBothTilesMap);
                Tile bottomConnectedTile = alignBottomTile(startingTile, edgeValueToBothTilesMap);

                startingTile = rightConnectedTile;
                if (isFirstLeft) {
                    nextStartingTile = bottomConnectedTile;
                    isFirstLeft = false;
                }
                System.out.println(startingTile);
            }
            startingTile = nextStartingTile;
            System.out.println(startingTile);
        }

        for (int i = 0; i < DIMENSIONS - 1; i++) {
            Tile rightConnectedTile = alignRightTile(startingTile, edgeValueToBothTilesMap);
            startingTile = rightConnectedTile;
        }

        //System.out.println(3083L * 1709L * 1499L * 1789L);

        Picture picture = new Picture(tiles.get(TOP_LEFT_CORNER_ID));

        int count = picture.getNumCountOfPattern(SEA_MONSTER);

        System.out.println(count);


    }

    static Tile alignRightTile(Tile tile, Map<Integer, IntPair> edgeValueToBothTilesMap) {
        int id = tile.id;
        boolean[] rightColumn = tile.getRightAlignColumn();
        int rightEdgeValue = Tile.getBinaryValueOf(rightColumn);
        int rightConnectedTileId = edgeValueToBothTilesMap.get(rightEdgeValue).getOther(id);
        Tile rightConnectedTile = tiles.get(rightConnectedTileId);
        tile.setRight(rightConnectedTile);
        rightConnectedTile.alignLeft(rightColumn);
        return rightConnectedTile;
    }

    static Tile alignBottomTile(Tile tile, Map<Integer, IntPair> edgeValueToBothTilesMap) {
        int id = tile.id;
        boolean[] bottomRow = tile.getBottomAlignColumn();
        int bottomEdgeValue = Tile.getBinaryValueOf(bottomRow);
        int bottomTileConnectedId = edgeValueToBothTilesMap.get(bottomEdgeValue).getOther(id);
        Tile bottomConnectedTile = tiles.get(bottomTileConnectedId);
        tile.setDown(bottomConnectedTile);
        bottomConnectedTile.alignTop(bottomRow);
        return bottomConnectedTile;
    }

    static class Tile {

        int id;
        boolean[][] layout;
        List<Integer> edgeValues1;
        List<Integer> edgeValues2;

        boolean[] topRow;
        boolean[] leftColumn;
        boolean[] rightColumn;
        boolean[] bottomRow;

        Tile left;
        Tile right;
        Tile up;
        Tile down;

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

        void setUp(Tile up) {
            this.up = up;
            up.down = this;
        }

        void setDown(Tile down) {
            this.down = down;
            down.up = this;
        }

        void setLeft(Tile left) {
            this.left = left;
            left.right = this;
        }

        void setRight(Tile right) {
            this.right = right;
            right.left = this;
        }

        boolean[] getRightAlignColumn() {
            boolean[] rightColumn = new boolean[10];
            for (int i = 0; i < 10; i++) {
                rightColumn[i] = layout[i][9];
            }
            return rightColumn;
        }

        boolean[] getBottomAlignColumn() {
            return layout[9];
        }

        void alignLeft(boolean[] alignColumn) {
            int valueToMatch = getBinaryValueOf(alignColumn);
            if (getBinaryValueOf(leftColumn) == valueToMatch) {
                adjustLeftMirror(alignColumn);
            } else if (getBinaryValueOf(topRow) == valueToMatch) {
                rotateLeft();
                init();
                adjustLeftMirror(alignColumn);
            } else if (getBinaryValueOf(bottomRow) == valueToMatch) {
                rotateRight();
                init();
                adjustLeftMirror(alignColumn);
            } else if (getBinaryValueOf(rightColumn) == valueToMatch) {
                rotateOpposite();
                init();
                adjustLeftMirror(alignColumn);
            }
            init();
        }

        void adjustLeftMirror(boolean[] alignColumn) {
            if (!Arrays.equals(alignColumn, leftColumn)) {
                mirrorHorizontal();
            }
        }

        void alignTop(boolean[] alignRow) {
            int valueToMatch = getBinaryValueOf(alignRow);
            if (getBinaryValueOf(leftColumn) == valueToMatch) {
                rotateRight();
                init();
                adjustTopMirror(alignRow);
            } else if (getBinaryValueOf(topRow) == valueToMatch) {
                adjustTopMirror(alignRow);
            } else if (getBinaryValueOf(bottomRow) == valueToMatch) {
                rotateOpposite();
                init();
                adjustTopMirror(alignRow);
            } else if (getBinaryValueOf(rightColumn) == valueToMatch) {
                rotateLeft();
                init();
                adjustTopMirror(alignRow);
            }

            init();
        }

        void adjustTopMirror(boolean[] alignRow) {
            if (!Arrays.equals(alignRow, topRow)) {
                mirrorVertical();
            }
        }

        void rotateLeft() {
            boolean[][] newFinalLayout = new boolean[10][];
            for (int i = 0; i < 10; i++) {
                newFinalLayout[i] = new boolean[10];
                for (int j = 0; j < 10; j++) {
                    newFinalLayout[i][j] = layout[j][9 - i];
                }
            }
            this.layout = newFinalLayout;
        }

        void rotateRight() {
            rotateLeft();
            rotateLeft();
            rotateLeft();
        }

        void rotateOpposite() {
            rotateLeft();
            rotateLeft();
        }

        void mirrorVertical() {
            boolean[][] newFinalLayout = new boolean[10][];
            for (int i = 0; i < 10; i++) {
                newFinalLayout[i] = new boolean[10];
                for (int j = 0; j < 10; j++) {
                    newFinalLayout[i][j] = layout[i][9 - j];
                }
            }
            this.layout = newFinalLayout;
        }

        void mirrorHorizontal() {
            rotateLeft();
            mirrorVertical();
            rotateRight();
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
            topRow = pair1Row1;
            boolean[] pair1Row2 = layout[9];
            bottomRow = pair1Row2;
            boolean[] pair2Row1 = new boolean[10];
            boolean[] pair2Row2 = new boolean[10];
            for (int i = 0; i < 10; i++) {
                pair2Row1[i] = layout[i][0];
                pair2Row2[i] = layout[i][9];
            }
            leftColumn = pair2Row1;
            rightColumn = pair2Row2;
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
                    .map(array -> {
                        return String.join("", Stream.iterate(0, x -> x < 10, x -> x + 1)
                                .map(x -> array[x] ? "#" : ".")
                                .toArray(String[]::new));
                    })
                    .reduce("", (s1, s2) -> s1 + "\n" + s2)
                    + "\nEdge values: "
                    + edgeValues1 + " " + edgeValues2;
        }



    }

    static class IntPair {
        int first;
        int second;

        IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        int getOther(int one) {
            return first == one ? second : first;
        }

        public String toString() {
            return first + ", " + second;
        }
    }

    static class Picture {

        static final int PICTURE_DIMENSION = 8 * DIMENSIONS;

        boolean[][] image;
        Tile startingTile;

        Picture(Tile startingTile) {
            this.startingTile = startingTile;
            image = new boolean[PICTURE_DIMENSION][];
            for (int i = 0; i < PICTURE_DIMENSION; i++) {
                image[i] = new boolean[PICTURE_DIMENSION];
            }
            init();
        }

        void init() {
            Tile leftmostTile = startingTile;
            for (int i = 0; i < DIMENSIONS; i++) {
                Tile nextTile = leftmostTile;
                for (int j = 0; j < DIMENSIONS; j++) {
                    boolean[][] tileLayout = nextTile.layout;
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            image[8 * i + k][8 * j + l] = tileLayout[k + 1][l + 1];
                        }
                    }
                    nextTile = nextTile.right;
                }
                leftmostTile = leftmostTile.down;
            }
        }

        void rotateLeft() {
            boolean[][] newImage = new boolean[PICTURE_DIMENSION][];
            for (int i = 0; i < PICTURE_DIMENSION; i++) {
                newImage[i] = new boolean[PICTURE_DIMENSION];
                for (int j = 0; j < PICTURE_DIMENSION; j++) {
                    newImage[i][j] = image[j][PICTURE_DIMENSION - 1 - i];
                }
            }
            image = newImage;
        }

        void flipVertical() {
            boolean[][] newImage = new boolean[PICTURE_DIMENSION][];
            for (int i = 0; i < PICTURE_DIMENSION; i++) {
                newImage[i] = new boolean[PICTURE_DIMENSION];
                for (int j = 0; j < PICTURE_DIMENSION; j++) {
                    newImage[i][j] = image[i][PICTURE_DIMENSION - 1 - j];
                }
            }
            image = newImage;
        }

        void printImage() {
            Arrays.stream(image)
                    .forEach(array -> {
                        System.out.println(String.join("", Stream.iterate(0, x -> x < PICTURE_DIMENSION, x -> x + 1)
                                .map(x -> array[x] ? "#" : ".")
                                .toArray(String[]::new)));
                    });
        }

        int getNumCountOfPattern(List<IntPair> intPairs) {
            int maxCount = 0;
            for (int i = 0; i < 4; i++) {
                printImage();
                int currentCount = getNumCountOfPatternInOrientation(intPairs);
                maxCount = Math.max(currentCount, maxCount);
                rotateLeft();
            }
            flipVertical();
            for (int i = 0; i < 4; i++) {
                printImage();
                int currentCount = getNumCountOfPatternInOrientation(intPairs);
                maxCount = Math.max(currentCount, maxCount);
                rotateLeft();
            }
            int totalCount = Arrays.stream(image)
                    .map(array -> {
                        int count = 0;
                        for (boolean b : array) {
                            if (b) {
                                count++;
                            }
                        }
                        return count;
                    })
                    .reduce(0, (x, y) -> x + y);
            return totalCount - maxCount * (SEA_MONSTER.size());
        }

        int getNumCountOfPatternInOrientation(List<IntPair> intPairs) {
            int count = 0;
            int x = 0;
            int y = 0;
            boolean isWithinVerticalLimit = true;
            boolean isWithinHorizontalLimit = true;
            while (isWithinVerticalLimit) {
                while (isWithinHorizontalLimit) {
                    boolean doesMatch = true;
                    for (IntPair pair : intPairs) {
                        int newX = x + pair.first;
                        int newY = y + pair.second;
                        if (x == 2 && y == 2) {
                            System.out.println("before (x, y): " + newX + " " + newY + ", matches: " + doesMatch);
                        }
                        if (newY >= PICTURE_DIMENSION) {
                            isWithinHorizontalLimit = false;
                            doesMatch = false;
                            break;
                        }
                        if (newX >= PICTURE_DIMENSION) {
                            isWithinVerticalLimit = false;
                            doesMatch = false;
                            break;
                        }

                        if (!image[newX][newY]) {
                            doesMatch = false;
                            break;
                        }
                        if (x == 2 && y == 2) {
                            System.out.println("after (x, y): " + newX + " " + newY + ", matches: " + doesMatch);
                        }
                    }
                    if (doesMatch) {
                        count++;
                    }
                    y++;
                }
                y = 0;
                x++;
                isWithinHorizontalLimit = true;
            }
            return count;
        }





    }

}
