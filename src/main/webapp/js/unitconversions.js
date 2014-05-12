function addRows(unitConversions, units) {

  $('#data tbody').remove();

  $.each(unitConversions, function(index, unitConversion) {
    addRow(index, unitConversion, units);
  });
}

function addRow(index, unitConversion, units) {

  var unitFromSelect = $("<select id='unitFrom" + index + "' name='unitFrom" + index + "' class='form-control' required/>");
  $(unitFromSelect).append("<option value=''>-- select --</option>");
  var unitToSelect = $("<select id='unitTo" + index + "' name='unitTo" + index + "' class='form-control' required/>");
  $(unitToSelect).append("<option value=''>-- select --</option>");
  $.each(units, function(index, unit) {
    var selected;
    selected = (unit.id === unitConversion.unitFrom.id) ? "selected" : "";
    $(unitFromSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>")
    selected = (unit.id === unitConversion.unitTo.id) ? "selected" : "";
    $(unitToSelect).append("<option value='" + unit.id + "' " + selected + ">" + unit.name + "</option>")
  });

  var row = $("<tr id='row" + index + "'/>");
  $(row).append($("<td/>").append("<input id='id" + index + "' name='id" + index + "' type='checkbox' value='" + unitConversion.id + "'/>"));
  $(row).append($("<td/>").append(unitFromSelect));
  $(row).append($("<td/>").append("<input id='ratio" + index + "' name='ratio" + index + "' value='" + unitConversion.ratio + "' class='form-control text-right' type='number' required/>"));
  $(row).append($("<td/>").append(unitToSelect));

  $("#data").append(row);

  $("#unitFrom" + index).bind("change", function() {
    unitConversion._changed = true;
    unitConversion.unitFrom.id = $(this).val();
  });

  $("#unitFrom" + index).rules('add', {
    required: true,
  });

  $("#ratio" + index).bind("input", function() {
    unitConversion._changed = true;
    unitConversion.ratio = $(this).val();
  });

  $("#ratio" + index).rules('add', {
    required: true,
    number: true,
    minlength: 1,
    maxlength: 20
  });

  $("#unitTo" + index).bind("change", function() {
    unitConversion._changed = true;
    unitConversion.unitTo.id = $(this).val();
  });

  $("#unitTo" + index).rules('add', {
    required: true,
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

  var form = $("#form");
  form.validate();

  var unitConversions = read("rest/unitconversion");
  var units = read("rest/unit");
  var unitConversionsLookup;

  $.when(unitConversions, units).done(function(data1, data2) {
    unitConversions = data1[0].unitConversions;
    units = data2[0].units;
    unitConversionsLookup = lookup(unitConversions);
    addRows(unitConversions, units);
  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });

  $("#save").click(function() {

    form.validate();

    if (!form.valid()) {
      return false;
    }

    $.when(save("rest/unitconversion", unitConversions)).done(function(data) {
      dialog("Unit Conversions", data.message);
      if (data.saved) {
        unitConversions = data.unitConversions;
        unitConversionsLookup = lookup(unitConversions);
        addRows(unitConversions, units);
      }
    }).fail(function(xhr, status, error) {
      dialog("Error", error);
    });
  });

  $("#add").click(function() {
    add(unitConversions, units)
  });

  $("#delete").click(function() {
    remove(unitConversionsLookup)
  });
});
