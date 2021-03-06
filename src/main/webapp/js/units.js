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

  $("#id" + index).bind("change", function() {
    unit._checked = this.checked;
  });

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

  $.when(units).done(function(data) {
    units = data.units;
    addRows(units);
  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });

  $("#save").click(function() {

    if (!form.valid()) {
      return false;
    }

    $.when(save("rest/unit", units)).done(function(data) {
      dialog("Units", data.message, data.messages);
      if (data.saved) {
        units = data.units;
        addRows(units);
      }
    }).fail(function(xhr, status, error) {
      dialog("Error", error);
    });
  });

  $("#add").click(function() {
    add(units);
  });

  $("#delete").click(function() {
    remove(units);
  });
});
