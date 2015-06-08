package BBRCust;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import BBR.BBRUtil;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;

public class BBRVisitSerializer implements JsonSerializer<BBRVisit>{

	@Override
	public JsonElement serialize(BBRVisit src, Type typeOfSrc,	JsonSerializationContext context) {
		JsonObject j = new JsonObject();
		Gson gson = BBRUtil.gson();
		
 		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String s;

		j.addProperty("id", src.getId());
		
 		if (src.getTimeScheduled() != null)
 			s = df.format(src.getTimeScheduled());
 		else
 			s = "08:00";
 		j.addProperty("timeScheduled", s);
 		j.add("pos", gson.toJsonTree(src.getPos()));
 		j.add("user", gson.toJsonTree(src.getUser()));
 		j.addProperty("userName", src.getUserName());
 		j.addProperty("userContacts", src.getUserContacts());
 		j.addProperty("length", src.getLength());
 		j.add("spec", gson.toJsonTree(src.getSpec()));
 		j.add("procedure", gson.toJsonTree(src.getProcedure()));
 		j.addProperty("posTitle", src.getPosTitle());
 		j.addProperty("status", src.getStatus());

 		return j;
	}

}
