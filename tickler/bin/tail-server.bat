@ECHO OFF
set APPNAME=tickler

set OS_TARGET_DISK=G:

set OS_TARGET_DIR=%OS_TARGET_DISK%\ticklersrv

%OS_TARGET_DISK%
cd %OS_TARGET_DIR%

rhc tail -a jbossews

echo.
echo.
echo.
pause