@ECHO OFF
set APPNAME=testleaflet
set MODULE=server
set SKIP_PAUSE=false
set SKIP_MOBILE_DEPENDENCIES=true
call %~dp0\_setenv2.bat
set goals=
set goals=%goals% clean
set goals=%goals% compile
set goals=%goals% gwt:compile
set goals=%goals% datanucleus:enhance
::set goals=%goals% appengine:endpoints_get_discovery_doc
::set goals=%goals% appengine:endpoints_get_client_lib
set goals=%goals% war:exploded
call %~dp0\_build-base.bat %goals%