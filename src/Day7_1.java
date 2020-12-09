import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7_1 {

    private static final String BAG_COLOR = "shiny gold";

    static Map<String, BagType> setOfBagTypes = new HashMap<>();

    static Set<BagType> memoizedSet = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            BagType nextBagType = parseBagType(scanner.nextLine());
            setOfBagTypes.put(nextBagType.getColor(), nextBagType);
        }

        System.out.println(setOfBagTypes);
        int count = (int) setOfBagTypes.entrySet()
                .stream()
                .filter(bagTypeMapEntry -> isSubBagTypeOf(bagTypeMapEntry.getValue(), BAG_COLOR))
                .count();

        System.out.println(count);

    }

    static boolean isSubBagTypeOf(BagType bagType, String subBagType) {
        if (memoizedSet.contains(bagType)) {
            return true;
        }

        boolean isSubBag = bagType.hasDirectSubBagType(subBagType)
                || bagType.getSubBagTypes().stream()
                        .map(sub -> setOfBagTypes.get(sub))
                        .anyMatch(x -> isSubBagTypeOf(x, subBagType));

        if (isSubBag) {
            memoizedSet.add(bagType);
        }

        return isSubBag;
    }


    static BagType parseBagType(String bagTypeDetails) {
        String[] bagAndSubBagTypes = bagTypeDetails.split("( bag[s]? contain [0-9] )"
                + "| bag[s]?[,] [0-9] "
                + "|( bag[s]?[.])"
                + "| bags contain no other bags.");
        String bagTypeString = bagAndSubBagTypes[0];
        Set<String> subBagTypeStrings = Arrays.stream(bagAndSubBagTypes)
                .filter(x -> !x.equals(bagTypeString))
                .collect(Collectors.toSet());
        return new BagType(bagTypeString, subBagTypeStrings);
    }

    static class BagType {

        String color;
        Set<String> subBagTypes;

        BagType(String color, Set<String> subBagTypes) {
            this.color = color;
            this.subBagTypes = subBagTypes;
        }

        boolean hasDirectSubBagType(String subBagType) {
            return this.subBagTypes.contains(subBagType);
        }

        String getColor() {
            return color;
        }

        Set<String> getSubBagTypes() {
            return subBagTypes;
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
