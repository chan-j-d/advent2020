import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21_1 {

    static Map<String, List<List<String>>> allergenToSetOfFoodsMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Food> listOfFoods = new ArrayList<>();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            listOfFoods.add(new Food(nextLine));
        }

        //System.out.println(listOfFoods);

        listOfFoods.stream()
                .forEach(food -> food.allergens.stream()
                        .forEach(allergen -> {
                            if (!allergenToSetOfFoodsMap.containsKey(allergen)) {
                                allergenToSetOfFoodsMap.put(allergen,
                                        new ArrayList<>(List.of(food.getIngredients())));
                            } else {
                                allergenToSetOfFoodsMap.get(allergen).add(food.getIngredients());
                            }
                        }));

        //System.out.println(allergenToSetOfFoodsMap.keySet());

        allergenToSetOfFoodsMap.keySet()
                .stream()
                .forEach(Day21_1::processAllergen);

        //System.out.println(allergenToSetOfFoodsMap);
        processAllergens(allergenToSetOfFoodsMap);

        System.out.println(allergenToSetOfFoodsMap);

        List<String> ingredientsWithAllergen = allergenToSetOfFoodsMap.values()
                .stream()
                .map(list -> list.get(0).get(0))
                .collect(Collectors.toList());

        long sum = listOfFoods.stream()
                .map(food -> food.getIngredients().stream()
                        .filter(ingredient -> !ingredientsWithAllergen.contains(ingredient))
                        .count())
                .reduce(0L, (x, y) -> x + y);
        System.out.println(sum);

    }

    static void processAllergen(String allergen) {
        boolean hasChange;
        List<List<String>> setsOfIngredientsContainingAllergent = allergenToSetOfFoodsMap.get(allergen);
        do {
            hasChange = false;
            for (int i = 0; i < setsOfIngredientsContainingAllergent.size(); i++) {
                List<String> currentList = setsOfIngredientsContainingAllergent.get(i);
                for (int j = 0; j < setsOfIngredientsContainingAllergent.size(); j++) {
                    List<String> listToCheck = setsOfIngredientsContainingAllergent.get(j);
                    if (i == j) {
                        continue;
                    }
                    List<String> copy = List.copyOf(listToCheck);
                    for (String ingredient : copy) {
                        if (!currentList.contains(ingredient)) {
                            listToCheck.remove(ingredient);
                            hasChange = true;
                        }
                    }
                }
            }
        } while (hasChange);

        allergenToSetOfFoodsMap.put(allergen, setsOfIngredientsContainingAllergent.stream()
                .map(list -> new HashSet<>(list))
                .distinct()
                .map(set -> new ArrayList<>(set))
                .collect(Collectors.toList()));

    }

    static void processAllergens(Map<String, List<List<String>>> map) {
        Set<String> allergensIdentified = new HashSet<>();
        Optional<String> optionalAllergen = map.keySet().stream()
                .filter(key -> allergenToSetOfFoodsMap.get(key).get(0).size() == 1)
                .findAny();
        while (optionalAllergen.isPresent()) {
            String allergen = optionalAllergen.get();
            String ingredient = allergenToSetOfFoodsMap.get(allergen).get(0).get(0);
            //System.out.println("allergen : " + allergen + ", ingredient: " + ingredient);
            for (String otherAllergen : Set.copyOf(allergenToSetOfFoodsMap.keySet())) {
                if (otherAllergen.equals(allergen)) {
                    continue;
                }

                allergenToSetOfFoodsMap.get(otherAllergen).get(0).remove(ingredient);
            }
            allergensIdentified.add(allergen);
            optionalAllergen = map.keySet().stream()
                    .filter(key -> allergenToSetOfFoodsMap.get(key).get(0).size() == 1)
                    .filter(a -> !allergensIdentified.contains(a))
                    .findAny();
        }
    }

    static class Food {

        List<String> ingredients;
        List<String> allergens;

        Food(List<String> ingredients, List<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }

        Food(String foodDetails) {
            String[] details = foodDetails.split(" \\(contains ");
            ingredients = Arrays.asList(details[0].split(" "));
            allergens = Arrays.asList(details[1].substring(0, details[1].length() - 1).split(", "));
        }

        List<String> getIngredients() {
            return new ArrayList<>(List.copyOf(ingredients));
        }

        List<String> getAllergens() {
            return new ArrayList<>(List.copyOf(allergens));
        }

        public String toString() {
            return "ingredients: " + ingredients + "\nallergens: " + allergens;
        }

    }


}
