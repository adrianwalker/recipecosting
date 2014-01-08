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

function save(url, data) {

  if ($.isArray(data)) {

    var changed = [];

    $.each(data, function(index, value) {

      if (value.changed) {
        changed.push(value);
      }
    });

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

  switch (key) {
    case "cost":
      return undefined;
    case "changed":
      return undefined;
    case "user":
      return undefined;
    default:
      return value;
  }
}
