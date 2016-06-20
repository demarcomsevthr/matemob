@ECHO OFF
set APPNAME=wolf

set MODULE=server
set SKIP_PAUSE=false
set SKIP_GWT_DEPENDENCIES=true
::set USE_GAE_DEPENDENCIES=true
set SKIP_DEPENDENCIES_CLEAN=true

set goals=
::set goals=%goals% clean
set goals=%goals% compile
set goals=%goals% war:exploded
set goals=%goals% jetty:run

call %~dp0\_build-base.bat %goals%

