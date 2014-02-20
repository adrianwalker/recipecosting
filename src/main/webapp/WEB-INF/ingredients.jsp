<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/ajax.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
      <%@ include file="menu.jspf" %>
      <div>
        <table id="data">
          <thead>
            <tr>
              <th>
                &nbsp;
              </th>
              <th>
                name
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
      function addRows(ingredients, units) {

        $('#data tbody').remove();

        $.each(ingredients, function(index, ingredient) {
          addRow(index, ingredient, units);
        });
      }

      function addRow(index, ingredient, units) {

        var unitSelect = $("<select id='unit" + index + "' />");
        $(unitSelect).append("<option>-- select --</option>");
        $.each(units, function(index, unit) {
          var selected;
          selected = (unit.id === ingredient.unit.id) ? "selected" : "";
          $(unitSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>");
        });

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' type='checkbox' value='" + ingredient.id + "'/>"));
        $(row).append($("<td/>").append("<input id='name" + index + "' value='" + ingredient.name + "' />"));
        $(row).append($("<td/>").append("<input id='amount" + index + "' value='" + ingredient.amount + "' />"));
        $(row).append($("<td/>").append(unitSelect));
        $(row).append($("<td/>").append("<input id='cost" + index + "' value='" + ingredient.cost + "' />"));

        $("#data").append(row);

        $("#name" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.name = $(this).val();
        });

        $("#amount" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.amount = $(this).val();
        });

        $("#unit" + index).bind("change", function() {
          ingredient._changed = true;
          ingredient.unit.id = $(this).val();
        });

        $("#cost" + index).bind("input", function() {
          ingredient._changed = true;
          ingredient.cost = $(this).val();
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
        var ingredients = read("rest/ingredient");
        var units = read("rest/unit");

        $.when(ingredients, units).done(function(data1, data2) {
          ingredients = data1[0].ingredients;
          units = data2[0].units;
          addRows(ingredients, units);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {
          $.when(save("rest/ingredient", ingredients)).done(function(data) {
            dialog(data.message);

            ingredients = read("rest/ingredient");

            $.when(ingredients).done(function(data) {
              ingredients = data.ingredients;
              addRows(ingredients, units);
            }).fail(function() {
              error();
            });

          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(ingredients, units);
        });

        $("#delete").click(function() {

          var ids = [];

          $('input[id^=id]:checked').each(function() {

            var value = $(this).val();
            if (value !== 'null') {
              ids.push(value);
            }
          });

          $.when(del("rest/ingredient", ids)).done(function(data) {
            dialog(data.message);

            ingredients = read("rest/ingredient");

            $.when(ingredients).done(function(data) {
              ingredients = data.ingredients;
              addRows(ingredients, units);
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
