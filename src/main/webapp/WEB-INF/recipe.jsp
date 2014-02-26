<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/ajax.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/url.js"></script>
    <script src="js/dialog.js"></script>
    <script src="js/common.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
      <%@ include file="menu.jspf" %>
      <form id="form">
        <div>
          <table>
            <tr>
              <td>name:</td>
              <td><input id="name" name="name"/></td>
            </tr>
            <tr>
              <td>servings:</td>
              <td><input id="serves" name="serves"/></td>
            </tr>
          </table>
        </div>
        <div>
          <table id="data">
            <thead>
              <tr>
                <th>
                  &nbsp;
                </th>
                <th>
                  ingredient
                </th>
                <th>
                  amount
                </th>
                <th>
                  unit
                </th>
                <th>
                  cost
                </th>
              </tr>
            </thead>
          </table>
        </div>
        <div>
          <table>
            <tr>
              <td>
                total cost:
              </td>
              <td>
                <label id="total_cost"></label>
              </td>
            </tr>
            <tr>
              <td>
                serving cost:
              </td>
              <td>
                <label id="serving_cost"></label>
              </td>
            </tr>
          </table>
        </div>
        <div>
          <table>
            <tr>
              <td>
                <input id="save" type="button" value="Save" />
              </td>
              <td>
                <input id="add" type="button" value="Add" />
              </td>
              <td>
                <input id="delete" type="button" value="Delete" />
              </td>
            </tr>
          </table>
        </div>
      </form>
    </div>
    <script>
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

        var ingredientSelect = $("<select id='ingredient" + index + "' name='ingredient" + index + "' />");
        $(ingredientSelect).append("<option value=''>-- select --</option>");
        $.each(ingredients, function(index, ingredient) {
          var selected;
          selected = (ingredient.id === recipeIngredient.ingredient.id) ? "selected" : "";
          $(ingredientSelect).append("<option value='" + ingredient.id + "' " + selected + ">" + ingredient.name + "</option>");
        });

        var unitSelect = $("<select id='unit" + index + "' name='unit" + index + "' />");
        $(unitSelect).append("<option value=''>-- select --</option>");
        $.each(units, function(index, unit) {
          var selected;
          selected = (unit.id === recipeIngredient.unit.id) ? "selected" : "";
          $(unitSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>");
        });

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' name='id" + index + "' type='checkbox' value='" + recipeIngredient.id + "'/>"));
        $(row).append($("<td/>").append(ingredientSelect));
        $(row).append($("<td/>").append("<input id='amount" + index + "' name='amount" + index + "' value='" + recipeIngredient.amount + "' />"));
        $(row).append($("<td/>").append(unitSelect));
        $(row).append($("<td/>").append("<label id='cost" + index + "' name='cost" + index + "' >?</label>"));

        $("#data").append(row);

        $("#ingredient" + index).bind("change", function() {
          recipeIngredient._changed = true;
          recipeIngredient.ingredient.id = $(this).val();
          updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
          updateTotalCost(recipe);
          updateServingCost(recipe);
        });

        $("#ingredient" + index).rules('add', {
          required: true,
        });

        $("#amount" + index).bind("input", function() {
          recipeIngredient._changed = true;
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
          recipeIngredient._changed = true;
          recipeIngredient.unit.id = $(this).val();
          updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
          updateTotalCost(recipe);
          updateServingCost(recipe);
        });

        $("#unit" + index).rules('add', {
          required: true,
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
        $("#cost" + index).text(recipeIngredient._cost.toFixed(2));
      }

      function updateTotalCost(recipe) {
        recipe._totalCost = calculateTotalCost(recipe.recipeIngredients);
        $("#total_cost").text(recipe._totalCost.toFixed(2));
      }

      function updateServingCost(recipe) {
        recipe._servingCost = calculateServingCost(recipe._totalCost, recipe.serves);
        $("#serving_cost").text(recipe._servingCost.toFixed(2));
      }

      function calculateCost(recipeIngredient, ratioLookup, ingredientLookup) {

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

        $("#form").validate({
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
        var recipeIngredientsLookup;

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

          ingredientsLookup = lookup(ingredients);
          recipeIngredientsLookup = lookup(recipe.recipeIngredients)

          setName(recipe);
          setServes(recipe);
          addRows(recipe, ingredients, units, ratiosLookup, ingredientsLookup);
          updateCosts(recipe, ratiosLookup, ingredientsLookup);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {

          var form = $("#form");
          form.validate();

          if (!form.valid()) {
            return false;
          }

          $.when(save("rest/recipe", recipe)).done(function(data) {
            dialog(data.message);

            recipe = data.recipe;
            recipeIngredientsLookup = lookup(recipe.recipeIngredients)

            setName(recipe);
            setServes(recipe);
            addRows(recipe, ingredients, units, ratiosLookup, ingredientsLookup);
            updateCosts(recipe, ratiosLookup, ingredientsLookup);
          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(recipe, recipe.recipeIngredients, ingredients, units, ratiosLookup, ingredientsLookup);
        });

        $("#delete").click(function() {
          del(recipeIngredientsLookup);
          updateCosts(recipe, ratiosLookup, ingredientsLookup);
        });
      });
    </script>
  </body>
</html>
