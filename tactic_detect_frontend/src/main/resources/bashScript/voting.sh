#!/bin/bash

javamop -merge -keepRVFiles voting.mop votingfail.mop votingrequest.mop
rv-monitor -merge -d classes/mop/ voting.rvm votingfail.rvm votingrequest.rvm
javac classes/mop/MultiSpec_1RuntimeMonitor.java
javamopagent MultiSpec_1MonitorAspect.aj classes -n voting -excludeJars