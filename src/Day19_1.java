import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day19_1 {

    static Map<Integer, Rule> ruleMap = new HashMap<>();
    static Set<Integer> basicRuleSet = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        do {
            String[] details = nextLine.split(": ");
            int id = Integer.parseInt(details[0]);
            ruleMap.put(id, new Rule(id, details[1]));
            nextLine = scanner.nextLine();
        } while (!nextLine.isBlank());

        long numMatches = scanner.tokens()
                .filter(string -> ruleMap.get(0).doesMatchRule(string))
                .peek(System.out::println)
                .count();

        System.out.println(numMatches);

    }


    static class Rule {

        int id;
        boolean isString;
        boolean isCalculated;
        List<List<Integer>> rules;
        Optional<String> basicString;

        Rule(int id, String rule) {
            this.id = id;
            init(rule);
        }

        void init(String rule) {
            if (rule.contains("\"")) {
                isString = true;
                isCalculated = true;
                basicRuleSet.add(id);
                basicString = Optional.of(rule.split("\"")[1]);
            } else {
                isString = false;
                isCalculated = false;
                rules = Arrays.stream(rule.split(" \\| "))
                        .map(ints -> Arrays.stream(ints.split(" "))
                                .map(s -> Integer.parseInt(s))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());
            }
        }

        boolean doesMatchRule(String string) {
            if (isString) {
                return basicString.get().equals(string);
            }

            return rules.stream()
                    .anyMatch(list -> doesMatchSubRule(string, list));
        }

        boolean doesMatchSubRule(String string, List<Integer> subRule) {
            if (subRule.size() == 1) {
                return ruleMap.get(subRule.get(0)).doesMatchRule(string);
            } else if (string.length() <= 1) {
                return false;
            }
            boolean hasBasicRule = subRule.stream()
                    .anyMatch(x -> basicRuleSet.contains(x));
            if (hasBasicRule) {
                if (basicRuleSet.contains(subRule.get(0))) {
                    return ruleMap.get(subRule.get(0)).doesMatchRule(string.substring(0, 1))
                            && ruleMap.get(subRule.get(1)).doesMatchRule(string.substring(1));
                } else {
                    int stringLength = string.length();
                    return ruleMap.get(subRule.get(1)).doesMatchRule(string.substring(stringLength - 1))
                            && ruleMap.get(subRule.get(0)).doesMatchRule(string.substring(0, stringLength - 1));
                }
            } else if (subRule.size() == 1) {
                return ruleMap.get(subRule.get(0)).doesMatchRule(string);
            } else {
                Rule rule1 = ruleMap.get(subRule.get(0));
                Rule rule2 = ruleMap.get(subRule.get(1));
                int stringLength = string.length();
                for (int i = 0; i < stringLength - 1; i++) {
                    String string1 = string.substring(0, i + 1);
                    String string2 = string.substring(i + 1);
                    boolean matchesSplit = rule1.doesMatchRule(string1)
                            && rule2.doesMatchRule(string2);
                    if (matchesSplit) {
                        return true;
                    }
                }
                return false;
            }
        }

    }


}
