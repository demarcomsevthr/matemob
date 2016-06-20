@ECHO OFF
set APPNAME=testleaflet

set MODULE=server
set SKIP_PAUSE=true
set SKIP_DEPENDENCIES=true
set SKIP_COPY_DEPLOY=true
call %~dp0\_setenv2.bat

set goals=
set goals=%goals% clean
set goals=%goals% compile

call %~dp0\_build-base.bat %goals%

