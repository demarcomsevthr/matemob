#!/bin/bash

echo "BUILDING AGENT"

export CURPATH="$(dirname $(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}"))"
export BASEDIR=$CURPATH/..

. $CURPATH/_setenv.sh

cd $BASEDIR
$CURPATH/_mvn2.sh install

cd $BASEDIR/adapter
$CURPATH/_mvn2.sh clean install

cd $BASEDIR/agent
$CURPATH/_mvn2.sh clean package assembly:assembly

echo ""
read -p "Premere un tasto per continuare"