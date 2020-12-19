import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17_1 {

    static final char INDICATOR_INACTIVE = '.';
    static final char INDICATOR_ACTIVE = '#';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine;
        int x = 0;
        int y = 0;
        int z = 0;
        int w = 0;

        Set<Coordinate> activeCoordinates = new HashSet<>();
        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();
            for (char c : nextLine.toCharArray()) {
                if (c == INDICATOR_ACTIVE) {
                    activeCoordinates.add(new Coordinate(x, y, z, w));
                }

                x++;
            }


            x = 0;
            y++;
        }

        ConwayCube cube = new ConwayCube(activeCoordinates);

        //System.out.println(activeCoordinates);

        for (int i = 0; i < 6; i++) {
            cube.simulate();
        }

        System.out.println(cube.getActiveCount());

    }

    static class ConwayCube {

        List<Integer> lowerLimits;
        List<Integer> upperLimits;
        Set<Coordinate> activeCoordinates;

        ConwayCube(Set<Coordinate> activeCoordinates) {
            this.activeCoordinates = activeCoordinates;
            int xLowerLimit = -1;
            int xUpperLimit = activeCoordinates.stream()
                    .max((c1, c2) -> c1.x - c2.x)
                    .get()
                    .x + 1;
            int yLowerLimit = -1;
            int yUpperLimit = activeCoordinates.stream()
                    .max((c1, c2) -> c1.y - c2.y)
                    .get()
                    .y + 1;
            int zLowerLimit = -1;
            int zUpperLimit = 1;
            int wLowerLimit = -1;
            int wUpperLimit = 1;

            lowerLimits = new ArrayList<>(List.of(xLowerLimit, yLowerLimit, zLowerLimit, wLowerLimit));
            upperLimits = new ArrayList<>(List.of(xUpperLimit, yUpperLimit, zUpperLimit, wUpperLimit));

        }

        List<Coordinate> getActiveSurroundingCoordinates(Coordinate coordinate) {
            return coordinate.getSurroundingCoordinates().stream()
                    .filter(x -> activeCoordinates.contains(x))
                    .collect(Collectors.toList());
        }

        boolean adjustActive(Coordinate coordinate) {
            List<Coordinate> surroundingActiveCoordinates = getActiveSurroundingCoordinates(coordinate);
            int numSurroundingActiveCoordinates = surroundingActiveCoordinates.size();
            return !(numSurroundingActiveCoordinates == 2 || numSurroundingActiveCoordinates == 3);
        }

        boolean adjustInactive(Coordinate coordinate) {
            List<Coordinate> surroundingActiveCoordinates = getActiveSurroundingCoordinates(coordinate);
            //System.out.println(coordinate + ": " + surroundingActiveCoordinates);
            return surroundingActiveCoordinates.size() == 3;
        }

        void flipCoordinate(Coordinate coordinate) {
            if (activeCoordinates.contains(coordinate)) {
                activeCoordinates.remove(coordinate);
            } else {
                activeCoordinates.add(coordinate);
                updateLimits(coordinate);
            }
        }

        void updateLimits(Coordinate coordinate) {
            List<Integer> coordinates = coordinate.getCoordinates();
            for (int i = 0; i < 4; i++) {
                if (coordinates.get(i) == lowerLimits.get(i)) {
                    lowerLimits.set(i, lowerLimits.get(i) - 1);
                }
                if (coordinates.get(i) == upperLimits.get(i)) {
                    upperLimits.set(i, upperLimits.get(i) + 1);
                }
            }
        }

        void simulate() {
            //System.out.println("Lower limits: " + lowerLimits);
            //System.out.println("Upper limits: " + upperLimits);


            Set<Coordinate> coordinatesToFlip = new HashSet<>();
            for (int i = lowerLimits.get(0); i <= upperLimits.get(0); i++) {
                for (int j = lowerLimits.get(1); j <= upperLimits.get(1); j++) {
                    for (int k = lowerLimits.get(2); k <= upperLimits.get(2); k++) {
                        for (int l = lowerLimits.get(3); l <= upperLimits.get(3); l++) {
                            Coordinate coordinateTest = new Coordinate(i, j, k, l);
                            boolean toFlip;
                            if (activeCoordinates.contains(coordinateTest)) {
                                toFlip = adjustActive(coordinateTest);
                            } else {
                                toFlip = adjustInactive(coordinateTest);
                            }
                            //System.out.println(coordinateTest + " " + toFlip);

                            if (toFlip) {
                                coordinatesToFlip.add(coordinateTest);
                            }
                        }
                    }
                }
            }
            coordinatesToFlip.stream()
                    .forEach(coordinate -> flipCoordinate(coordinate));
            //System.out.println(activeCoordinates);
        }

        int getActiveCount() {
            return activeCoordinates.size();
        }

    }

    static class Coordinate {

        int x;
        int y;
        int z;
        int w;

        Coordinate(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        List<Coordinate> getSurroundingCoordinates() {
            List<Coordinate> listOfCoordinates = new ArrayList<>();
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    for (int k = z - 1; k <= z + 1; k++) {
                        for (int l = w - 1; l <= w + 1; l++) {
                            listOfCoordinates.add(new Coordinate(i, j, k, l));
                        }
                    }
                }
            }
            listOfCoordinates.remove(this);
            return listOfCoordinates;
        }

        List<Integer> getCoordinates() {
            return List.of(x, y, z, z);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new int[] {x, y, z, w});
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Coordinate)) {
                return false;
            } else {
                Coordinate other = (Coordinate) o;
                return other.x == x
                        && other.y == y
                        && other.z == z
                        && other.w == w;
            }
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d, %d)", x, y, z, w);
        }

    }



}
