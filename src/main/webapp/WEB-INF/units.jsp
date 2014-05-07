<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div  class="container">
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
        $(row).append($("<td/>").append("<input id='name" + index + "'  name='name" + index + "'value='" + unit.name + "' class='form-control' required/>"));

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
        }).fail(function(xhr, status, error) {
          dialog("Error", error);
        });

        $("#save").click(function() {

          if (!form.valid()) {
            return false;
          }

          $.when(save("rest/unit", units)).done(function(data) {
            dialog("Units", data.message);
            units = data.units;
            unitsLookup = lookup(units);
            addRows(units);
          }).fail(function(xhr, status, error) {
            dialog("Error", error);
          });
        });

        $("#add").click(function() {
          add(units);
        });

        $("#delete").click(function() {
          remove(unitsLookup);
        });
      });
    </script>
  </body>
</html>
