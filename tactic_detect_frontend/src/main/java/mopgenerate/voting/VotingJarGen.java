package mopgenerate.voting;

import mopgenerate.JarGen;
import mopgenerate.util.VotingCommand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class VotingJarGen implements JarGen {
    @Override
    public void GenJar(String path) throws IOException, InterruptedException {
        File dir = new File(path);

        VotingCommand command = new VotingCommand();

        File f = new File(dir,"classes"+File.separator+"mop");
        if(!f.exists()) {
            f.mkdirs();
        }
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            Process p = Runtime.getRuntime().exec("cmd.exe /c " + command.mop, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + command.rvm, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + command.compile, null, dir);
            p.waitFor();
            p = Runtime.getRuntime().exec("cmd.exe /c " + command.jar, null, dir);
            p.waitFor();
        } else {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("bashScript/voting.sh");
            File file = new File(dir + File.separator + "voting.sh");
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

            Process p = Runtime.getRuntime().exec("/bin/sh voting.sh", null, dir);
            p.waitFor();
        }

        String[] list = {"MultiSpec_1RuntimeMonitor.java","voting.sh",
                "voting.mop","votingfail.mop","votingrequest.mop",
                "voting.rvm","votingfail.rvm","votingrequest.rvm"};
        for (String l:list) {
            JarGen.myDelete(path + File.separator + l);
        }
        JarGen.myDelete(path+File.separator+"classes");

        File file = new File(dir + File.separator + "MultiSpec_1MonitorAspect.aj");
        if (file.exists()){
            file.renameTo(new File(dir+File.separator+"voting.aj"));
        } else {
            System.out.println("文件重命名失败");
        }

        System.out.println("Jar Generated");
    }
}
