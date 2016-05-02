
var AppInitWrapper = function(modulePath, moduleName, options) {
  
  this.modulePath = modulePath;
  this.moduleName = moduleName;
  this.options = options;
  
  window._appInitWrapper = this;
  
  window.onload = function() {
    window._appInitWrapper.doInitApplication();
  };
  
}

AppInitWrapper.prototype = {
  
  doInitApplication: function () {
  
    var options = this.options;
  
    if (options === undefined) {
      options = {};
    }
      
    this.createMetaElement("Content-Type", "text/html; charset=UTF-8");
    
    if (options.language !== undefined) {
      this.createMetaElement("gwt:property", "locale=" + options.language);
    }
    
    if (options.initialScripts !== undefined) {
      for (var it = 0; it < options.initialScripts.length; it++) {
        this.createScriptElement(options.initialScripts[it]);
      }
    }

    this.createIFrameElement("javascript:''", "__gwt_historyFrame", '-1', "position:absolute;width:0;height:0;border:0");
    
    this.createScriptElement(this.modulePath + "/" + this.moduleName + ".nocache.js");
    
  },
  
  createScriptElement: function(src) {
    var head = document.getElementsByTagName('head').item(0);
    var script = document.createElement('script');
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('language', 'javascript');
    script.setAttribute('src', src);
    head.appendChild(script);
  },
    
  createIFrameElement: function(src, id, tabIndex, style) {
    var body = document.getElementsByTagName('body').item(0);
    var iframe = document.createElement('iframe');
    iframe.setAttribute('src', src);
    iframe.setAttribute('id', id);
    iframe.setAttribute('tabIndex', tabIndex);
    iframe.setAttribute('style', style);
    body.appendChild(iframe);
  },
    
  createMetaElement: function(name, value) {
    var head = document.getElementsByTagName('head').item(0);
    var meta = document.createElement('meta');
    meta.setAttribute('http-equiv', name);
    meta.setAttribute('content', value);
    head.appendChild(meta);
  }
    
}

function initApplication(modulePath, moduleName, options) {
  new AppInitWrapper(modulePath, moduleName, options);
}

