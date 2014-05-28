<!DOCTYPE html>
<html>
  <head>
    <title>Recipe</title>
    <%@ include file="head.jspf" %>
    <script src="js/recipe.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <h2>Recipe</h2>

      <form id="form">
        <div class="panel panel-default">
          <div class="panel-body">
            <div>
              <table class="table">
                <tr>
                  <td>
                    <label for="name">Name:</label>
                  </td>
                  <td>
                    <input id="name" name="name" class="form-control" type="text" required autofocus/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <label for="serves">Servings:</label>
                  </td>
                  <td>
                    <input id="serves" name="serves" class="form-control" type="number" required/>
                  </td>
                </tr>
              </table>
            </div>
            <div>
              <table id="data" class="table">
                <thead>
                  <tr>
                    <th>
                      &nbsp;
                    </th>
                    <th>
                      Ingredient
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
            <div>
              <table class="table">
                <tr>
                  <td>
                    Total Cost:
                  </td>
                  <td class='text-right'>
                    <label id="total_cost"></label>
                  </td>
                </tr>
                <tr>
                  <td>
                    Serving Cost:
                  </td>
                  <td class='text-right'>
                    <label id="serving_cost"></label>
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>

        <button id="save" type="button" class="btn btn-primary">Save Recipe</button>
        <button id="add" type="button" class="btn btn-primary">Add Ingredient</button>
        <button id="delete" type="button" class="btn btn-primary">Delete Ingredients</button>
      </form>
    </div>
  </body>
</html>
