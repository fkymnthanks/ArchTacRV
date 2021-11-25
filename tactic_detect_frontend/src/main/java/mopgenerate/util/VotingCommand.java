package mopgenerate.util;

public class VotingCommand {
    public final static String mop
            = "javamop -merge -keepRVFiles voting.mop votingfail.mop votingrequest.mop";

    public final static String dir
            = "mkdir classes\\mop";

    public final static String rvm
            = "rv-monitor -merge -d classes/mop/ voting.rvm votingfail.rvm votingrequest.rvm";

    public final static String compile
            = "javac classes/mop/MultiSpec_1RuntimeMonitor.java";

    public final static String jar
            = "javamopagent MultiSpec_1MonitorAspect.aj classes -n voting -excludeJars";
}
