package mopgenerate.redundancy;

import mopgenerate.MopGen;
import mopgenerate.util.MopCommen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class RedundancyGen implements MopGen {
    HashMap<String,String> hashMap;

    public RedundancyGen(HashMap <String,String> hashMap){
        this.hashMap = hashMap;
    }
    @Override
    public void Gen(String path, Properties properties) throws IOException {
        writeSelect(path, properties);
        writeUpdate(path, properties);
    }

    private void writeSelect(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "select.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("SelectResults() {");
        fileWriter.write("\r\n");
        writeSelectResult(fileWriter, properties, mopCommen);
        writeRequestService(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "selectResult => (*)requestService\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeUpdate(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "update.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("UpdateState() {");
        fileWriter.write("\r\n");
        writeSelectResult(fileWriter, properties, mopCommen);
        writeUpdateState(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "updateState => (*)selectResult\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeRequestService(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_requestService").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("requestService");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_requestService").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("requestService");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("requestService"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Request Service---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeSelectResult(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_selectResult").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("selectResult");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_selectResult").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("selectResult");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("selectResult"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Select Result---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeUpdateState(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_updateState").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("updateState");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_updateState").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("updateState");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
        fileWriter.write(properties.getProperty("updateState"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Update State---");
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
