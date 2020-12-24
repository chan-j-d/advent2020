import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Day24_2 {

    static List<String> twoLengthDirections = List.of("se", "sw", "nw", "ne");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Grid grid = new Grid();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            grid.parseTileString(nextLine);
        }

        System.out.println(grid.getNumBlackTiles());

        System.out.println(grid);
        for (int i = 0; i < 100; i++) {
            grid.processDay();
            System.out.println(grid.getNumBlackTiles());
        }
        System.out.println(grid.getNumBlackTiles());
    }

    static class Grid {
        List<Tile> tiles;
        int xMax;
        int yMax;

        Grid() {
            tiles = new ArrayList<>();
            tiles.add(new Tile(0, 0));
            xMax = 1;
            yMax = 1;
        }

        void parseTileString(String string) {
            String refString = string;
            Tile tileToMoveTo = new Tile(0, 0);
            while (!refString.isEmpty()) {
                if (refString.length() == 1) {
                    tileToMoveTo = tileToMoveTo.move(refString);
                    refString = "";
                } else {
                    String substring = refString.substring(0, 2);
                    if (twoLengthDirections.contains(substring)) {
                        refString = refString.substring(2);
                        tileToMoveTo = tileToMoveTo.move(substring);
                    } else {
                        substring = refString.substring(0, 1);
                        tileToMoveTo = tileToMoveTo.move(substring);
                        refString = refString.substring(1);
                    }
                }
            }
            if (!tiles.contains(tileToMoveTo)) {
                tiles.add(tileToMoveTo);
                tileToMoveTo.flip();
                xMax = Math.max(xMax, Math.abs(tileToMoveTo.x) + 1);
                yMax = Math.max(yMax, Math.abs(tileToMoveTo.y) + 1);
            } else {
                Tile refTile = tiles.get(tiles.indexOf(tileToMoveTo));
                refTile.flip();
            }
        }

        int getNumBlackTiles() {
            return (int) tiles.stream()
                    .filter(tile -> tile.color == 'b')
                    .count();
        }

        void processDay() {
            List<Tile> tilesToFlip = new ArrayList<>();
            for (int i = -xMax; i <= xMax; i++) {
                for (int j = -yMax; j <= yMax; j++) {
                    Tile currentTile = new Tile(i, j);
                    if (tiles.contains(currentTile)) {
                        currentTile = tiles.get(tiles.indexOf(currentTile));
                    } else {
                        tiles.add(currentTile);
                    }

                    int numAdjacentBlack = (int) currentTile.getAdjacentTiles().stream()
                            .filter(tile -> tiles.contains(tile))
                            .map(tile -> tiles.get(tiles.indexOf(tile)))
                            .filter(tile -> tile.color == 'b')
                            .count();

                    if (currentTile.color == 'b') {
                        if (numAdjacentBlack == 0 || numAdjacentBlack > 2) {
                            tilesToFlip.add(currentTile);
                        }
                    } else {
                        if (numAdjacentBlack == 2) {
                            tilesToFlip.add(currentTile);
                            xMax = Math.max(xMax, Math.abs(currentTile.x) + 1);
                            yMax = Math.max(yMax, Math.abs(currentTile.y) + 1);
                        }
                    }
                }
            }

            tilesToFlip.stream()
                    .forEach(tile -> tile.flip());
        }

        public String toString() {
            return tiles.toString();
        }
    }

    static class Tile {

        int x;
        int y;
        char color;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
            color = 'w';
        }

        void flip() {
            if (color == 'w') {
                color = 'b';
            } else {
                color = 'w';
            }
        }

        Tile move(String direction) {
            switch (direction) {
            case "e":
                return new Tile(x + 1, y);
            case "se":
                return new Tile(x, y - 1);
            case "sw":
                return new Tile(x - 1, y - 1);
            case "w":
                return new Tile(x - 1, y);
            case "nw":
                return new Tile(x, y + 1);
            case "ne":
                return new Tile(x + 1, y + 1);
            default:
                throw new IllegalArgumentException("Not a valid direction");
            }
        }

        List<Tile> getAdjacentTiles() {
            List<Tile> tiles = new ArrayList<>();
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if ((i == x - 1 && j == y + 1)
                            || (i == x + 1 && j == y - 1)
                            || (i == x && j == y)) {
                        continue;
                    }

                    tiles.add(new Tile(i, j));
                }
            }
            return tiles;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new int[] {x, y});
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Tile)) {
                return false;
            } else {
                Tile other = (Tile) o;
                return other.x == x
                        && other.y == y;
            }
        }

        public String toString() {
            return String.format("(%d, %d): %c", x, y, color);
        }

    }

}
