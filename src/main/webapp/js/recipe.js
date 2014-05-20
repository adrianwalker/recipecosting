function setName(recipe) {
  $('#name').val(recipe.name);
  $("#name").bind("input", function() {
    recipe._changed = true;
    recipe.name = $(this).val();
  });
}

function setServes(recipe) {
  $('#serves').val(recipe.serves);
  $("#serves").bind("input", function() {
    recipe._changed = true;
    recipe.serves = $(this).val();
    updateServingCost(recipe);
  });
}

function addRows(recipe, ingredients, units, ratioLookup, ingredientLookup) {

  $('#data tbody').remove();

  $.each(recipe.recipeIngredients, function(index, recipeIngredient) {
    addRow(index, recipe, recipeIngredient, ingredients, units, ratioLookup, ingredientLookup);
  });
}

function addRow(index, recipe, recipeIngredient, ingredients, units, ratioLookup, ingredientLookup) {

  var ingredientSelect = $("<select id='ingredient" + index + "' name='ingredient" + index + "' class='form-control'/>");
  $(ingredientSelect).append("<option value=''>-- select --</option>");
  $.each(ingredients, function(index, ingredient) {
    var selected;
    selected = (ingredient.id === recipeIngredient.ingredient.id) ? "selected" : "";
    $(ingredientSelect).append("<option value='" + ingredient.id + "' " + selected + ">" + ingredient.name + " (" + ingredient.unit.name + ")</option>");
  });

  var unitSelect = $("<select id='unit" + index + "' name='unit" + index + "' class='form-control'/>");
  $(unitSelect).append("<option value=''>-- select --</option>");
  $.each(units, function(index, unit) {
    var selected;
    selected = (unit.id === recipeIngredient.unit.id) ? "selected" : "";
    $(unitSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>");
  });

  var row = $("<tr id='row" + index + "'/>");
  $(row).append($("<td/>").append("<input id='id" + index + "' name='id" + index + "' type='checkbox' value='" + recipeIngredient.id + "'/>"));
  $(row).append($("<td/>").append(ingredientSelect));
  $(row).append($("<td/>").append("<input id='amount" + index + "' name='amount" + index + "' value='" + recipeIngredient.amount + "'  class='form-control text-right' type='number' required/>"));
  $(row).append($("<td/>").append(unitSelect));
  $(row).append($("<td class='text-right'/>").append("<label id='cost" + index + "' name='cost" + index + "'>?</label>"));

  $("#data").append(row);

  $("#id" + index).bind("change", function() {
    recipeIngredient._checked = this.checked;
  });

  $("#ingredient" + index).bind("change", function() {
    recipe._changed = true;
    recipeIngredient.ingredient.id = $(this).val();
    updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
    updateTotalCost(recipe);
    updateServingCost(recipe);
  });

  $("#ingredient" + index).rules('add', {
    required: true,
  });

  $("#amount" + index).bind("input", function() {
    recipe._changed = true;
    recipeIngredient.amount = $(this).val();
    updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
    updateTotalCost(recipe);
    updateServingCost(recipe);
  });

  $("#amount" + index).rules('add', {
    required: true,
    number: true,
    minlength: 1,
    maxlength: 20
  });

  $("#unit" + index).bind("change", function() {
    recipe._changed = true;
    recipeIngredient.unit.id = $(this).val();
    updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
    updateTotalCost(recipe);
    updateServingCost(recipe);
  });

  $("#unit" + index).rules('add', {
    required: true
  });
}

function updateCosts(recipe, ratioLookup, ingredientLookup) {
  $.each(recipe.recipeIngredients, function(index, recipeIngredient) {
    updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
  });
  updateTotalCost(recipe);
  updateServingCost(recipe);
}

function updateCost(index, recipeIngredient, ratioLookup, ingredientLookup) {
  recipeIngredient._cost = calculateCost(recipeIngredient, ratioLookup, ingredientLookup);
  if (isNaN(recipeIngredient._cost)) {
    $("#cost" + index).text("#ERROR");
  } else {
    $("#cost" + index).text(recipeIngredient._cost.toFixed(2));
  }
}

function updateTotalCost(recipe) {
  recipe._totalCost = calculateTotalCost(recipe.recipeIngredients);
  if (isNaN(recipe._totalCost)) {
    $("#total_cost").text("#ERROR");
  } else {
    $("#total_cost").text(recipe._totalCost.toFixed(2));
  }
}

function updateServingCost(recipe) {
  recipe._servingCost = calculateServingCost(recipe._totalCost, recipe.serves);
  if (isNaN(recipe._servingCost)) {
    $("#serving_cost").text("#ERROR");
  } else {
    $("#serving_cost").text(recipe._servingCost.toFixed(2));
  }
}

function calculateCost(recipeIngredient, ratioLookup, ingredientLookup) {

  if (recipeIngredient._delete) {
    return 0;
  }

  var ingredient = ingredientLookup[recipeIngredient.ingredient.id];
  var ingredientUnitId = ingredient.unit.id;
  var ingredientAmount = ingredient.amount;
  var ingredientCost = ingredient.cost;

  var unitId = recipeIngredient.unit.id;
  var amount = recipeIngredient.amount;

  var ratio;
  if (ingredientUnitId == unitId) {
    ratio = 1;
  } else {
    ratio = ratioLookup[unitId + ":" + ingredientUnitId];
  }

  var cost = ratio * (amount * (ingredientCost / ingredientAmount));
  cost = Math.ceil(cost * 100) / 100;

  return cost;
}

function calculateTotalCost(recipeIngredients) {

  var totalCost = 0;
  $.each(recipeIngredients, function(index, recipeIngredient) {
    totalCost += recipeIngredient._cost;
  });

  return totalCost;
}

function calculateServingCost(totalCost, servings) {

  var servingCost = totalCost / servings;
  servingCost = Math.ceil(servingCost * 100) / 100;

  return servingCost;
}

function add(recipe, recipeIngredients, ingredients, units, ratioLookup, ingredientLookup) {

  var recipeIngredient = {
    id: null,
    ingredient: {id: null},
    amount: 0.0,
    unit: {id: null},
    _cost: 0
  };
  addRow(recipeIngredients.length, recipe, recipeIngredient, ingredients, units, ratioLookup, ingredientLookup);
  recipeIngredients.push(recipeIngredient);
}

$(function() {

  var form = $("#form");
  form.validate({
    rules: {
      name: {
        required: true,
        minlength: 1,
        maxlength: 1000
      },
      serves: {
        required: true,
        digits: true,
        min: 1,
        minlength: 1,
        maxlength: 20
      }
    }
  });

  var params = getUrlParams();
  var recipe;
  if (params.id === undefined) {
    recipe = [{
        id: null,
        name: "",
        serves: 1,
        recipeIngredients: [],
        _totalCost: 0,
        _servingCost: 0
      }];
  } else {
    recipe = read("rest/recipe/" + params.id);
  }
  var ingredients = read("rest/ingredient");
  var units = read("rest/unit");
  var unitConversions = read("rest/unitconversion");

  var ratiosLookup;
  var ingredientsLookup;

  $.when(recipe, ingredients, units, unitConversions).done(function(data1, data2, data3, data4) {

    recipe = data1[0];
    ingredients = data2[0].ingredients;
    units = data3[0].units;
    unitConversions = data4[0].unitConversions;

    ratiosLookup = {};
    $.each(unitConversions, function(index, unitConversion) {
      var fromId = unitConversion.unitFrom.id;
      var toId = unitConversion.unitTo.id;
      var ratio = unitConversion.ratio;

      ratiosLookup[fromId + ":" + toId] = ratio;
      if (ratio !== 0) {
        ratiosLookup[toId + ":" + fromId] = 1 / ratio;
      }
    });

    ingredientsLookup = {};
    $.each(ingredients, function(index, ingredient) {
      ingredientsLookup[ingredient.id] = ingredient;
    });

    setName(recipe);
    setServes(recipe);
    addRows(recipe, ingredients, units, ratiosLookup, ingredientsLookup);
    updateCosts(recipe, ratiosLookup, ingredientsLookup);
  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });

  $("#save").click(function() {

    if (!form.valid()) {
      return false;
    }

    $.when(save("rest/recipe", [recipe].concat(recipe.recipeIngredients))).done(function(data) {
      dialog("Recipe", data.message, data.messages);
      if (data.saved) {

        recipe = data.recipe;

        setName(recipe);
        setServes(recipe);
        addRows(recipe, ingredients, units, ratiosLookup, ingredientsLookup);
        updateCosts(recipe, ratiosLookup, ingredientsLookup);
      }
    }).fail(function(xhr, status, error) {
      dialog("Error", error);
    });
  });

  $("#add").click(function() {
    add(recipe, recipe.recipeIngredients, ingredients, units, ratiosLookup, ingredientsLookup);
    recipe._changed = true;
  });

  $("#delete").click(function() {
    remove(recipe.recipeIngredients);
    recipe._changed = true;

    updateCosts(recipe, ratiosLookup, ingredientsLookup);
  });
});
