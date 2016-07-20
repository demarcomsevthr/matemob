@ECHO OFF
set APPNAME=tickler

set MODULE=server
set SKIP_PAUSE=false
::set SKIP_GWT_DEPENDENCIES=true
::set USE_GAE_DEPENDENCIES=true

set goals=
set goals=%goals% clean
set goals=%goals% compile
set goals=%goals% war:exploded

call %~dp0\_build-base.bat %goals%