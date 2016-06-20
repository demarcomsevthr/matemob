@ECHO OFF

echo BUILDING APP %APPNAME% %MODULE%
echo.
echo.

title BUILDING APP %APPNAME% %MODULE% [%*]

if "%MODULE%"=="" GOTO NO_MODULE_SET

call %~dp0\_setenv.bat

SET BASEAPPDIR=%~dp0..
SET MVN2CMD=%BASEAPPDIR%\bin\_mvn2.bat

if "%SKIP_DEPENDENCIES%"=="true" GOTO NO_DEPENDENCIES

set DEP_CLEAN=clean

if "%SKIP_DEPENDENCIES_CLEAN%"=="true" set DEP_CLEAN=

if "%SKIP_GWT_DEPENDENCIES%"=="true" GOTO NO_GWT_DEPENDENCIES

cd %BASEAPPDIR%\..\gwtcommons
call %MVN2CMD% %DEP_CLEAN% install

:NO_GWT_DEPENDENCIES

if "%USE_GAE_DEPENDENCIES%"=="" GOTO NO_GAE_DEPENDENCIES

cd %BASEAPPDIR%\..\gaecommons
call %MVN2CMD% %DEP_CLEAN% install

:NO_GAE_DEPENDENCIES

cd %BASEAPPDIR%
call %MVN2CMD% install

cd %BASEAPPDIR%\adapter
call %MVN2CMD% clean install

:NO_DEPENDENCIES

cd %BASEAPPDIR%\%MODULE%
call %MVN2CMD% %*

:COPY_DEPLOY

if "%DEPLOY_TARGET%"=="" GOTO NO_COPY_DEPLOY
set DEPLOY_SOURCE="%BASEAPPDIR%\android\assets\www"
del /Q /S "%DEPLOY_TARGET%\*" >NUL
rmdir /Q /S "%DEPLOY_TARGET%\main"
echo COPYING DEPLOY TO %DEPLOY_TARGET%
xcopy /E /Y %DEPLOY_SOURCE%\main "%DEPLOY_TARGET%\main\"
::del /Q /S "%DEPLOY_TARGET%\cordova-*.js" >NUL

:NO_COPY_DEPLOY

if "%SKIP_PAUSE%"=="true" GOTO NO_PAUSE
pause
GOTO END

:NO_MODULE_SET
echo NO MODULE SET
GOTO END

:NO_PAUSE
:END
title Ready
cd %BASEAPPDIR%
