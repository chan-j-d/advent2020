import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16_1 {

    static List<Field> listOfFields = new ArrayList<>();
    static List<TicketValues> listOfTickets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        while (!nextLine.isEmpty()) {
            String[] details1 = nextLine.split(": ");
            String fieldName = details1[0];
            String[] details2 = details1[1].split(" or ");
            String[] intervalDetails1 = details2[0].split("-");
            String[] intervalDetails2 = details2[1].split("-");
            Interval interval1 = new Interval(Integer.parseInt(intervalDetails1[0]),
                    Integer.parseInt(intervalDetails1[1]));
            Interval interval2 = new Interval(Integer.parseInt(intervalDetails2[0]),
                    Integer.parseInt(intervalDetails2[1]));

            Field newField = new Field(fieldName, interval1, interval2);
            listOfFields.add(newField);
            nextLine = scanner.nextLine();
        }

        scanner.nextLine();
        TicketValues myTicket = new TicketValues(scanner.nextLine());
        scanner.nextLine();
        scanner.nextLine();


        List<Integer> listOfInvalidFields = new ArrayList<>();

        while (scanner.hasNext()) {
            listOfTickets.add(new TicketValues(scanner.nextLine()));
        }

        List<TicketValues> filteredTicketList = listOfTickets.stream()
                .filter(ticket -> ticket.isValidTicket(listOfFields))
                .collect(Collectors.toList());

        int numFields = filteredTicketList.get(0).getNumFields();

        List<List<Integer>> listOfPossibleMatchingIndices = listOfFields.stream()
                .map(field -> Stream.iterate(0, x -> x < numFields, x -> x + 1)
                        .filter(x -> filteredTicketList.stream()
                                .allMatch(y -> y.isValidValueForField(x, field)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        Map<Integer, List<Integer>> mapOfIndexToField = new HashMap<>();
        for (int i = 0; i < numFields; i++) {
            List<Integer> currentFieldList = listOfPossibleMatchingIndices.get(i);
            for (int j : currentFieldList) {
                if (!mapOfIndexToField.containsKey(j)) {
                    mapOfIndexToField.put(j, new ArrayList<>(List.of(i)));
                } else {
                    mapOfIndexToField.get(j).add(i);
                }
            }
        }

        //System.out.println(mapOfIndexToField);

        //System.out.println(listOfPossibleMatchingIndices);

        List<Integer> filterList = List.of(7, 10, 13, 16, 17, 19);
        System.out.println(filterList.stream()
                .reduce(1L, (x, y) -> x * myTicket.listOfValues.get(y),
                        (x, y) -> x * y));

    }

    static class Field {

        Interval interval1;
        Interval interval2;

        String fieldName;

        Field(String fieldName, Interval interval1, Interval interval2) {
            this.fieldName = fieldName;
            this.interval1 = interval1;
            this.interval2 = interval2;
        }

        boolean doesMatchField(int value) {
            return interval1.isWithinInterval(value) || interval2.isWithinInterval(value);
        }

    }

    static class Interval {

        int lowerBound;
        int upperBound;

        Interval(int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        boolean isWithinInterval(int value) {
            return value <= upperBound && value >= lowerBound;
        }

    }

    static class TicketValues {

        List<Integer> listOfValues;

        TicketValues(List<Integer> listOfValues) {
            this.listOfValues = listOfValues;
        }

        TicketValues(String values) {
            this(Arrays.stream(values.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }

        List<Integer> getListOfValues() {
            return listOfValues;
        }

        List<Integer> getListOfValuesNotMatching(List<Field> listOfFields) {
            return listOfValues.stream()
                    .filter(value -> !listOfFields.stream()
                            .anyMatch(field -> field.doesMatchField(value)))
                    .collect(Collectors.toList());
        }

        boolean isValidTicket(List<Field> listOfFields) {
            return getListOfValuesNotMatching(listOfFields).size() == 0;
        }

        boolean isValidValueForField(int valueIndex, Field field) {
            return field.doesMatchField(listOfValues.get(valueIndex));
        }

        int getNumFields() {
            return listOfValues.size();
        }

    }

}
