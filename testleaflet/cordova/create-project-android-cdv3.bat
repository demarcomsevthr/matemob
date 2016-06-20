@ECHO OFF

echo.
echo CREATING PHONEGAP 3 PROJECT
echo.
echo ATTENZIONE!!!
echo.
echo OCCORRE INSERIRE NEL PATH DI SISTEMA LE SEGUENTI VARIABILI:
echo     ANT_HOME\bin
echo     JAVA_HOME\bin
echo     AND_SDK\platform-tools
echo     AND_SDK\tools
echo.
echo.

SET JAVA_HOME=P:\OPT\java\jdk1.7.0_17

SET PACKAGE=it.mate.testleaflet

SET APPNAME=Testleaflet

echo PATH VARIABLE
echo %path%

SET BASEDIR=%~dp0

SET PROJNAME=template-project

cd %BASEDIR%

echo.
echo.
echo removing project dir %projname%
RMDIR /S /Q %projname%

echo.
echo.
echo creating project %projname% %package% %appname%
pause
call cordova create %projname% %package% %appname%

cd %projname%

echo.
echo.
echo adding android platform
pause
call cordova platform add android

echo.
echo.
echo adding plugins
pause
call cordova plugin add org.apache.cordova.device
call cordova plugin add org.apache.cordova.inappbrowser
call cordova plugin add org.apache.cordova.console
call cordova plugin add org.apache.cordova.globalization

call cordova plugin add org.apache.cordova.file
call cordova plugin add org.apache.cordova.file-transfer

:: 10/04/2015
call cordova plugin add https://github.com/phonegap-build/PushPlugin.git

:: 13/04/2015
call cordova plugin add https://github.com/wymsee/cordova-imagePicker.git

echo.
echo.
echo building android
pause
call cordova build android

echo.
echo.
echo FINISH
pause
cd %BASEDIR%

echo.
echo.
echo >>>>>>>>>>>>>  ATTENZIONE  <<<<<<<<<<<<<<
echo RICORDARSI DI RIPULIRE IL PATH DI SISTEMA AL TERMINE DELLA BUILD 
echo.
echo.
echo.

::EXIT
