import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

public class Day4_2 {

    private static final Set<String> VALID_FIELDS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
    private static final Set<String> REQUIRED_FIELDS_PASSPORT = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
    private static final Set<String> REQUIRED_FIELDS_NORTHPOLECREDENTIALS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private static final Set<String> VALID_HAIR_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static final Map<String, Predicate<String>> PREFIX_PREDICATES = new HashMap<>();
    static {
        PREFIX_PREDICATES.put("byr", s -> checkStringIntValueBetween(s, 1920, 2002));
        PREFIX_PREDICATES.put("iyr", s -> checkStringIntValueBetween(s, 2010, 2020));
        PREFIX_PREDICATES.put("eyr", s -> checkStringIntValueBetween(s, 2020, 2030));
        PREFIX_PREDICATES.put("hgt", s -> {
            s = s.trim();
            boolean isCm = s.substring(s.length() - 2).equals("cm");
            boolean isIn = s.substring(s.length() - 2).equals("in");
            int value = Integer.parseInt(s.replaceFirst("[^0-9]+", ""));
            if (isCm) {
                return value >= 150 && value <= 193;
            } else if (isIn) {
                return value >= 59 && value <= 76;
            } else {
                return false;
            }
        });
        PREFIX_PREDICATES.put("hcl", s -> {
            s = s.trim();
            if (s.charAt(0) != '#') {
                return false;
            }

            String remainder = s.substring(1);
            return remainder.matches("[a-z0-9]+");
        });
        PREFIX_PREDICATES.put("ecl", s -> VALID_HAIR_COLORS.contains(s.trim()));
        PREFIX_PREDICATES.put("pid", s -> s.trim().length() == 9 && s.trim().matches("[0-9]+"));
        PREFIX_PREDICATES.put("cid", s -> true);
    }

    private static final String KEY_VALUE_SEPARATOR = "[:]";
    private static final String PASSPORT_SEPARATOR = ((char) 13) + "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[ \n]");
        int validCount = 0;
        NorthPoleCredentials record = new NorthPoleCredentials();
        while (scanner.hasNext()) {
            String nextField = scanner.next();
            if (nextField.equals(PASSPORT_SEPARATOR)) {
                if (record.isValidRecord()) {
                    validCount++;
                }
                record = new NorthPoleCredentials();
                continue;
            }
            record.addField(nextField);
        }

        if (record.isValidRecord()) {
            validCount++;
        }

        System.out.println(validCount);
    }

    static abstract class Record {

        Set<String> getValidFields() {
            return VALID_FIELDS;
        }

        abstract Set<String> getRequiredFields();

        Map<String, String> recordDetails;

        Record() {
            recordDetails = new HashMap<>();
        }

        void addField(String field) {
            String[] details = field.split(KEY_VALUE_SEPARATOR);
            recordDetails.put(details[0], details[1]);
        }

        boolean isValidRecord() {
            for (String prefix : getRequiredFields()) {
                if (!recordDetails.containsKey(prefix)) {
                    return false;
                }

                String testField = recordDetails.get(prefix);
                Predicate<String> testPredicate = PREFIX_PREDICATES.get(prefix);
                if (!testPredicate.test(testField)) {
                    return false;
                }
            }
            return true;
        }

    }

    static boolean checkStringIntValueBetween(String s, int lower, int upper) {
        s = s.trim();
        int value = Integer.parseInt(s);
        return value >= lower && value <= upper;
    }

    static class Passport extends Record {
        @Override
        Set<String> getRequiredFields() {
            return REQUIRED_FIELDS_PASSPORT;
        }
    }

    static class NorthPoleCredentials extends Record {
        @Override
        Set<String> getRequiredFields() {
            return REQUIRED_FIELDS_NORTHPOLECREDENTIALS;
        }
    }


}
