package mopgenerate.pingecho;

import mopgenerate.MopGen;
import mopgenerate.util.MopCommen;
import window.SampleToolWindow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class PingEchoGen implements MopGen {
    HashMap <String,String> hashMap;

    public PingEchoGen(HashMap <String,String> hashMap){
        this.hashMap = hashMap;
    }

    @Override
    public void Gen(String path, Properties properties) throws IOException {
        writePingEcho(path, properties);
        writePingEchoException(path, properties);
        writeEchoXorException(path, properties);
        writeExceptionTime(path, properties);
    }

    private void writePingEcho(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "pingecho.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("PingEcho() {");
        fileWriter.write("\r\n");
        writePing(fileWriter, properties, mopCommen);
        writeEcho(fileWriter, properties, mopCommen);
        writeException(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](echo => (*)ping)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writePingEchoException(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "pingechoexception.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("PingEchoException() {");
        fileWriter.write("\r\n");
        writeException(fileWriter, properties, mopCommen);
        writeHandleException(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](handleexception => (*)exception)\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeEchoXorException(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "echoxorexception.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("EchoXorException() {");
        fileWriter.write("\r\n");
        writePing(fileWriter, properties, mopCommen);
        writeEcho(fileWriter, properties, mopCommen);
        writeException(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "(echo xor exception) => (*)ping\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writeExceptionTime(String path, Properties properties) throws IOException {
        File mopfile =new File(path + File.separator + "exceptiontime.mop");
        refresh(mopfile);
        FileWriter fileWriter = new FileWriter(mopfile.getAbsolutePath(),true);
        MopCommen mopCommen = new MopCommen();
        for (String str : mopCommen.head){
            fileWriter.write(str);
            fileWriter.write("\r\n");
        }
        fileWriter.write("ExceptionTime() {");
        fileWriter.write("\r\n");
        writePing(fileWriter, properties, mopCommen);
        writeEcho(fileWriter, properties, mopCommen);
        writeException(fileWriter, properties, mopCommen);
        fileWriter.write("  " + mopCommen.ltl + "[](exception => (not echo S ping))\r\n");
        fileWriter.write("  " + mopCommen.violate + mopCommen.msg[0] + properties.getProperty("violate_msg") + mopCommen.msg[1]);
        fileWriter.write("\r\n");
        fileWriter.write("}");
        fileWriter.flush();
        fileWriter.close();
    }

    private void writePing(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_ping").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("ping");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_ping").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("ping");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
//        fileWriter.write(properties.getProperty("ping"));
        fileWriter.write(hashMap.get("ping"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Ping---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeEcho(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_echo").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("echo");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_echo").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("echo");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
//        fileWriter.write(properties.getProperty("echo"));
        fileWriter.write(hashMap.get("echo"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Echo---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeException(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_exception").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("exception");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_exception").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("exception");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
//        fileWriter.write(properties.getProperty("exception"));
        fileWriter.write(hashMap.get("exception"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Exception Happend---");
        fileWriter.write(mopCommen.msg[1]);
        fileWriter.write("\r\n");
    }

    private void writeHandleException(FileWriter fileWriter, Properties properties, MopCommen mopCommen) throws IOException {
        fileWriter.write("\t");
        if(properties.getProperty("join_handleexception").equals("before")){
            fileWriter.write(mopCommen.event_before[0]);
            fileWriter.write("handleexception");
            fileWriter.write(mopCommen.event_before[1]);
        }else if(properties.getProperty("join_handleexception").equals("after")){
            fileWriter.write(mopCommen.event_after[0]);
            fileWriter.write("handleexception");
            fileWriter.write(mopCommen.event_after[1]);
        }
        fileWriter.write("\r\n");
        fileWriter.write("\t\t");
        fileWriter.write(mopCommen.call[0]);
//        fileWriter.write(properties.getProperty("handleexception"));
        fileWriter.write(hashMap.get("handleexception"));
        fileWriter.write(mopCommen.call[1]);
        fileWriter.write("\r\n");
        fileWriter.write("\t\t\t");
        fileWriter.write(mopCommen.msg[0]);
        fileWriter.write("---Exception Handled---");
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
