<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
    <script src="js/ingredients.min.js"></script>
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
  </body>
</html>
