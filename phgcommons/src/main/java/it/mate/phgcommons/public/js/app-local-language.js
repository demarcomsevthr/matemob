
function setAppLocalLanguage(value) {
  window.localStorage.setItem("app-local-language", value);
}

function getAppLocalLanguage() {
  return window.localStorage.getItem("app-local-language");
}

function checkAppLocalLanguage() {
  var lang = getAppLocalLanguage();
  
  /* 20/10/2015 */
  
  /* VERSIONE GENERALE */
  if (lang != null) {
    if (getUrlParam('locale') != lang) {
      setUrlParamAndReload("locale", lang);
    }
  }
  
  /* VERSIONE RISTRETTA 
  if (lang == 'it') {
    if (getUrlParam('locale') != "it") {
      setUrlParamAndReload("locale", "it");
    }
  }
  if (lang == 'en') {
    if (getUrlParam('locale') != "en") {
      setUrlParamAndReload("locale", "en");
    }
  }
  */
  
  /* 20/10/2015 - BEFORE
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
  */
  
  return lang;
}


function getUrlParam(key) {
    key = encodeURI(key);
    var kvp = document.location.search.substr(1).split('&');
    var i=kvp.length; var x; 
    while(i--) {
        x = kvp[i].split('=');
        if (x[0]==key) {
            return x[1];
        }
    }
    return "undefined";
}

function setUrlParamAndReload(key, value) {
    key = encodeURI(key); value = encodeURI(value);

    var kvp = document.location.search.substr(1).split('&');

    var i=kvp.length; var x; 
    while(i--) {
        x = kvp[i].split('=');
        if (x[0]==key) {
            x[1] = value;
            kvp[i] = x.join('=');
            break;
        }
    }

    if(i<0) {kvp[kvp.length] = [key,value].join('=');}

    //this will reload the page, it's likely better to store this until finished
    document.location.search = kvp.join('&'); 
}

var appLanguage = checkAppLocalLanguage();

