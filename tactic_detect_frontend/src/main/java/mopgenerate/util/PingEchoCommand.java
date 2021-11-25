package mopgenerate.util;

public class PingEchoCommand {

    public final static String mop
            = "javamop -merge -keepRVFiles pingecho.mop pingechoexception.mop echoxorexception.mop exceptiontime.mop";

    public final static String dir
            = "mkdir classes\\mop";

    public final static String rvm
            = "rv-monitor -merge -d classes/mop/ pingecho.rvm pingechoexception.rvm echoxorexception.rvm exceptiontime.rvm";

    public final static String compile
            = "javac classes/mop/MultiSpec_1RuntimeMonitor.java";

    public final static String jar
            = "javamopagent MultiSpec_1MonitorAspect.aj classes -n pingecho -excludeJars";
}
