package mopgenerate.voting;

import mopgenerate.MopGen;
import mopgenerate.util.MopCommen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class VotingGen implements MopGen {
    HashMap<String,String> hashMap;

    public VotingGen(HashMap <String,String> hashMap){
        this.hashMap = hashMap;
    }
    @Override
    public void Gen(String path, Properties properties) throws IOException {
        writeVoting(path, properties);
        writeVotingFail(path, properties);
        writeVotingRequest(path, properties);
    }

    private void writeVoting(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "voting.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("Voting() {");
        fileWriter.write("\r\n");
        writeRequest(fileWriter, properties, mopCommen);
        writeVote(fileWriter, properties, mopCommen);
        writeFailService(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](failService => (not vote S request))\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeVotingFail(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "votingfail.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("VotingFail() {");
        fileWriter.write("\r\n");
        writeFailService(fileWriter, properties, mopCommen);
        writeStopService(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](stopService => (*) failService)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeVotingRequest(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "votingrequest.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("VotingRequest() {");
        fileWriter.write("\r\n");
        writeRequest(fileWriter, properties, mopCommen);
        writeVote(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](vote => (*) request)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeRequest(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_request").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("request");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_request").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("request");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("request"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Cluster Receive Request---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeVote(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_vote").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("vote");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_vote").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("vote");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("vote"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Cluster Voting Master---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeFailService(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_failService").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("failService");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_failService").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("failService");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("failService"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Cluster Service Fail---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeStopService(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_stopService").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("stopService");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_stopService").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("stopService");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("stopService"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Cluster Stop---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void refresh(File file) throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }else {
            file.delete();
            file.createNewFile();
        }
    }
}
