<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/ajax.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
    <script src="js/url.js"></script>
  </head>
  <body>
    <div>
      <%@ include file="menu.jspf" %> 
      <div>
        <table>
          <tr>
            <td>name:</td>
            <td><input id="name"/></td>
          </tr>
          <tr>
            <td>servings:</td>
            <td><input id="serves"/></td>
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
    </div>
    <script>
      function setName(recipe) {
        $('#name').val(recipe.name);
        $("#name").bind("input", function() {
          recipe.changed = true;
          recipe.name = $(this).val();
        });
      }

      function setServes(recipe) {
        $('#serves').val(recipe.serves);
        $("#serves").bind("input", function() {
          recipe.changed = true;
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

        var ingredientSelect = $("<select id='ingredient" + index + "' />");
        $(ingredientSelect).append("<option>-- select --</option>");
        $.each(ingredients, function(index, ingredient) {
          var selected;
          selected = (ingredient.id === recipeIngredient.ingredient.id) ? "selected" : "";
          $(ingredientSelect).append("<option value='" + ingredient.id + "' " + selected + ">" + ingredient.name + "</option>");
        });

        var unitSelect = $("<select id='unit" + index + "' />");
        $(unitSelect).append("<option>-- select --</option>");
        $.each(units, function(index, unit) {
          var selected;
          selected = (unit.id === recipeIngredient.unit.id) ? "selected" : "";
          $(unitSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>");
        });

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' type='checkbox' value='" + recipeIngredient.id + "'/>"));
        $(row).append($("<td/>").append(ingredientSelect));
        $(row).append($("<td/>").append("<input id='amount" + index + "' value='" + recipeIngredient.amount + "' />"));
        $(row).append($("<td/>").append(unitSelect));
        $(row).append($("<td/>").append("<label id='cost" + index + "' >?</label>"));

        $("#data").append(row);

        $("#ingredient" + index).bind("change", function() {
          recipeIngredient.changed = true;
          recipeIngredient.ingredient.id = $(this).val();
          updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
          updateTotalCost(recipe);
          updateServingCost(recipe);
        });

        $("#amount" + index).bind("input", function() {
          recipeIngredient.changed = true;
          recipeIngredient.amount = $(this).val();
          updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
          updateTotalCost(recipe);
          updateServingCost(recipe);
        });

        $("#unit" + index).bind("change", function() {
          recipeIngredient.changed = true;
          recipeIngredient.unit.id = $(this).val();
          updateCost(index, recipeIngredient, ratioLookup, ingredientLookup);
          updateTotalCost(recipe);
          updateServingCost(recipe);
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
        recipeIngredient.cost = calculateCost(recipeIngredient, ratioLookup, ingredientLookup);
        $("#cost" + index).text(recipeIngredient.cost.toFixed(2));
      }

      function updateTotalCost(recipe) {
        recipe.totalCost = calculateTotalCost(recipe.recipeIngredients);
        $("#total_cost").text(recipe.totalCost.toFixed(2));
      }

      function updateServingCost(recipe) {
        recipe.servingCost = calculateServingCost(recipe.totalCost, recipe.serves);
        $("#serving_cost").text(recipe.servingCost.toFixed(2));
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
          totalCost += recipeIngredient.cost;
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
          cost: 0
        };
        addRow(recipeIngredients.length, recipe, recipeIngredient, ingredients, units, ratioLookup, ingredientLookup);
        recipeIngredients.push(recipeIngredient);
      }

      $(function() {

        var params = getUrlParams();
        var recipe;
        if (params.id === undefined) {
          recipe = [{
              id: null,
              name: "",
              serves: 1,
              recipeIngredients: [],
              totalCost: 0,
              servingCost: 0
            }];
        } else {
          recipe = read("rest/recipe/" + params.id);
        }
        var ingredients = read("rest/ingredient");
        var units = read("rest/unit");
        var unitConversions = read("rest/unitconversion");

        var ratioLookup = {};
        var ingredientLookup = {};

        $.when(recipe, ingredients, units, unitConversions).done(function(data1, data2, data3, data4) {

          recipe = data1[0];
          ingredients = data2[0].ingredients;
          units = data3[0].units;
          unitConversions = data4[0].unitConversions;

          $.each(unitConversions, function(index, unitConversion) {
            var fromId = unitConversion.unitFrom.id;
            var toId = unitConversion.unitTo.id;
            var ratio = unitConversion.ratio;

            ratioLookup[fromId + ":" + toId] = ratio;
            if (ratio !== 0) {
              ratioLookup[toId + ":" + fromId] = 1 / ratio;
            }
          });

          $.each(ingredients, function(index, ingredient) {
            ingredientLookup[ingredient.id] = ingredient;
          });

          setName(recipe);
          setServes(recipe);
          addRows(recipe, ingredients, units, ratioLookup, ingredientLookup);
          updateCosts(recipe, ratioLookup, ingredientLookup);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {
          $.when(save("rest/recipe", recipe)).done(function(data) {
            alert(data.message);

            recipe = data.recipe;

            setName(recipe);
            setServes(recipe);
            addRows(recipe, ingredients, units, ratioLookup, ingredientLookup);
            updateCosts(recipe, ratioLookup, ingredientLookup);
          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(recipe, recipe.recipeIngredients, ingredients, units, ratioLookup, ingredientLookup);
        });

        $("#delete").click(function() {

          var ids = [];

          $('input[id^=id]:checked').each(function() {

            var value = $(this).val();
            if (value !== 'null') {
              ids.push(value);
            }
          });

          $.when(del("rest/recipe/ingredient", ids)).done(function(data) {
            alert(data.message);

            recipe = read("rest/recipe/" + params.id);

            $.when(recipe).done(function(data) {

              recipe = data;

              setName(recipe);
              setServes(recipe);
              addRows(recipe, ingredients, units, ratioLookup, ingredientLookup);
              updateCosts(recipe, ratioLookup, ingredientLookup);
            }).fail(function() {
              error();
            });

          }).fail(function() {
            error();
          });
        });
      });
    </script>
  </body>
</html>
