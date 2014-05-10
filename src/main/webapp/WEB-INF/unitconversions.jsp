<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
    <script src="js/unitconversions.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <h2>Unit Conversions</h2>

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
                      From Unit
                    </th>
                    <th>
                      Ratio
                    </th>
                    <th>
                      To Unit
                    </th>
                  </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
        <button id="save" type="button" class="btn btn-primary">Save Conversions</button>
        <button id="add" type="button" class="btn btn-primary">Add Conversion</button>
        <button id="delete" type="button" class="btn btn-primary">Delete Conversions</button>
      </form>
    </div>
  </body>
</html>
