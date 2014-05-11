function addRows(recipes) {

  $('#data tbody').remove();

  $.each(recipes, function(index, recipe) {
    addRow(index, recipe);
  });
}

function addRow(index, recipe) {

  var row = $("<tr id='row" + index + "'/>");
  $(row).append($("<td style='width: 1px;'/>").append("<input id='id" + index + "' type='checkbox' value='" + recipe.id + "'/>"));
  $(row).append($("<td/>").append("<a href='recipe.html?id=" + recipe.id + "' id='name" + index + "' >" + recipe.name + "</a>"));

  $("#data").append(row);
}

$(function() {
  var recipes = read("rest/recipe");
  var recipsLookup;

  $.when(recipes).done(function(data) {
    recipes = data.recipes;
    recipsLookup = lookup(recipes);
    addRows(recipes);
  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });

  $("#save").click(function() {

    $.when(del("rest/recipe", recipes)).done(function(data) {
      dialog("Recipes", data.message);

      recipes = read("rest/recipe");

      $.when(recipes).done(function(data) {
        recipes = data.recipes;
        addRows(recipes);
      }).fail(function(xhr, status, error) {
        dialog("Error", error);
      });

    }).fail(function(xhr, status, error) {
      dialog("Error", error);
    });
  });

  $("#add").click(function() {
    window.location.replace("recipe.html");
  });

  $("#delete").click(function() {
    remove(recipsLookup);
  });
});