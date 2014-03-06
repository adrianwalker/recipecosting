<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/ajax.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/dialog.js"></script>
    <script src="js/common.js"></script>
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
                recipe
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
      function addRows(recipes) {

        $('#data tbody').remove();

        $.each(recipes, function(index, recipe) {
          addRow(index, recipe);
        });
      }

      function addRow(index, recipe) {

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' type='checkbox' value='" + recipe.id + "'/>"));
        $(row).append($("<td/>").append("<a href='recipe.html?id=" + recipe.id + "' id='name" + index + "' >" + recipe.name + "</a>"));

        $("#data").append(row);
      }

      $(function() {
        var recipes = read("rest/recipe");
        var recipsLookup;

        $.when(recipes).done(function(data) {
          recipes = data.recipes;
          recipsLookup = lookup(recipes);
          addRows(recipes);
        }).fail(function(xhr, status, error) {
          dialog(error);
        });

        $("#save").click(function() {

          $.when(del("rest/recipe", recipes)).done(function(data) {
            dialog(data.message);

            recipes = read("rest/recipe");

            $.when(recipes).done(function(data) {
              recipes = data.recipes;
              addRows(recipes);
            }).fail(function(xhr, status, error) {
              dialog(error);
            });

          }).fail(function(xhr, status, error) {
            dialog(error);
          });
        });

        $("#add").click(function() {
          window.location.replace("recipe.html");
        });

        $("#delete").click(function() {
          remove(recipsLookup);
        });
      });
    </script>
  </body>
</html>
