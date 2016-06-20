@ECHO OFF
set APPNAME=wolf

set MODULE=agent
set SKIP_PAUSE=false
set SKIP_GWT_DEPENDENCIES=true

set goals=
set goals=%goals% clean
set goals=%goals% package
set goals=%goals% assembly:assembly

call %~dp0\_build-base.bat %goals%