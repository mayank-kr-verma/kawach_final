package nitp.navi.kawach_final;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by akezio on 18-02-2018.
 */

public class JsonHandler extends MainActivity2{
    JSONArray jsonArray;
    public JsonHandler(MainActivity2 mainActivity){
        JSONArray jsonArray=null;
    }

    public JSONArray reads(String s,Context context){

        try {
            InputStream is = context.getResources().getAssets().open(s);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray=new JSONArray(json);

        }catch (IOException e){

            e.printStackTrace();

        }catch (JSONException je){

            je.printStackTrace();

        }
        return jsonArray;

    }

}
