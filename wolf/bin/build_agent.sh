#!/bin/bash

echo "BUILDING AGENT"

export CURPATH="$(dirname $(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}"))"
export BASEDIR=$CURPATH/..
export MODULE=agent

. $CURPATH/_setenv.sh

cd $BASEDIR/$MODULE
$CURPATH/_mvn2.sh clean package
