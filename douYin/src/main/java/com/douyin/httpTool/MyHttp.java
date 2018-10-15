package com.douyin.httpTool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class MyHttp {
	public static String cookieVal="";  	  
    public static void Get(String url_get,String str_param_url,String charset,String cookie) throws IOException  {  
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码  
        //    String getURL = GET_URL + "?username="  + URLEncoder.encode("fat man", "utf-8");  
        String getURL = url_get + "?" + str_param_url;  
        URL getUrl = new URL(getURL);  
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，  
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection  
        HttpURLConnection connection = (HttpURLConnection) getUrl  
                .openConnection();  
  
        if (cookie != null) {  
            //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限  
            System.out.println("set cookieVal = [" + cookie + "]");  
            connection.setRequestProperty("Cookie", cookie);  
        }  
  
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到  
        // 服务器  
        connection.connect();  
        // 取得输入流，并使用Reader读取  
        BufferedReader reader = new BufferedReader(new InputStreamReader(  
                connection.getInputStream(),charset));  
        System.out.println("Contents of get request:");  
        String lines;  
        while ((lines = reader.readLine()) != null)  {  
             System.out.println(lines);  
        }  
        System.out.println(" "); 
        reader.close();  
        // 断开连接  
        connection.disconnect();  
    }  
  
    public static String Post(String url_post,String str_param_body,String charset,boolean b_flag,String cookies) throws IOException, JSONException  {  
        // Post请求的url，与get不同的是不需要带参数  
        URL postUrl = new URL(url_post);  
        // 打开连接  
        HttpURLConnection connection = (HttpURLConnection) postUrl  
                .openConnection();   
        if("" != cookies){  
            connection.setRequestProperty("Cookie", cookies);  
        }  
  
        connection.setDoOutput(true);    
        connection.setDoInput(true);   
        connection.setRequestMethod("POST");  
        connection.setUseCaches(false);  
        connection.setInstanceFollowRedirects(b_flag);  
        connection.setRequestProperty("Content-Type",  
                "application/x-www-form-urlencoded");    
        connection.connect();  
        DataOutputStream out = new DataOutputStream(connection  
                .getOutputStream());  
        out.writeBytes(str_param_body);  
  
        out.flush();  
  
        // 取得cookie，相当于记录了身份，供下次访问时使用  
        //    cookieVal = connection.getHeaderField("Set-Cookie").split(";")[0]  
        cookieVal = connection.getHeaderField("Set-Cookie");  
  
        out.close(); // flush and close  
        BufferedReader reader = new BufferedReader(new InputStreamReader(  
                connection.getInputStream(),charset));  
        String line; 
        String rsl=""; 
        while ((line = reader.readLine()) != null)  {  
        	rsl = rsl + line;
        }  
        
        //jObject.append("cookie", cookieVal);
        if(cookieVal=="") {
        	return rsl;
        }else {
        	JSONObject jObject = new JSONObject(rsl);
        	jObject.accumulate("cookie", cookieVal);
            reader.close();  
            connection.disconnect();  
            return jObject.toString();
        }
          
    }  
    
 
}
