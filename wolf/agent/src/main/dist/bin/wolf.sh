### ====================================================================== ###
##                                                                          ##
##  WOLF AGENT Bootstrap Script                                             ##
##                                                                          ##
### ====================================================================== ###

### $Id: hl7pad.sh 2012-07-16 demarco.m@melograno.it $ ###

DIRNAME=$(dirname $0)

. "$DIRNAME/setenv.sh"

echo JAVA_HOME=$JAVA_HOME

JAVA_OPTS=""

#JAVA_OPTS="-Xms256m -Xmx512m -server"

#JAVA_OPTS="$JAVA_OPTS -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000"

#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xnoagent -Djava.compiler=NONE"

#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8788,server=y,suspend=n"


#############################################################################

startproc(){
  cd $AGENT_HOME/lib
  for file in *.jar
  do
    CP=${CP}:$AGENT_HOME/lib/$file
  done
  JAVA_OPTS="$JAVA_OPTS $addopt"
  echo Starting $title ...
  cd $AGENT_HOME/bin
  STARTCMD="$JAVA_HOME/bin/java -Dprogram.name=$PROCNAME $JAVA_OPTS -cp $CP $mainclass "
  echo $STARTCMD
  if [ "x$RUN_BATCH" = "x" ]; then
    $STARTCMD
  else
    $STARTCMD >/dev/null 2>&1 &
  fi
}

#############################################################################

case "$1" in
start)
    PROCNAME=wolf-agent.sh
    CP=$AGENT_HOME/conf
    addopt="-Dlog4j.configuration=log4j.properties"
    title="WOLF AGENT"
    mainclass="it.mate.wolf.agent.AgentMain"
  ##  RUN_BATCH=true
    startproc
    ;;
*)
    echo "usage: $0 (start|stop)"
    exit
    ;;
esac
