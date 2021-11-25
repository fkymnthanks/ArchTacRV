package mopgenerate.util;

public class HeartbeatCommand {

    public final static String mop
            = "javamop -merge -keepRVFiles heartbeat.mop heartbeatalive.mop heartbeatlost.mop";

    public final static String dir
            = "mkdir classes\\mop";

    public final static String rvm
            = "rv-monitor -merge -d classes/mop/ heartbeat.rvm heartbeatalive.rvm heartbeatlost.rvm";

    public final static String compile
            = "javac classes/mop/MultiSpec_1RuntimeMonitor.java";

    public final static String jar
            = "javamopagent MultiSpec_1MonitorAspect.aj classes -n heartbeat -excludeJars";

}
