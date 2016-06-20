#!/bin/bash

CURPATH=$(dirname $0)

MVNCMD=$M2_HOME\bin\mvn
BASEDIR=$CURPATH\..

echo ""
echo ""
echo "[INFO] ========================================================================================================"
echo "[INFO] JAVA_HOME: $JAVA_HOME"
echo "[INFO] M2_HOME: $M2_HOME"
echo "[INFO] BUILDING PROJECT: $CURPATH"
echo "[INFO] BUILDING OPTIONS: $@"
echo "[INFO] --------------------------------------------------------------------------------------------------------"

%MVNCMD% "$@"
