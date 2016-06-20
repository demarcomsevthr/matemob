@ECHO OFF

echo.
echo CREATING CORDOVA 4 PROJECT
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

call %~dp0\_setenv.bat

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
call cordova plugin add cordova-plugin-device
call cordova plugin add cordova-plugin-inappbrowser
call cordova plugin add cordova-plugin-console
call cordova plugin add cordova-plugin-globalization

call cordova plugin add cordova-plugin-file
call cordova plugin add cordova-plugin-file-transfer

:: 10/04/2015
call cordova plugin add https://github.com/phonegap-build/PushPlugin.git

:: 13/04/2015
call cordova plugin add https://github.com/wymsee/cordova-imagePicker.git

:: 25/04/2015
:: call cordova platform update android@4.0.0
:: call cordova plugin add https://github.com/apache/cordova-plugin-whitelist.git#r1.0.0

:: PROVA DI CROSSWALK
:: To install Crosswalk (optional):
::call cordova plugin add https://github.com/MobileChromeApps/cordova-plugin-crosswalk-webview.git#1.0.0


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
