package api;

public class Data {
    String method_name;
    String method_type;

    public Data(String method_name, String method_type) {
        this.method_name = method_name;
        this.method_type = method_type;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getMethod_type() {
        return method_type;
    }

    public void setMethod_type(String method_type) {
        this.method_type = method_type;
    }
}

