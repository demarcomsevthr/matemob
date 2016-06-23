@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET CURDRIVE=%~d0
SET CURPATH=%~dp0
SET MVNCMD=%M2_HOME%\bin\mvn
SET BASEDIR=%CURPATH%..

@echo.
@echo.
@echo [INFO] ========================================================================================================
@echo [INFO] JAVA_HOME: %JAVA_HOME%
@echo [INFO] M2_HOME: %M2_HOME%
@echo [INFO] BUILDING PROJECT: %CD%
@echo [INFO] BUILDING OPTIONS: %*
@echo [INFO] --------------------------------------------------------------------------------------------------------

%MVNCMD% %*
