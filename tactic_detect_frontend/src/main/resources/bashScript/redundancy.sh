#!/bin/bash

javamop -merge -keepRVFiles select.mop update.mop
rv-monitor -merge -d classes/mop/ select.rvm update.rvm
javac classes/mop/MultiSpec_1RuntimeMonitor.java
javamopagent MultiSpec_1MonitorAspect.aj classes -n redundancy -excludeJars