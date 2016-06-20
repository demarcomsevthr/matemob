@ECHO OFF

call %~dp0\_setenv.bat

SET BASEDIR=%~dp0..

SET MVN2CMD=%BASEDIR%\bin\_mvn2.bat

cd %BASEDIR%

SET gid=com.netiq
SET ver=0.4
SET aid=gwt-syncproxy
SET file=extlib/SyncProxy-0.4.jar

call %MVN2CMD% install:install-file -Dfile=%file% -DgroupId=%gid% -DartifactId=%aid% -Dversion=%ver% -Dpackaging=jar -DgeneratePom=true

pause
