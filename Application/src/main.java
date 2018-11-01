import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.BanDAO;
import dao.DAOFactory;
import model.Ban;
import model.Card;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class main {

    public static void main(String[] args){

        String filePath = "/home/rafael/UEL_a3/DataBase/scrapy/Crawler/magic/liga-magic_23-06-2018.json";
        String file = null;
        try {
            file = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }



            Boolean aux = true;
            int i = 0;
            while (aux) {

                JSONArray jArray = null;
                JSONObject obj = null;
                try {
                    jArray = new JSONArray(file);
                    obj = jArray.getJSONObject(i);
                } catch (JSONException e) {
                    aux = false;
                }
                if(obj == null) aux = false;
                i++;
            }




    }
}
