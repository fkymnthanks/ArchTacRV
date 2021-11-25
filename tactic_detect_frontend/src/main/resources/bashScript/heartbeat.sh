#!/bin/bash

javamop -merge -keepRVFiles heartbeat.mop heartbeatalive.mop heartbeatlost.mop
rv-monitor -merge -d classes/mop/ heartbeat.rvm heartbeatalive.rvm heartbeatlost.rvm
javac classes/mop/MultiSpec_1RuntimeMonitor.java
javamopagent MultiSpec_1MonitorAspect.aj classes -n heartbeat -excludeJars