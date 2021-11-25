package mopgenerate;

import mopgenerate.ckprbc.CkpRbkGen;
import mopgenerate.heartbeat.HeartbeatGen;
import mopgenerate.pingecho.PingEchoGen;
import mopgenerate.redundancy.RedundancyGen;
import mopgenerate.voting.VotingGen;

import java.util.HashMap;

public class MopGenFactory {

    public MopGen getMopGen(String tactic, HashMap<String,String> hashMap){
        if(tactic.equals("pingecho")){
            return new PingEchoGen(hashMap);
        }else if(tactic.equals("heartbeat")){
            return new HeartbeatGen(hashMap);
        }else if(tactic.equals("voting")){
            return new VotingGen(hashMap);
        }else if(tactic.equals("ckprbk")){
            return new CkpRbkGen(hashMap);
        }else if(tactic.equals("redundancy")){
            return new RedundancyGen(hashMap);
        }else {
            System.out.println("No Such Tactic!");
            return null;
        }
    }
}
