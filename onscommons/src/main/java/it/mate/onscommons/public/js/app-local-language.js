
function setAppLocalLanguage(value) {
  window.localStorage.setItem("app-local-language", value);
}

function getAppLocalLanguage() {
  return window.localStorage.getItem("app-local-language");
}

function checkAppLocalLanguage() {
  var lang = getAppLocalLanguage();
  if (lang == 'it') {
    if (window.location.href.indexOf('index-it') == -1) {
      var href = window.location.href.replace('index.html', 'index-it.html');
      window.location.replace(href);
    }
  }
  if (lang == 'en') {
    if (window.location.href.indexOf('index.html') == -1) {
      var href = window.location.href.replace('index-it.html', 'index.html');
      window.location.replace(href);
    }
  }
  return lang;
}

var appLanguage = checkAppLocalLanguage();

