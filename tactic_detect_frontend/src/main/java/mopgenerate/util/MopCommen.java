package mopgenerate.util;

public class MopCommen {

    public final static String[] head = new String[]{
            "package mop",
            "",
            "import java.io.*",
            "import java.util.*",
            ""
    };

    public final static String[] event_before = new String[]{
            "event ",
            " before() :"
    };

    public final static String[] event_after = new String[]{
            "event ",
            " after() :"
    };

    public final static String[] call = new String[]{
            "call(* ",
            "(..))"
    };

    public final static String[] msg = new String[]{
            "{ System.out.println(\"",
            "\"); }"
    };

    public final static String ltl = "ltl: ";

    public final static String violate = "@violation";
}
