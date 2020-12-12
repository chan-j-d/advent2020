import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day12_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Instruction> instructions = new ArrayList<>();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            instructions.add(new Instruction(nextLine.charAt(0), Integer.parseInt(nextLine.substring(1))));
        }

        Ship ship = new Ship();
        instructions.stream()
                .peek(x -> System.out.println(ship))
                .peek(System.out::println)
                .forEach(i -> ship.executeInstruction(i));

        System.out.println(ship.getManhattanDistance());
    }

    static final char EAST = 'E';
    static final char NORTH = 'N';
    static final char SOUTH = 'S';
    static final char WEST = 'W';

    static List<Character> DIRECTION_ORDER = List.of(NORTH, EAST, SOUTH, WEST);
    static int NUM_DIRECTIONS = DIRECTION_ORDER.size();

    static Map<Character, IntPair> DIRECTION_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put('N', new IntPair(0, 1));
        DIRECTION_MAP.put('E', new IntPair(1, 0));
        DIRECTION_MAP.put('S', new IntPair(0, -1));
        DIRECTION_MAP.put('W', new IntPair(-1, 0));
    }


    static class Instruction {
        char instructionType;
        int value;

        Instruction(char instructionType, int value) {
            this.instructionType = instructionType;
            this.value = value;
        }

        public String toString() {
            return instructionType + ": " + value;
        }
    }

    static class Ship {

        int x;
        int y;

        int wX;
        int wY;

        Ship() {
            x = 0;
            y = 0;
            wX = 10;
            wY = 1;
        }

        void executeInstruction(Instruction instruction) {
            char type = instruction.instructionType;
            switch (type) {
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
                moveInDirection(instruction);
                break;
            case 'R':
            case 'L':
                changeDirection(instruction);
                break;
            case 'F':
                moveFacingDirection(instruction);
                break;
            default:
                throw new IllegalArgumentException("Not a valid instruction type");
            }
        }

        void moveFacingDirection(Instruction instruction) {
            x = x + wX * instruction.value;
            y = y + wY * instruction.value;
        }

        void moveInDirection(Instruction instruction) {
            IntPair pair = DIRECTION_MAP.get(instruction.instructionType);
            wX = wX + pair.first * instruction.value;
            wY = wY + pair.second * instruction.value;
        }

        void changeDirection(Instruction instruction) {
            char type = instruction.instructionType;
            if (type == 'R') {
                getNewWaypoint(true, instruction.value);
            } else {
                getNewWaypoint(false, instruction.value);
            }
        }

        void getNewWaypoint(boolean isRight, int degree) {
            int num90Turns = degree / 90;
            char newDirection;
            while (num90Turns != 0) {
                if (isRight) {
                    int temp = wY;
                    wY = -wX;
                    wX = temp;
                } else {
                    int temp = wX;
                    wX = -wY;
                    wY = temp;
                }

                num90Turns--;
            }
        }

        int getManhattanDistance() {
            return Math.abs(x) + Math.abs(y);
        }

        public String toString() {
            return "(x, y) = " + x + ", " + y + " | (wX, wY) = " + wX + ", " + wY;
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
