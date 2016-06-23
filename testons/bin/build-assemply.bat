@ECHO OFF
set APPNAME=testons

::SERVE PER COPIARE I POLICY FILE (.gwt.rpc) NEI PATH GIUSTI
set MODULE=gwt
set SKIP_PAUSE=false
set SKIP_DEPENDENCIES=true
set SKIP_DEPENDENCIES_CLEAN=true

:: UPGRADE TO GWT 2.7.0 (richiede maven 3)
call %~dp0\_setenv2.bat

set goals= 
set goals=%goals% -e
::set goals=%goals% war:exploded
set goals=%goals% assembly:single
call %~dp0\_build-base.bat %goals%
echo.
echo.
echo.

