package BBRAcc;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import BBR.BBRUtil;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;

public class BBRPoSSerializer implements JsonSerializer<BBRPoS>{

	@Override
	public JsonElement serialize(BBRPoS src, Type typeOfSrc,	JsonSerializationContext context) {
		JsonObject j = new JsonObject();
		Gson gson = BBRUtil.gson();
		
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String s;

		j.addProperty("id", src.getId());
		j.addProperty("title", src.getTitle());
		j.addProperty("locationDescription", src.getLocationDescription());
		if (src.getStartWorkHour() != null)
			s = df.format(src.getStartWorkHour());
		else
			s = "08:00";
		j.addProperty("startWorkHour", s);
		if (src.getEndWorkHour() != null)
			s = df.format(src.getEndWorkHour());
		else
			s = "21:00";
		j.addProperty("endWorkHour", s);
		j.add("locationGPS", gson.toJsonTree(src.getLocationGPS()));
		j.add("shop", gson.toJsonTree(src.getShop()));

 		return j;
	}

}
