@ECHO OFF

echo BUILDING ANDROID APP
echo.
echo.

title BUILDING ANDROID APP

call %~dp0\_setenv.bat

SET ANT_OPTS=-Xmx1g

SET BASEAPPDIR=%~dp0..

SET ANTCMD=%ANT_HOME%\bin\ant.bat

cd %BASEAPPDIR%\android

CALL %ANTCMD% clean

CALL %ANTCMD% release

cd ..

PAUSE