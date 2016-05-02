
/*******************************************************************************************************************************************
 * 
 * QUESTO SCRIPT SERVE PER INIZIALIZZARE ANGULAR IN MODO DA POTER DEFINIRE DEI CONTROLLER DA DENTRO GWT A "RUN-TIME" (O IN MODALITA' "LAZY")
 * 
 * OVVERO DOPO AVER FATTO IL BOOTSTRAP DELL'APP
 * 
 * SEE: http://www.bennadel.com/blog/2553-loading-angularjs-components-after-your-application-has-been-bootstrapped.htm
 * 
 *
 * NOTA BENE:
 *
 *   NON CAPISCO PERCHE', MA LO STESSO IDENTICO SCRIPT ESEGUITO IN UNA FUNZIONE JSNI NON FUNZIONA
 *   (IN PARTICOLARE NON VIENE ESEGUITO IL METODO DI CONFIG)
 * 
 */

/*  
function initNgReconfigurableAppModule(requiredModules) {
  return;
}
*/

function initNgReconfigurableAppModule(requiredModules) {
  'use strict';
  var RECONFIGURABLE_MODULE_NAME = "ngReconfigurableAppModule";
  document.getElementsByTagName("html")[0].setAttribute("ng-app", RECONFIGURABLE_MODULE_NAME);
  console.log('app-angular-extension.js::initNgReconfigurableAppModule: STEP #1 - REFERENCING RECONFIGURABLE MODULE ' + RECONFIGURABLE_MODULE_NAME + ' WITH MODULES ' + requiredModules);
  var app = angular.module(RECONFIGURABLE_MODULE_NAME, requiredModules, 
    function( $controllerProvider, $provide, $compileProvider ) {
   	  console.log('app-angular-extension.js::initNgReconfigurableAppModule: STEP #4 - EXECUTING CONFIG METHOD ON APP='+app);
      window.ngReconfigurableAppModule = app;
      window.ngReconfigurableAppModuleName = RECONFIGURABLE_MODULE_NAME;
      app._controller = app.controller;
      app._service = app.service;
      app._factory = app.factory;
      app._value = app.value;
      app._directive = app.directive;
      app.controller = function( name, constructor ) {
        $controllerProvider.register( name, constructor );
        return( this );
      };
      app.service = function( name, constructor ) {
        $provide.service( name, constructor );
        return( this );
      };
      app.factory = function( name, factory ) {
        $provide.factory( name, factory );
        return( this );
      };
      app.value = function( name, value ) {
        $provide.value( name, value );
        return( this );
      };
      app.directive = function( name, factory ) {
        $compileProvider.directive( name, factory );
        return( this );
      };
    }
  );
  if (app === undefined) {
    console.log('ERROR: CANNOT FIND MODULE WITH NAME ' + RECONFIGURABLE_MODULE_NAME);
    return;
  }
  console.log('app-angular-extension.js::initNgReconfigurableAppModule: STEP #3 - END SCRIPT');
}

