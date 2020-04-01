import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Convert {


    public static void main(String[] args) throws IOException {

        File file = new File("/Users/nero/Desktop/member.js");


        FileInputStream inputStream = new FileInputStream(file);



        byte[] bytes = new byte[100000];
        inputStream.read(bytes);


        String json = new String(bytes,"UTF-8");
        json = json.substring(14,json.length());


        JSONArray jsonArray = (JSONArray) JSON.parse(json);
        JSONArray des = new JSONArray();


        int index = 1;
        for (int i = 0 ; i < jsonArray.size() ; i++){
            JSONObject sourceObject = jsonArray.getJSONObject(i);
            String name = sourceObject.getString("name");


            JSONObject desObject = new JSONObject();
            desObject.put("name",name);
        }



        System.out.println(JSON.toJSONString(jsonArray));




    }





}
