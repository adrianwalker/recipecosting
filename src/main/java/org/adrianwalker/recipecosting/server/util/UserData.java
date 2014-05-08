package org.adrianwalker.recipecosting.server.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.adrianwalker.recipecosting.common.entity.Ingredient;
import org.adrianwalker.recipecosting.common.entity.Recipe;
import org.adrianwalker.recipecosting.common.entity.RecipeIngredient;
import org.adrianwalker.recipecosting.common.entity.Unit;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.RecipeCostingEntityResourceDelegate;

public final class UserData {

  private RecipeCostingEntityResourceDelegate<Unit> unitsDelegate;
  private RecipeCostingEntityResourceDelegate<UnitConversion> unitConversionsDelegate;
  private RecipeCostingEntityResourceDelegate<Ingredient> ingredientsDelegate;
  private RecipeCostingEntityResourceDelegate<Recipe> recipesDelegate;

  public UserData(
          final RecipeCostingEntityResourceDelegate<Unit> unitsDelegate,
          final RecipeCostingEntityResourceDelegate<UnitConversion> unitConversionsDelegate,
          final RecipeCostingEntityResourceDelegate<Ingredient> ingredientsDelegate,
          final RecipeCostingEntityResourceDelegate<Recipe> recipesDelegate) {

    this.unitsDelegate = unitsDelegate;
    this.unitConversionsDelegate = unitConversionsDelegate;
    this.ingredientsDelegate = ingredientsDelegate;
    this.recipesDelegate = recipesDelegate;
  }

  private Unit createUnit(final User user, final String name) throws Exception {
    Unit unit = new Unit();
    unit.setUser(user);
    unit.setName(name);

    return unitsDelegate.update(unit);
  }

  private UnitConversion createUnitConversion(final User user, final Unit from, final Unit to, final String ratio) throws Exception {
    UnitConversion unitConversion = new UnitConversion();
    unitConversion.setUser(user);
    unitConversion.setUnitFrom(from);
    unitConversion.setUnitTo(to);
    unitConversion.setRatio(new BigDecimal(ratio));

    return unitConversionsDelegate.update(unitConversion);
  }

  private Ingredient createIngredient(final User user, final String name, final String amount, final Unit unit, final String cost) throws Exception {
    Ingredient selfRasingFlour = new Ingredient();
    selfRasingFlour.setName(name);
    selfRasingFlour.setAmount(new BigDecimal(amount));
    selfRasingFlour.setUnit(unit);
    selfRasingFlour.setCost(new BigDecimal(cost));
    selfRasingFlour.setUser(user);

    return ingredientsDelegate.update(selfRasingFlour);
  }

  private RecipeIngredient createRecipeIngredient(final User user, final Ingredient ingredient, final String amount, final Unit unit) {
    RecipeIngredient recipeIngredient = new RecipeIngredient();
    recipeIngredient.setIngredient(ingredient);
    recipeIngredient.setAmount(new BigDecimal(amount));
    recipeIngredient.setUnit(unit);
    recipeIngredient.setUser(user);

    return recipeIngredient;
  }

  private Recipe createRecipe(final User user, final String name, final int serves, final List<RecipeIngredient> ingredients) throws Exception {
    Recipe recipe = new Recipe();
    recipe.setName(name);
    recipe.setServes(serves);

    for (RecipeIngredient recipeIngredient : ingredients) {
      recipeIngredient.setRecipe(recipe);
    }

    recipe.setRecipeIngredients(ingredients);
    recipe.setUser(user);

    return recipesDelegate.update(recipe);
  }

