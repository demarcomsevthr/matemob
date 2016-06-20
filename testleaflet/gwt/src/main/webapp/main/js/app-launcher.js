
function appLauncher(language) {
	
  var options = {};
  
  options.initialScripts = ["cordova.js",
                            "jquery-1.9.1.min.js",
                            "main/js/app-local-language.js"];
  
  if (language !== undefined) {
    options.language = language;
  }
  
  initApplication("main", "main", options);
  
}
