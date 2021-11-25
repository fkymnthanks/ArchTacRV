#!/bin/bash

javamop -merge -keepRVFiles ckprbk.mop ckprbkbegin.mop ckprbkfail.mop
rv-monitor -merge -d classes/mop/ ckprbk.rvm ckprbkbegin.rvm ckprbkfail.rvm
javac classes/mop/MultiSpec_1RuntimeMonitor.java
javamopagent MultiSpec_1MonitorAspect.aj classes -n ckprbk -excludeJars