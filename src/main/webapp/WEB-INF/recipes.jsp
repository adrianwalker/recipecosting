<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
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
              <input id="new" type="button" value="New" />
            </td>
            <td>
              <input id="delete" type="button" value="Delete" />
            </td>
          </tr>
        </table>
      </div>
    </div>
    <script>
      var page = 1;
      var pageSize = 20;

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
        var recipes = read("rest/recipe", page, pageSize);

        $.when(recipes).done(function(data) {
          recipes = data.recipes;
          addRows(recipes);
        }).fail(function() {
          error();
        });

        $("#new").click(function() {
          window.location.replace("recipe.html");
        });

        $("#delete").click(function() {

          var ids = [];

          $('input[id^=id]:checked').each(function() {

            var value = $(this).val();
            if (value !== 'null') {
              ids.push(value);
            }
          });

          $.when(del("rest/recipe", ids)).done(function(data) {
            dialog(data.message);

            recipes = read("rest/recipe", page, pageSize);

            $.when(recipes).done(function(data) {
              recipes = data.recipes;
              addRows(recipes);
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
