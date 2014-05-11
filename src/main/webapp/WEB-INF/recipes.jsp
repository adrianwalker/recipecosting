<!DOCTYPE html>
<html>
  <head>
    <title>Recipes</title>
    <%@ include file="head.jspf" %>
    <script src="js/recipes.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <h2>Recipes</h2>

      <div class="panel panel-default">
        <div class="panel-body">
          <table id="data" class="table">
          </table>
        </div>
      </div>

      <button id="save" type="button" class="btn btn-primary">Save Recipes</button>
      <button id="add" type="button" class="btn btn-primary">Add Recipe</button>
      <button id="delete" type="button" class="btn btn-primary">Delete Recipes</button>
    </div>
  </body>
</html>
