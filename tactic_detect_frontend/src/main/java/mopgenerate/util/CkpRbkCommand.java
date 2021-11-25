package mopgenerate.util;

public class CkpRbkCommand {
    public final static String mop
            = "javamop -merge -keepRVFiles ckprbk.mop ckprbkbegin.mop ckprbkfail.mop";

    public final static String dir
            = "mkdir classes\\mop";

    public final static String rvm
            = "rv-monitor -merge -d classes/mop/ ckprbk.rvm ckprbkbegin.rvm ckprbkfail.rvm";

    public final static String compile
            = "javac classes/mop/MultiSpec_1RuntimeMonitor.java";

    public final static String jar
            = "javamopagent MultiSpec_1MonitorAspect.aj classes -n ckprbk -excludeJars";
}
