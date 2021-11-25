package mopgenerate;

import window.SampleToolWindow;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class GenerateMop {

    public static void Generate(String tacticName, String path, HashMap<String,String> hashMap) throws Exception {

        InputStream resourceAsStream = GenerateMop.class.getClassLoader().getResourceAsStream(tacticName + ".properties");

        MopGenFactory mopGenFactory = new MopGenFactory();

        JarGenFactory jarGenFactory = new JarGenFactory();

        Properties p = new Properties();

        //p.load(new BufferedInputStream(new FileInputStream(properties)));
        p.load(resourceAsStream);

        MopGen mopGen = mopGenFactory.getMopGen(tacticName, hashMap);

        JarGen jarGen = jarGenFactory.getJarGen(tacticName);

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows){
            path = path.replace("\\","\\\\");
        }

        mopGen.Gen(path, p);

        jarGen.GenJar(path);
    }

}
