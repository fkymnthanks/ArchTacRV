package mopgenerate.ckprbc;

import mopgenerate.MopGen;
import mopgenerate.util.MopCommen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class CkpRbkGen implements MopGen {
    HashMap <String,String> hashMap;

    public CkpRbkGen(HashMap <String,String> hashMap){
        this.hashMap = hashMap;
    }
    @Override
    public void Gen(String path, Properties properties) throws IOException {
        writeCkpRbk(path, properties);
        writeCkpRbkBegin(path, properties);
        writeCkpRbkFail(path, properties);
    }

    private void writeCkpRbk(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "ckprbk.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("CkpRbk() {");
        fileWriter.write("\r\n");
        writeCommit(fileWriter, properties, mopCommen);
        writeFailTask(fileWriter, properties, mopCommen);
        writeStoreCkp(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](commit => (not failTask S storeCkp))\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeCkpRbkBegin(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "ckprbkbegin.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("CkpRbkBegin() {");
        fileWriter.write("\r\n");
        writeNotifyCkp(fileWriter, properties, mopCommen);
        writeStoreCkp(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](storeCkp => (*)notifyCkp)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeCkpRbkFail(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "ckprbkfail.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("CkpRbkFail() {");
        fileWriter.write("\r\n");
        writeRecover(fileWriter, properties, mopCommen);
        writeFailTask(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](recover => (*)failTask)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeNotifyCkp(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_notifyCkp").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("notifyCkp");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_notifyCkp").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("notifyCkp");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("notifyCkp"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Notify Checkpoint---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeStoreCkp(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_storeCkp").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("storeCkp");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_storeCkp").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("storeCkp");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("storeCkp"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Store Checkpoint Status---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeCommit(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_commit").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("commit");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_commit").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("commit");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("commit"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Task Group Commit---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeRecover(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_recover").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("recover");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_recover").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("recover");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("recover"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Recover Checkpoint---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeFailTask(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_failTask").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("failTask");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_failTask").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("failTask");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("failTask"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Task Group Fail---");
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
