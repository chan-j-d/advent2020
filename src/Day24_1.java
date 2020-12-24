import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day24_1 {

    static List<String> twoLengthDirections = List.of("se", "sw", "nw", "ne");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Grid grid = new Grid();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            grid.parseTileString(nextLine);
        }

        System.out.println(grid.getNumBlackTiles());
    }

    static class Grid {
        List<Tile> tiles;

        Grid() {
            tiles = new ArrayList<>();
            tiles.add(new Tile(0, 0));
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

    }

}
