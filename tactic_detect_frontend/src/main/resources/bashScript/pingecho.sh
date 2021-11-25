#!/bin/bash

javamop -merge -keepRVFiles pingecho.mop pingechoexception.mop echoxorexception.mop exceptiontime.mop
rv-monitor -merge -d classes/mop/ pingecho.rvm pingechoexception.rvm echoxorexception.rvm exceptiontime.rvm
javac classes/mop/MultiSpec_1RuntimeMonitor.java
javamopagent MultiSpec_1MonitorAspect.aj classes -n pingecho -excludeJars
