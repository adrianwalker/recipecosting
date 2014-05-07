<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <h2>Ingredients</h2>

      <form id="form">
        <div class="panel panel-default">
          <div class="panel-body">
            <div>
              <table id="data" class="table">
                <thead>
                  <tr>
                    <th>
                      &nbsp;
                    </th>
                    <th>
                      Name
                    </th>
                    <th>
                      Amount
                    </th>
                    <th>
                      Unit
                    </th>
                    <th>
                      Cost
                    </th>
                  </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
        <button id="save" type="button" class="btn btn-primary">Save Ingredients</button>
        <button id="add" type="button" class="btn btn-primary">Add Ingredient</button>
        <button id="delete" type="button" class="btn btn-primary">Delete Ingredients</button>
      </form>
    </div>
    <script>
      function addRows(ingredients, units) {

        $('#data tbody').remove();

        $.each(ingredients, function(index, ingredient) {
          addRow(index, ingredient, units);
        });
      }

      function addRow(index, ingredient, units) {

        var unitSelect = $("<select id='unit" + index + "' name='unit" + index + "' class='form-control'/>");
        $(unitSelect).append("<option value=''>-- select --</option>");
        $.each(units, function(index, unit) {
          var selected;
          selected = (unit.id === ingredient.unit.id) ? "selected" : "";
          $(unitSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>");
        });

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' name='id" + index + "' type='checkbox' value='" + ingredient.id + "' />"));
        $(row).append($("<td/>").append("<input id='name" + index + "' name='name" + index + "' value='" + ingredient.name + "' class='form-control' required/>"));
        $(row).append($("<td/>").append("<input id='amount" + index + "' name='amount" + index + "' value='" + ingredient.amount + "' class='form-control text-right' type='number' required/>"));
        $(row).append($("<td/>").append(unitSelect));
        $(row).append($("<td/>").append("<input id='cost" + index + "' name='cost" + index + "' value='" + ingredient.cost + "' class='form-control text-right' type='number' required/>"));

        $("#data").append(row);

        $("#name" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.name = $(this).val();
        });

        $("#name" + index).rules('add', {
          required: true,
          minlength: 1,
          maxlength: 1000
        });

        $("#amount" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.amount = $(this).val();
        });

        $("#amount" + index).rules('add', {
          required: true,
          number: true,
          minlength: 1,
          maxlength: 20
        });

        $("#unit" + index).bind("change", function() {
          ingredient._changed = true;
          ingredient.unit.id = $(this).val();
        });

        $("#unit" + index).rules('add', {
          required: true,
        });

        $("#cost" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.cost = $(this).val();
        });

        $("#cost" + index).rules('add', {
          required: true,
          number: true,
          minlength: 1,
          maxlength: 20
        });
      }

      function add(ingredients, units) {
        var ingredient = {
          id: null,
          name: "",
          amount: 0.0,
          unit: {id: null},
          cost: 0.0
        };
        addRow(ingredients.length, ingredient, units);
        ingredients.push(ingredient);
      }

      $(function() {

        $("#form").validate();

        var ingredients = read("rest/ingredient");
        var units = read("rest/unit");
        var ingredientsLookup;

        $.when(ingredients, units).done(function(data1, data2) {
          ingredients = data1[0].ingredients;
          units = data2[0].units;
          ingredientsLookup = lookup(ingredients);
          addRows(ingredients, units);
        }).fail(function(xhr, status, error) {
          dialog("Error", error);
        });

        $("#save").click(function() {

          var form = $("#form");
          form.validate();

          if (!form.valid()) {
            return false;
          }

          $.when(save("rest/ingredient", ingredients)).done(function(data) {
            dialog("Ingredients", data.message);
            ingredients = data.ingredients;
            ingredientsLookup = lookup(ingredients);
            addRows(ingredients, units);
          }).fail(function(xhr, status, error) {
            dialog("Error", error);
          });
        });

        $("#add").click(function() {
          add(ingredients, units);
        });

        $("#delete").click(function() {
          remove(ingredientsLookup);
        });
      });
    </script>
  </body>
</html>
