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
                unit
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
      function addRows(units) {

        $('#data tbody').remove();

        $.each(units, function(index, unit) {
          addRow(index, unit);
        });
      }

      function addRow(index, unit) {

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' type='checkbox' value='" + unit.id + "'/>"));
        $(row).append($("<td/>").append("<input id='name" + index + "' value='" + unit.name + "' />"));

        $("#data").append(row);

        $("#name" + index).bind("input", function() {
          unit._changed = true;
          unit.name = $(this).val();
        });
      }

      function add(units) {
        var unit = {
          id: null,
          name: ""
        };
        addRow(units.length, unit);
        units.push(unit);
      }

      $(function() {
        var units = read("rest/unit");

        $.when(units).done(function(data) {
          units = data.units;
          addRows(units);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {
          $.when(save("rest/unit", units)).done(function(data) {
            dialog(data.message);

            units = read("rest/unit");

            $.when(units).done(function(data) {
              units = data.units;
              addRows(units);
            }).fail(function() {
              error();
            });

          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(units);
        });

        $("#delete").click(function() {

          var ids = [];

          $('input[id^=id]:checked').each(function() {

            var value = $(this).val();
            if (value !== 'null') {
              ids.push(value);
            }
          });

          $.when(del("rest/unit", ids)).done(function(data) {
            dialog(data.message);

            units = read("rest/unit");

            $.when(units).done(function(data) {
              units = data.units;
              addRows(units);
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
