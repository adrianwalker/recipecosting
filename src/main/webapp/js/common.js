function lookup(entities) {
  var entityLookup = {};
  $.each(entities, function(index, entity) {
    entityLookup[entity.id] = entity;
  });
  return entityLookup;
}

function remove(entities) {

  var checked = $('input[id^=id]:checked');

  if (checked.length === 0) {
    dialog("Delete", "Select items to delete");
    return;
  }

  checked.each(function() {
    $(this).closest("tr[id^=row]").remove();
  });

  var indexes = [];
  $.each(entities, function(index, entity) {

    if (entity._checked) {
      if (entity.id == null) {
        indexes.push(index);
      } else {
        entity._delete = true;
      }
    }
  });

  for (var i = indexes.length - 1; i >= 0; i--) {
    entities.splice(indexes[i], 1);
  }
}