import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day8_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InstructionMemory memory = new InstructionMemory();

        while (scanner.hasNext()) {
            memory.addInstruction(parseInstruction(scanner.nextLine()));
        }

        Set<Integer> integerLoopSet = getInstructionLoopSet(memory);

        int instructionToReplace = integerLoopSet.stream()
                .filter(x -> isProperTerminatingInstructionSet(memory, x))
                .findFirst().get();

        memory.setInstruction(instructionToReplace,
                flipInstruction(memory.getInstruction(instructionToReplace)));

        int instructionNumber = 0;
        boolean hasTerminated = false;
        Machine machine = new Machine();
        while (!hasTerminated) {
            int numberChange = machine.execute(memory.getInstruction(instructionNumber));
            instructionNumber = instructionNumber + numberChange;
            hasTerminated = instructionNumber >= memory.size();
        }

        System.out.println(machine.getAccumulator());
    }

    static boolean isProperTerminatingInstructionSet(InstructionMemory memory, int instructionIndexToFlip) {
        if (!isNotAccInstruction(memory.getInstruction(instructionIndexToFlip))) {
            return false;
        }

        int instructionNumber = 0;
        Set<Integer> instructionLinesExecuted = new HashSet<>();
        Machine machine = new Machine();

        boolean isTerminationConditionMet = false;
        boolean isRepeated = false;
        boolean hasTerminatedProperly = false;

        while (!isTerminationConditionMet) {
            Instruction instructionToExecute = memory.getInstruction(instructionNumber);
            int instructionNumberChange;
            if (instructionNumber == instructionIndexToFlip) {
                instructionNumberChange = machine.execute(flipInstruction(instructionToExecute));
            } else {
                instructionNumberChange = machine.execute(instructionToExecute);
            }

            instructionLinesExecuted.add(instructionNumber);
            instructionNumber = instructionNumber + instructionNumberChange;

            if (instructionLinesExecuted.contains(instructionNumber)) {
                isRepeated = true;
            }
            hasTerminatedProperly = instructionNumber >= memory.size();

            isTerminationConditionMet = hasTerminatedProperly || isRepeated;
        }

        return hasTerminatedProperly;
    }

    static Set<Integer> getInstructionLoopSet(InstructionMemory memory) {

        Set<Integer> instructionLinesExecuted = new HashSet<>();
        Set<Integer> instructionLinesExecutedTwice = new HashSet<>();
        int instructionNumber = 0;
        boolean isNextRepeated = false;

        Machine machine = new Machine();
        while (!isNextRepeated) {
            Instruction instructionToExecute = memory.getInstruction(instructionNumber);
            int instructionNumberChange = machine.execute(instructionToExecute);

            if (instructionLinesExecuted.contains(instructionNumber)) {
                instructionLinesExecutedTwice.add(instructionNumber);
            } else {
                instructionLinesExecuted.add(instructionNumber);
            }
            instructionNumber = instructionNumber + instructionNumberChange;

            isNextRepeated = instructionLinesExecutedTwice.contains(instructionNumber);
        }

        return instructionLinesExecutedTwice;
    }

    static boolean isNotAccInstruction(Instruction instruction) {
        return !instruction.type.equals(InstructionType.ACC);
    }

    static Instruction flipInstruction(Instruction instruction) {
        InstructionType type = instruction.type;
        InstructionType newType;
        switch (type) {
        case NOP:
            newType = InstructionType.JMP;
            break;
        case JMP:
            newType = InstructionType.NOP;
            break;
        default:
            throw new IllegalArgumentException("Not a valid instruction type to flip");
        }
        return new Instruction(newType, instruction.value);
    }

    static Instruction parseInstruction(String instructionDetails) {
        String[] details = instructionDetails.split(" ");
        String opType = details[0];
        int value = Integer.parseInt(details[1]);
        InstructionType type;

        switch (opType) {
        case "nop":
            type = InstructionType.NOP;
            break;
        case "acc":
            type = InstructionType.ACC;
            break;
        case "jmp":
            type = InstructionType.JMP;
            break;
        default:
            throw new IllegalArgumentException("Not a valid command type");
        }

        return new Instruction(type, value);
    }

    static class Machine {

        private static final int SINGLE_STEP = 1;

        int accumulator;

        Machine() {
            accumulator = 0;
        }

        int execute(Instruction instruction) {
            InstructionType type = instruction.type;
            int value = instruction.value;
            switch (type) {
            case NOP:
                return executeNop();
            case ACC:
                return executeAcc(value);
            case JMP:
                return executeJmp(value);
            default:
                throw new IllegalArgumentException("Illegal instruction type");
            }
        }

        int executeNop() {
            return SINGLE_STEP;
        }

        int executeAcc(int valueChange) {
            accumulator = accumulator + valueChange;
            return SINGLE_STEP;
        }

        int executeJmp(int jump) {
            return jump;
        }

        int getAccumulator() {
            return accumulator;
        }

    }

    static class Instruction {
        InstructionType type;
        int value;

        Instruction(InstructionType type, int value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + ": " + value;
        }
    }

    static enum InstructionType {
        NOP, ACC, JMP;
    }

    static class InstructionMemory {
        List<Instruction> instructions;

        InstructionMemory() {
            instructions = new ArrayList<>();
        }

        void addInstruction(Instruction instruction) {
            instructions.add(instruction);
        }

        Instruction getInstruction(int instructionNumber) {
            return instructions.get(instructionNumber);
        }

        void setInstruction(int index, Instruction instruction) {
            instructions.set(index, instruction);
        }

        int size() {
            return instructions.size();
        }

        @Override
        public String toString() {
            return instructions.toString();
        }

    }

}
