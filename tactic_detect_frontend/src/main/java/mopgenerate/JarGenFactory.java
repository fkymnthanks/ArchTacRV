package mopgenerate;

import mopgenerate.ckprbc.CkpRbkJarGen;
import mopgenerate.heartbeat.HeartbeatJarGen;
import mopgenerate.pingecho.PingEchoJarGen;
import mopgenerate.redundancy.RedundancyJarGen;
import mopgenerate.voting.VotingJarGen;

public class JarGenFactory {
    public JarGen getJarGen(String tactic){
        if(tactic.equals("pingecho")){
            return new PingEchoJarGen();
        }else if(tactic.equals("heartbeat")){
            return new HeartbeatJarGen();
        }else if(tactic.equals("voting")){
            return new VotingJarGen();
        }else if(tactic.equals("ckprbk")){
            return new CkpRbkJarGen();
        }else if(tactic.equals("redundancy")){
            return new RedundancyJarGen();
        }else{
            System.out.println("No Such Tactic!");
            return null;
        }
    }
}
