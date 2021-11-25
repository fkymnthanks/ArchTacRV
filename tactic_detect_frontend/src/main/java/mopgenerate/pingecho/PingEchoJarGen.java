package mopgenerate.pingecho;

import jnr.ffi.annotations.In;
import mopgenerate.JarGen;
import mopgenerate.util.PingEchoCommand;

import java.io.*;

public class PingEchoJarGen implements JarGen {

    @Override
    public void GenJar(String path) throws IOException, InterruptedException {
        File dir = new File(path);

        File f = new File(dir,"classes"+File.separator+"mop");
        if(!f.exists()) {
            f.mkdirs();
        }


        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            Process p = Runtime.getRuntime().exec("cmd.exe /c" + PingEchoCommand.mop, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + PingEchoCommand.rvm, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + PingEchoCommand.compile, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + PingEchoCommand.jar, null, dir);
            p.waitFor();
        } else {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("bashScript/pingecho.sh");
            File file = new File(dir + File.separator + "pingecho.sh");
            if(file.exists()){
                file.delete();
            }
            FileWriter fileWriter = new FileWriter(file);
            int len = -1;
            while ((len = resourceAsStream.read())!=-1){
                fileWriter.write(len);
                fileWriter.flush();
            }
            fileWriter.close();
            Process p = Runtime.getRuntime().exec("/bin/sh pingecho.sh", null, dir);
            p.waitFor();
        }

        String[] list = {"MultiSpec_1RuntimeMonitor.java","pingecho.sh",
                "pingecho.mop","pingechoexception.mop","echoxorexception.mop","exceptiontime.mop",
                "pingecho.rvm","pingechoexception.rvm","echoxorexception.rvm","exceptiontime.rvm"};
        for (String l:list) {
            JarGen.myDelete(path + File.separator + l);
        }
        JarGen.myDelete(path+File.separator+"classes");

        File file = new File(dir + File.separator + "MultiSpec_1MonitorAspect.aj");
        if (file.exists()){
            file.renameTo(new File(dir+File.separator+"pingecho.aj"));
        } else {
            System.out.println("文件重命名失败");
        }

        System.out.println("Jar Generated!");
    }
}
