@ECHO OFF
set APPNAME=wolf

set OS_TARGET_DISK=G:

set OS_TARGET_DIR=%OS_TARGET_DISK%\wolfsrv

set MODULE=server
set SKIP_DEPENDENCIES=true
set SKIP_PAUSE=true

set antfiledir=%~dp0..\ant
set antfilename=build.xml

set goals=
set goals=%goals% -Dantfiledir=%antfiledir% -Dantfilename=%antfilename% -Dant.execution.task=post-build
set goals=%goals% -Dopenshiftdir=%OS_TARGET_DIR%
::set goals=%goals% -e
set goals=%goals% antrun:run

call %~dp0\_build-base.bat %goals%

echo.
echo.
echo DEPLOYING TO OPENSHIFT...
echo.
echo.

%OS_TARGET_DISK%
cd %OS_TARGET_DIR%

git add .
git status
git commit -m "."
git push

echo.
echo.
echo.
pause