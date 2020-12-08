import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

public class Day4_1 {

    private static final Set<String> VALID_FIELDS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
    private static final Set<String> REQUIRED_FIELDS_PASSPORT = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid");
    private static final Set<String> REQUIRED_FIELDS_NORTHPOLECREDENTIALS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

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
            }
            return true;
        }

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
