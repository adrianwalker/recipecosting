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
      <form id="form">
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
      </form>
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
        $(row).append($("<td/>").append("<input id='id" + index + "' name='id" + index + "' type='checkbox' value='" + unit.id + "'/>"));
        $(row).append($("<td/>").append("<input id='name" + index + "'  name='name" + index + "'value='" + unit.name + "' />"));

        $("#data").append(row);

        $("#name" + index).bind("input", function() {
          unit._changed = true;
          unit.name = $(this).val();
        });

        $("#name" + index).rules('add', {
          required: true,
          minlength: 1,
          maxlength: 1000
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

        var form = $("#form");
        form.validate();

        var units = read("rest/unit");
        var unitsLookup;

        $.when(units).done(function(data) {
          units = data.units;
          unitsLookup = lookup(units);
          addRows(units);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {

          if (!form.valid()) {
            return false;
          }

          $.when(save("rest/unit", units), del("rest/unit", units)).done(function(data1, data2) {
            dialog(data1[0].message);

            /*
            units = read("rest/unit");

            $.when(units).done(function(data) {
              units = data.units;
              unitsLookup = lookup(units);
              addRows(units);
            }).fail(function() {
              error();
            });
            */

          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(units);
        });

        $("#delete").click(function() {
          remove(unitsLookup)
        });
      });
    </script>
  </body>
</html>
