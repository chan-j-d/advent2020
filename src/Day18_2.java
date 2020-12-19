import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day18_2 {

    public static void main(String[] args) {
        List<Expression> expressions = new ArrayList<>();
        String nextLine;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();
            Expression currentExpression = new Expression();
            nextLine.chars()
                    .filter(c -> c != ' ')
                    .forEach(c -> currentExpression.addChar((char) c));
            expressions.add(currentExpression);
        }

        long answer = expressions.stream()
                .map(x -> x.getValue())
                .reduce(0L, (x, y) -> x + y, (x, y) -> x + y);

        System.out.println(answer);

    }


    static class Expression {

        final char SYMBOL_OPEN = '(';
        final char SYMBOL_CLOSE = ')';
        final char SYMBOL_PLUS = '+';
        final char SYMBOL_MULTIPLY = '*';
        final Set<Character> OPERATION_SYMBOLS = Set.of(SYMBOL_PLUS, SYMBOL_MULTIPLY);

        Expression nestedExpression;
        long inNested;

        boolean isPlus;
        boolean isMultiply;
        boolean prevMultiply;
        long tempValue;
        long currentValue;

        Expression() {
            inNested = 0;
            isMultiply = false;
            isPlus = false;
            tempValue = 1;
        }

        void addChar(char c) {
            if (c == SYMBOL_OPEN) {
                if (inNested == 0) {
                    inNested = 1;
                    nestedExpression = new Expression();
                } else {
                    inNested++;
                    nestedExpression.addChar(c);
                }
            } else if (c == SYMBOL_CLOSE) {
                if (inNested == 1) {
                    inNested = 0;
                    updateValue(nestedExpression.getValue());
                } else {
                    inNested--;
                    nestedExpression.addChar(c);
                }
            } else if (inNested != 0) {
                nestedExpression.addChar(c);
            } else if (c == SYMBOL_PLUS) {
                isPlus = true;
                isMultiply = false;
            } else if (c == SYMBOL_MULTIPLY) {
                isMultiply = true;
                isPlus = false;
            } else {
                long value = Integer.parseInt(c + "");
                updateValue(value);
            }
        }

        long getValue() {
            return currentValue * tempValue;
        }

        void updateValue(long value) {
            if (isMultiply) {
                if (prevMultiply) {
                    currentValue = currentValue * tempValue;
                } else {
                    prevMultiply = true;
                }
                tempValue = value;
            } else if (isPlus) {
                if (prevMultiply) {
                    tempValue = tempValue + value;
                } else {
                    currentValue = currentValue + value;
                }
            } else {
                currentValue = value;
            }
        }





    }


}
