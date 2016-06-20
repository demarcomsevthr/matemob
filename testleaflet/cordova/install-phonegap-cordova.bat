@echo off

set NJH=P:\OPT\nodejs\node-v0.10.24-x64

CD %NJH%

::call npm config set registry http://registry.npmjs.org

call npm install -g phonegap

call npm install -g cordova

