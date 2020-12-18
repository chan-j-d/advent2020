import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Day14_2 {

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
                int memoryIndex = getInstructionIndex(nextLine);
                long memoryValue = getInstructionValue(nextLine);
                MemoryAddress address = new MemoryAddress(memoryIndex);
                memory.addValue(address, memoryValue);
            }

            System.out.println(memory);


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

    static class MemoryAddress {
        int[] address;

        MemoryAddress(long value) {
            address = new int[36];
            int index = 35;
            while (value != 0) {
                address[index--] = value % 2 == 0 ? 0 : 1;
                value = value / 2;
            }
        }

        MemoryAddress(int[] address) {
            this.address = address;
        }

        long getValue() {
            return Arrays.stream(address)
                    .mapToLong(x -> (long) x)
                    .reduce(0L, (x, y) -> x * 2 + y);
        }

    }

    static class Mask {

        List<Integer> oneIndices;
        List<Integer> xIndices;

        Mask(String maskValue) {
            oneIndices = new ArrayList<>();
            xIndices = new ArrayList<>();
            for (int i = 0; i < maskValue.length(); i++) {
                if (maskValue.charAt(i) == '1') {
                    oneIndices.add(i);
                } else if (maskValue.charAt(i) == 'X') {
                    xIndices.add(i);
                }
            }
        }

        Stream<MemoryAddress> maskedMemoryAddresses(MemoryAddress address) {
            preMaskMemoryAddress(address);
            Stream<MemoryAddress> stream = Stream.of(address);
            for (int i = 0; i < xIndices.size(); i++) {
                stream = stream.flatMap(Mask::splitFirstXMemoryAddress);
            }
            return stream;
        }

        static Stream<MemoryAddress> splitFirstXMemoryAddress(MemoryAddress address) {
            int firstXIndex = getFirstXIndex(address);
            if (firstXIndex == -1) {
                return Stream.of(address);
            }

            int[] array1 = Arrays.copyOf(address.address, address.address.length);
            array1[firstXIndex] = 0;
            int[] array2 = Arrays.copyOf(address.address, address.address.length);
            array2[firstXIndex] = 1;

            return Stream.of(new MemoryAddress(array1), new MemoryAddress(array2));
        }

        static int getFirstXIndex(MemoryAddress address) {
            for (int i = 0; i < address.address.length; i++) {
                if (address.address[i] == 'X') {
                    return i;
                }
            }
            return -1;
        }

        void preMaskMemoryAddress(MemoryAddress toPreMask) {
            for (int i : oneIndices) {
                toPreMask.address[i] = 1;
            }
            for (int i : xIndices) {
                toPreMask.address[i] = 'X';
            }
        }

    }

    static class Memory {

        Mask mask;
        Map<Long, Long> memory;

        Memory() {
            memory = new HashMap<>();
        }

        void addValue(MemoryAddress address, long value) {
            mask.maskedMemoryAddresses(address)
                    .forEach(a -> memory.put(a.getValue(), value));
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
