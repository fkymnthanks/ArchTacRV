package api;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodData{
    String user_email;
    List<Data> data;

    public MethodData(String user_email, List<Data> data) {
        this.user_email = user_email;
        this.data = data;
    }

    public static String getJSONStr(String email, List<Data> dataList){
        MethodData methodData = new MethodData(email, dataList);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_email", methodData.user_email);
        paramMap.put("data", methodData.data);
        String paramStr = JSONObject.toJSONString(paramMap);
        return paramStr;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}


