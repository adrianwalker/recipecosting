function read(url, page, pageSize) {

  return $.ajax({
    type: "GET",
    url: url,
    data: {
      page: page,
      pageSize: pageSize
    }
  });
}

function read(url) {

  return $.ajax({
    type: "GET",
    url: url
  });
}

function lookup(entities) {
  var entityLookup = {};
  $.each(entities, function(index, entity) {
    entityLookup[entity.id] = entity;
  });
  return entityLookup;
}

function save(url, data) {

  if ($.isArray(data)) {

    var changed = [];
    var ids = [];

    $.each(data, function(index, value) {

      if (value._delete) {
        ids.push(value.id);
      } else if (value._changed) {
        changed.push(value);
      }
    });

    del(url, ids);

    return $.ajax({
      type: "POST",
      url: url,
      data: JSON.stringify(changed, replacer),
      contentType: "application/json",
      dataType: "json"
    });

  } else {

    return $.ajax({
      type: "POST",
      url: url,
      data: JSON.stringify(data, replacer),
      contentType: "application/json",
      dataType: "json"
    });
  }
}

function del(url, ids) {

  return $.ajax({
    type: "DELETE",
    url: url,
    data: JSON.stringify(ids),
    contentType: "application/json",
    dataType: "json"
  });
}

function replacer(key, value) {

  if (key.indexOf("_") === 0) {
    return undefined;
  } else {
    return value;
  }
}
