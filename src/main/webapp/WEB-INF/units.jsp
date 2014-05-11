<!DOCTYPE html>
<html>
  <head>
    <title>Units</title>
    <%@ include file="head.jspf" %>
    <script src="js/units.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <h2>Units</h2>

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
                      Unit
                    </th>
                  </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
        <button id="save" type="button" class="btn btn-primary">Save Units</button>
        <button id="add" type="button" class="btn btn-primary">Add Unit</button>
        <button id="delete" type="button" class="btn btn-primary">Delete Units</button>
      </form>
    </div>
  </body>
</html>
