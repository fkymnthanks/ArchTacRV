package api;

import com.intellij.openapi.ui.Messages;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MethodApi {
    private static final String baseUrl = "http://127.0.0.1:5000";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(2000)
            .setSocketTimeout(10000).build();

    public static boolean getMethodInfo(String tacticName, HashMap<String, String> methodHashMap){
        System.out.println("------进入api------");
        //pingecho ckprbk heartbeat voting redundancy
        System.out.println("策略类型：" + tacticName);
        System.out.println("输入的方法信息：" + methodHashMap);

        // 参数
        List<Data> dataList = new ArrayList<>();
        // 遍历map
        for (Map.Entry<String, String> entry : methodHashMap.entrySet()) {
            dataList.add(new Data(entry.getValue(), entry.getKey()));
        }
        String paramJSONStr = MethodData.getJSONStr("lyyre@outlook.com",dataList);

        // http POST请求
        HttpPost httpPost = new HttpPost(baseUrl + "/" + tacticName +"/restoremethod");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setConfig(requestConfig);

        // JSON 格式
        StringEntity se = null;
        try {
            System.out.println("参数：" + paramJSONStr);
            se = new StringEntity(paramJSONStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        httpPost.setEntity(se);

        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 执行
            response = httpClient.execute(httpPost);

            // 获取内容
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            System.out.println(jsonObject);

            // 状态码
            String code = (String) jsonObject.get("code");
            if (code.equals("200")){
                System.out.println("success!");
                return true;
            } else if (code.equals("400")){
                System.out.println("error!");
                return false;
            } else {
                System.out.println("other wrong!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

