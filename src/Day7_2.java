import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7_2 {

    private static final String BAG_COLOR = "shiny gold";

    static Map<String, BagType> setOfBagTypes = new HashMap<>();

    static Set<BagType> memoizedSet = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            BagType nextBagType = parseBagType(scanner.nextLine());
            setOfBagTypes.put(nextBagType.getColor(), nextBagType);

            System.out.println(nextBagType);
        }

        System.out.println(setOfBagTypes.get("shiny gold").getCount());
    }

    static boolean isSubBagTypeOf(BagType bagType, String subBagType) {
        if (memoizedSet.contains(bagType)) {
            return true;
        }

        boolean isSubBag = bagType.hasDirectSubBagType(subBagType)
                || bagType.getSubBagTypes().entrySet().stream()
                .map(sub -> setOfBagTypes.get(sub))
                .anyMatch(x -> isSubBagTypeOf(x, subBagType));

        if (isSubBag) {
            memoizedSet.add(bagType);
        }

        return isSubBag;
    }


    static BagType parseBagType(String bagTypeDetails) {
        String[] bagAndSubBagTypes = bagTypeDetails.split("( bag[s]? contain )"
                + "| bag[s]?[,] "
                + "|( bag[s]?[.])");
        String bagTypeString = bagAndSubBagTypes[0];
        Map<String, Integer> subBagTypeStrings = Arrays.stream(bagAndSubBagTypes)
                .filter(x -> !x.equals(bagTypeString))
                .filter(x -> !x.equals("no other"))
                .collect(Collectors.toMap(x -> x.split(" ", 2)[1],
                        x -> Integer.parseInt(x.split(" ", 2)[0])));
        return new BagType(bagTypeString, subBagTypeStrings);
    }

    static class BagType {

        String color;
        Map<String, Integer> subBagTypes;
        Optional<Integer> count;

        BagType(String color, Map<String, Integer> subBagTypes) {
            this.color = color;
            this.subBagTypes = subBagTypes;
            count = Optional.empty();
        }

        boolean hasDirectSubBagType(String subBagType) {
            return this.subBagTypes.containsKey(subBagType);
        }

        String getColor() {
            return color;
        }

        Map<String, Integer> getSubBagTypes() {
            return subBagTypes;
        }

        int getCount() {
            if (count.isPresent()) {
                return count.get();
            }

            int finalCount = subBagTypes.entrySet().stream()
                    .map(pair -> pair.getValue() + pair.getValue() * setOfBagTypes.get(pair.getKey()).getCount())
                    .reduce(0, (x, y) -> x + y);
            count = Optional.of(finalCount);
            return finalCount;
        }

        @Override
        public int hashCode() {
            return color.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof BagType)) {
                return false;
            } else {
                return ((BagType) o).color.equals(this.color);
            }
        }

        @Override
        public String toString() {
            return color + ": " + subBagTypes;
        }

    }

}
