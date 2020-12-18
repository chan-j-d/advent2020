import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Day14_1 {

    static String INDICATOR_MASK = "mas";
    static String INDICATOR_MEMORY = "mem";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine;
        Memory memory = new Memory();
        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();
            String instructionType = nextLine.substring(0, 3);
            if (instructionType.equals(INDICATOR_MASK)) {
                memory.setMask(parseMask(nextLine));
            } else if (instructionType.equals(INDICATOR_MEMORY)) {
                memory.setValueAt(getInstructionIndex(nextLine),
                        getInstructionValue(nextLine));
            }


        }

        System.out.println(memory.getSumOfValues());

    }

    static Mask parseMask(String maskInstruction) {
        return new Mask(maskInstruction.split(" = ")[1]);
    }

    static int getInstructionIndex(String instruction) {
        return Integer.parseInt(instruction.split("\\[")[1].split("\\]")[0]);
    }

    static int getInstructionValue(String instruction) {
        return Integer.parseInt(instruction.split(" = ")[1]);
    }

    static class AddressValue {

        int[] memoryBits;

        AddressValue(int value) {
            memoryBits = new int[36];
            int index = 35;
            while (value != 0) {
                memoryBits[index--] = value % 2 == 0 ? '0' : '1';
                value = value / 2;
            }
        }

        long getValue() {
            return Arrays.stream(memoryBits)
                    .reduce(0, (x, y) -> x * 2 + getIntValueOfChar(y));
        }

        int getIntValueOfChar(int c) {
            return c == '1' ? 1 : 0;
        }

        void setBitAt(int index, int value) {
            memoryBits[index] = value;
        }

        long getValueWithMask(Mask mask) {
            int[] maskIntArray = mask.getMemoryBits();
            return Stream.iterate(0, x -> x < 36, x -> x + 1)
                    .reduce(0L, (x, y) -> {
                        int maskValue = maskIntArray[y];
                        int valueToAdd;
                        if (maskValue == 'X') {
                            valueToAdd = getIntValueOfChar(memoryBits[y]);
                        } else {
                            valueToAdd = getIntValueOfChar(maskValue);
                        }
                        return (long) (2 * x + valueToAdd);
                    },
                            (x, y) -> x + y);
        }

        public String toString() {
            return getValue() + "";
        }

    }

    static class Mask {

        int[] memoryBits;

        Mask(String mask) {
            memoryBits = mask.chars()
                    .toArray();
        }

        int[] getMemoryBits() {
            return memoryBits;
        }

        public String toString() {
            return Arrays.toString(memoryBits);
        }
    }


    static class Memory {

        Mask mask;
        Map<Integer, Long> memory;

        Memory() {
            memory = new HashMap<>();
        }

        void setValueAt(int index, int value) {
            memory.put(index, new AddressValue(value).getValueWithMask(mask));
        }

        void setMask(Mask mask) {
            this.mask = mask;
        }

        BigInteger getSumOfValues() {
            return memory.entrySet().stream()
                    .reduce(BigInteger.ZERO, (x, y) -> BigInteger.valueOf(y.getValue()).add(x), (x, y) -> x.add(y));
        }



        public String toString() {
            return memory.toString();
        }

    }

}