  public void createDefaultDataForUser(final User user) throws Exception {
    // Units

    // weights
    Unit kilogram = createUnit(user, "kg");
    Unit pound = createUnit(user, "lb");
    Unit ounce = createUnit(user, "oz");
    Unit gram = createUnit(user, "g");
    // volumes
    Unit quart = createUnit(user, "qt");
    Unit litre = createUnit(user, "L");
    Unit pint = createUnit(user, "pt");
    Unit fluidOunce = createUnit(user, "fl oz");
    Unit tablespoon = createUnit(user, "tbsp");
    Unit teaspoon = createUnit(user, "tsp");
    Unit millilitre = createUnit(user, "ml");
    // each
    Unit each = createUnit(user, "each");
    Unit slice = createUnit(user, "slice");

    // Unit Conversions

    // weights

    // kilogram
    UnitConversion kilogramToGram = createUnitConversion(user, kilogram, gram, "1000");
    UnitConversion kilogramToPound = createUnitConversion(user, kilogram, pound, "2.20462");
    UnitConversion kilogramToOunce = createUnitConversion(user, kilogram, ounce, "35.274");
    // pound
    UnitConversion poundToOunce = createUnitConversion(user, pound, ounce, "16");
    UnitConversion poundToGram = createUnitConversion(user, pound, gram, "453.592");
    //ounces
    UnitConversion ounceToGram = createUnitConversion(user, ounce, gram, "28.3495");

    // volumes

    // quart
    UnitConversion quartToLitre = createUnitConversion(user, quart, litre, "1.13652");
    UnitConversion quartToPint = createUnitConversion(user, quart, pint, "2");
    UnitConversion quartToFluidOunce = createUnitConversion(user, quart, fluidOunce, "40");

    // litre
    UnitConversion litreToMillilitre = createUnitConversion(user, litre, millilitre, "1000");
    UnitConversion litreToPint = createUnitConversion(user, litre, pint, "1.75975");
    UnitConversion litreToFluidOunce = createUnitConversion(user, litre, fluidOunce, "35.1951");
    UnitConversion litreToTablespoon = createUnitConversion(user, litre, tablespoon, "56.3121");
    UnitConversion litreToTeaspoon = createUnitConversion(user, litre, teaspoon, "168.936");

    // pint
    UnitConversion pintToFluidOunce = createUnitConversion(user, pint, fluidOunce, "20");
    UnitConversion pintToMillitre = createUnitConversion(user, pint, millilitre, "568.261485");
    UnitConversion pintToTablespoon = createUnitConversion(user, pint, tablespoon, "32");
    UnitConversion pintToTeaspoon = createUnitConversion(user, pint, teaspoon, "96");

    // fluid ounce
    UnitConversion fluidOunceToTablespoon = createUnitConversion(user, fluidOunce, tablespoon, "1.6");
    UnitConversion fluidOunceToTeaspoon = createUnitConversion(user, fluidOunce, tablespoon, "4.8");

    // tablespoon
    UnitConversion tablespoonToTeaspoon = createUnitConversion(user, tablespoon, teaspoon, "3");
    UnitConversion tablespoonToMillilitre = createUnitConversion(user, tablespoon, millilitre, "15");
    UnitConversion tablespoonToLitre = createUnitConversion(user, tablespoon, litre, "0.015");
    UnitConversion tablespoonToGram = createUnitConversion(user, tablespoon, gram, "15");
    UnitConversion tablespoonToKilogram = createUnitConversion(user, tablespoon, kilogram, "0.015");    
    
    // teaspoon
    UnitConversion teaspoonToMillilitre = createUnitConversion(user, teaspoon, millilitre, "5");
    UnitConversion teaspoonToLitre = createUnitConversion(user, teaspoon, litre, "0.005");
    UnitConversion teaspoonToGram = createUnitConversion(user, teaspoon, gram, "5");
    UnitConversion teaspoonToKilogram = createUnitConversion(user, teaspoon, kilogram, "0.005");

    // Ingredients

    Ingredient selfRasingFlour = createIngredient(user, "self-raising flour", "1.5", kilogram, "0.45");
    Ingredient salt = createIngredient(user, "salt", "750", gram, "0.29");
    Ingredient bakingPowder = createIngredient(user, "baking powder", "170", gram, "1.08");
    Ingredient butter = createIngredient(user, "butter", "250", gram, "0.98");
    Ingredient casterSugar = createIngredient(user, "caster sugar", "1", kilogram, "1.48");
    Ingredient milk = createIngredient(user, "milk", "4", pint, "1.00");
    Ingredient vanillaExtract = createIngredient(user, "vanilla extract", "75", millilitre, "2.98");
    Ingredient eggs = createIngredient(user, "eggs", "6", each, "1.00");
    Ingredient jam = createIngredient(user, "jam", "430", gram, "1.00");
    Ingredient clottedCream = createIngredient(user, "clotted cream", "250", millilitre, "1.30");

    // Recipe Ingredients

    RecipeIngredient selfRaisingFlourIngredient = createRecipeIngredient(user, selfRasingFlour, "350", gram);
    RecipeIngredient saltIngredient = createRecipeIngredient(user, salt, "0.25", teaspoon);
    RecipeIngredient bakingPowderIngredient = createRecipeIngredient(user, bakingPowder, "1", teaspoon);
    RecipeIngredient butterIngredient = createRecipeIngredient(user, butter, "85", gram);
    RecipeIngredient casterSugarIngredient = createRecipeIngredient(user, casterSugar, "3", tablespoon);
    RecipeIngredient milkIngredient = createRecipeIngredient(user, milk, "175", millilitre);
    RecipeIngredient vanillaExtractIngredient = createRecipeIngredient(user, vanillaExtract, "1", teaspoon);
    RecipeIngredient eggsIngredient = createRecipeIngredient(user, eggs, "1", each);
    RecipeIngredient jamIngredient = createRecipeIngredient(user, jam, "8", teaspoon);
    RecipeIngredient clottedCreamIngredient = createRecipeIngredient(user, clottedCream, "8", tablespoon);

    // Recipe

    List<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();
    recipeIngredients.add(selfRaisingFlourIngredient);
    recipeIngredients.add(saltIngredient);
    recipeIngredients.add(bakingPowderIngredient);
    recipeIngredients.add(butterIngredient);
    recipeIngredients.add(casterSugarIngredient);
    recipeIngredients.add(milkIngredient);
    recipeIngredients.add(vanillaExtractIngredient);
    recipeIngredients.add(eggsIngredient);
    recipeIngredients.add(jamIngredient);
    recipeIngredients.add(clottedCreamIngredient);

    Recipe scones = createRecipe(user, "Classic scones with jam & clotted cream (example recipe)", 8, recipeIngredients);
  }
}
