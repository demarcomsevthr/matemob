@ECHO OFF
set APPNAME=testons
set MODULE=gwt
set SKIP_PAUSE=false
set SKIP_DEPENDENCIES_CLEAN=true
set goals=
::set goals=%goals% -e
set goals=%goals% clean
set goals=%goals% compile
set goals=%goals% gwt:compile
set goals=%goals% war:exploded
set goals=%goals% assembly:assembly
call %~dp0\_build-base.bat %goals%
