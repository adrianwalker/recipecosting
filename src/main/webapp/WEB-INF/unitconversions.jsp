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
                from unit
              </th>
              <th>
                ratio
              </th>
              <th>
                to unit
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
      function addRows(unitConversions, units) {

        $('#data tbody').remove();

        $.each(unitConversions, function(index, unitConversion) {
          addRow(index, unitConversion, units);
        });
      }

      function addRow(index, unitConversion, units) {

        var unitFromSelect = $("<select id='unitFrom" + index + "' />");
        $(unitFromSelect).append("<option>-- select --</option>");
        var unitToSelect = $("<select id='unitTo" + index + "' />");
        $(unitToSelect).append("<option>-- select --</option>");
        $.each(units, function(index, unit) {
          var selected;
          selected = (unit.id === unitConversion.unitFrom.id) ? "selected" : "";
          $(unitFromSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>")
          selected = (unit.id === unitConversion.unitTo.id) ? "selected" : "";
          $(unitToSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>")
        });

        var row = $("<tr id='row" + index + "'/>");
        $(row).append($("<td/>").append("<input id='id" + index + "' type='checkbox' value='" + unitConversion.id + "'/>"));
        $(row).append($("<td/>").append(unitFromSelect));
        $(row).append($("<td/>").append("<input id='ratio" + index + "' value='" + unitConversion.ratio + "' />"));
        $(row).append($("<td/>").append(unitToSelect));

        $("#data").append(row);

        $("#unitFrom" + index).bind("change", function() {
          unitConversion._changed = true;
          unitConversion.unitFrom.id = $(this).val();
        });

        $("#ratio" + index).bind("input", function() {
          unitConversion._changed = true;
          unitConversion.ratio = $(this).val();
        });

        $("#unitTo" + index).bind("change", function() {
          unitConversion._changed = true;
          unitConversion.unitTo.id = $(this).val();
        });
      }

      function add(unitConversions, units) {
        var unitConversion = {
          id: null,
          unitFrom: {id: null},
          ratio: 0.0,
          unitTo: {id: null}
        };
        addRow(unitConversions.length, unitConversion, units);
        unitConversions.push(unitConversion);
      }

      $(function() {
        var unitConversions = read("rest/unitconversion");
        var units = read("rest/unit");

        $.when(unitConversions, units).done(function(data1, data2) {
          unitConversions = data1[0].unitConversions;
          units = data2[0].units;
          addRows(unitConversions, units);
        }).fail(function() {
          error();
        });

        $("#save").click(function() {
          $.when(save("rest/unitconversion", unitConversions)).done(function(data) {
            dialog(data.message);

            unitConversions = read("rest/unitconversion");

            $.when(unitConversions).done(function(data) {
              unitConversions = data.unitConversions;
              addRows(unitConversions, units);
            }).fail(function() {
              error();
            });

          }).fail(function() {
            error();
          });
        });

        $("#add").click(function() {
          add(unitConversions, units);
        });

        $("#delete").click(function() {

          var ids = [];

          $('input[id^=id]:checked').each(function() {

            var value = $(this).val();
            if (value !== 'null') {
              ids.push(value);
            }
          });

          $.when(del("rest/unitconversion", ids)).done(function(data) {
            dialog(data.message);

            unitConversions = read("rest/unitconversion");

            $.when(unitConversions).done(function(data) {
              unitConversions = data.unitConversions;
              addRows(unitConversions, units);
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
