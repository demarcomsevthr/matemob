#!/bin/bash

CURPATH=$(pwd)
MVNCMD=$M2_HOME/bin/mvn

echo ""
echo ""
echo "[INFO] ========================================================================================================"
echo "[INFO] JAVA_HOME: $JAVA_HOME"
echo "[INFO] M2_HOME: $M2_HOME"
echo "[INFO] BUILDING PROJECT: $CURPATH"
echo "[INFO] BUILDING OPTIONS: $@"
echo "[INFO] --------------------------------------------------------------------------------------------------------"

$MVNCMD "$@"
