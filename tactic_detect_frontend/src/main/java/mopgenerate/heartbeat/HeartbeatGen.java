package mopgenerate.heartbeat;

import mopgenerate.MopGen;
import mopgenerate.util.MopCommen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class HeartbeatGen implements MopGen {
    HashMap <String,String> hashMap;

    public HeartbeatGen(HashMap <String,String> hashMap){
        this.hashMap = hashMap;
    }

    @Override
    public void Gen(String path, Properties properties) throws IOException {
        writeHeartbeat(path, properties);
        writeHeartbeatAlive(path, properties);
        writeHeartbeatLost(path, properties);
    }

    private void writeHeartbeat(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "heartbeat.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("Heartbeat() {");
        fileWriter.write("\r\n");
        writeReceive(fileWriter, properties, mopCommen);
        writeLost(fileWriter, properties, mopCommen);
        writeUpdate(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "(update xor lost) => (*) receive\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeHeartbeatAlive(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "heartbeatalive.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("HeartbeatAlive() {");
        fileWriter.write("\r\n");
        writeAlive(fileWriter, properties, mopCommen);
        writeUpdate(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](alive <=> (*) update)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeHeartbeatLost(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "heartbeatlost.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("HeartbeatLost() {");
        fileWriter.write("\r\n");
        writeReceive(fileWriter, properties, mopCommen);
        writeLost(fileWriter, properties, mopCommen);
        writeUpdate(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](lost => (not update S receive))\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeReceive(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_receive").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("receive");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_receive").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("receive");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("receive"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Receive HeartBeat---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeAlive(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_alive").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("alive");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_alive").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("alive");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("alive"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---HeartBeat Alive---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeLost(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_lost").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("lost");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_lost").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("lost");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("lost"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---HeartBeat Lost---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeUpdate(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_update").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("update");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_update").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("update");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("update"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---HeartBeat Update---");
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
