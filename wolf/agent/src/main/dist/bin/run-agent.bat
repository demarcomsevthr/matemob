@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

SET CURPATH=%~dp0
SET BASEDIR=%CURPATH%..

set JAVA_HOME=P:\OPT\java\jdk1.7.0_67

set JAVACMD=%JAVA_HOME%/bin/java

::set JAVA_OPTS=-Xms256m -Xmx1024m -server
::set JAVA_OPTS=%JAVA_OPTS% -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000
::set JAVA_OPTS=%JAVA_OPTS% -Dlog4j.configuration=log4j-inbound.properties
::set JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xnoagent -Djava.compiler=NONE
::set JAVA_OPTS=%JAVA_OPTS% -Xrunjdwp:transport=dt_socket,address=8788,server=y,suspend=n

set CP=%BASEDIR%\conf;
set LIB=%BASEDIR%\lib
cd %LIB%
for /F %%i in ('dir /b *.*') do set CP=!CP!;..\lib\%%i

echo Starting WOLF AGENT...

cd %CURPATH%
@echo on
%JAVACMD% %JAVA_OPTS% -cp %CP% it.mate.wolf.agent.AgentMain

