package org.adrianwalker.recipecosting.server.util;

import java.math.BigDecimal;
import org.adrianwalker.recipecosting.common.entity.Unit;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.RecipeCostingEntityResourceDelegate;

public final class UserData {

  private RecipeCostingEntityResourceDelegate<Unit> unitsDelegate;
  private RecipeCostingEntityResourceDelegate<UnitConversion> unitConversionsDelegate;

  public UserData(
          final RecipeCostingEntityResourceDelegate<Unit> unitsDelegate,
          final RecipeCostingEntityResourceDelegate<UnitConversion> unitConversionsDelegate) {
    this.unitsDelegate = unitsDelegate;
    this.unitConversionsDelegate = unitConversionsDelegate;
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

  private void createDefaultDataForUser(final User user) throws Exception {
    // Units

    // weights
    Unit kilogram = createUnit(user, "kilogram (kg)");
    Unit pound = createUnit(user, "pound (lb)");
    Unit ounce = createUnit(user, "ounce (oz)");
    Unit gram = createUnit(user, "gram (g)");
    // volumes
    Unit quart = createUnit(user, "quart (qt)");
    Unit litre = createUnit(user, "litre (L)");
    Unit pint = createUnit(user, "pint (pt)");
    Unit fluidOunce = createUnit(user, "fluid ounce (fl oz)");
    Unit tablespoon = createUnit(user, "tablespoon (tbsp)");
    Unit teaspoon = createUnit(user, "teaspoon (tsp)");
    Unit millilitre = createUnit(user, "millilitre (ml)");
    // each
    Unit each = createUnit(user, "each");

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
    UnitConversion pintToTablespoon = createUnitConversion(user, pint, tablespoon, "32");
    UnitConversion pintToTeaspoon = createUnitConversion(user, pint, teaspoon, "96");

    // fluid ounce
    UnitConversion fluidOunceToTablespoon = createUnitConversion(user, fluidOunce, tablespoon, "1.6");
    UnitConversion fluidOunceToTeaspoon = createUnitConversion(user, fluidOunce, tablespoon, "4.8");

    // tablespoon
    UnitConversion fluidTablespoonToTeaspoon = createUnitConversion(user, tablespoon, teaspoon, "3");
  }
}
