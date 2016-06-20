@ECHO OFF
set APPNAME=testleaflet

set MODULE=gwt
set SKIP_PAUSE=false
set SKIP_DEPENDENCIES=true
set goals=
::set goals=%goals% -e
set goals=%goals% war:exploded
set goals=%goals% assembly:assembly

call %~dp0\_build-base.bat %goals%
