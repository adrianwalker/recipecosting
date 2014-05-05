function lookup(entities) {
  var entityLookup = {};
  $.each(entities, function(index, entity) {
    entityLookup[entity.id] = entity;
  });
  return entityLookup;
}

function remove(lookup) {
  var checked = $('input[id^=id]:checked');

  if (checked.length === 0) {
    dialog("Delete","Select items to delete");
    return;
  }

  checked.each(function() {

    var value = $(this).val();
    if (value !== 'null') {
      var entity = lookup[value];
      entity._delete = true;
    }

    $(this).closest("tr[id^=row]").remove();
  });
}