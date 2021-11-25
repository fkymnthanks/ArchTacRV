package mopgenerate.util;

public class RedundancyCommand {

    public final static String mop
            = "javamop -merge -keepRVFiles select.mop update.mop";

    public final static String dir
            = "mkdir classes\\mop";

    public final static String rvm
            = "rv-monitor -merge -d classes/mop/ select.rvm update.rvm";

    public final static String compile
            = "javac classes/mop/MultiSpec_1RuntimeMonitor.java";

    public final static String jar
            = "javamopagent MultiSpec_1MonitorAspect.aj classes -n redundancy -excludeJars";
}
