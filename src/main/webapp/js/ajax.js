function read(url) {

  return $.ajax({
    type: "GET",
    url: url
  });
}

function save(url, data) {

  var changed = [];
  var ids = [];

  $.each(data, function(index, value) {

    if (value._delete) {
      ids.push(value.id);
    } else if (value._changed) {
      changed.push(value);
    }
  });

  if (changed.length === 0 && ids.length === 0) {
    data = {
      saved: false,
      message: "No changes to save"
    };

    return data;
  }

  data = {
    changed: changed,
    ids: ids
  };

  return $.ajax({
    type: "POST",
    url: url,
    data: JSON.stringify(data, replacer),
    contentType: "application/json",
    dataType: "json"
  });
}

function del(url, data) {

  var ids = [];

  $.each(data, function(index, value) {

    if (value._delete) {
      ids.push(value.id);
    }
  });

  if (ids.length === 0) {
    data = {
      saved: false,
      message: "No changes to save"
    };

    return data;
  }

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
