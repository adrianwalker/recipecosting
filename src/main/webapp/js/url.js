function getUrlParams() {
  var query = location.search.substr(1);
  var data = query.split("&");
  var result = {};
  for (var i = 0; i < data.length; i++) {
    var item = data[i].split("=");
    result[item[0]] = item[1];
  }
  return result;
}