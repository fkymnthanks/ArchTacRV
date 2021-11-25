package mopgenerate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public interface MopGen {

    void Gen(String path, Properties properties) throws IOException;

}
