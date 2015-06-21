package BBR;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import BBR.BBRUtil;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;

public class BBRDataElementSerializer implements JsonSerializer<BBRDataElement>{

	@Override
	public JsonElement serialize(BBRDataElement src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject j = new JsonObject();
		Gson gson = BBRUtil.gson();

 		Field[] allFields = src.getClass().getDeclaredFields();
	    for (Field field : allFields) {
	    	field.setAccessible(true);
	    	if (field.isAnnotationPresent(JsonFormat.class)) {
				JsonFormat ann = field.getAnnotation(JsonFormat.class);
	    		if (ann.includeToJson()) {
	    			String fieldName;
	    			if (ann.fieldName().equals(""))
	    				fieldName = field.getName();
	    			else
	    				fieldName = ann.fieldName(); 
	    			try {
	    				if (ann.format().equals(""))
    						j.add(fieldName, gson.toJsonTree(field.get(src)));
    					else
	    					if (java.util.Date.class.isAssignableFrom(field.getType())) {
	    						String s = null;
	    						if (field.get(src) != null) {
		    				 		SimpleDateFormat df = new SimpleDateFormat(ann.format());
		    						s = df.format(field.get(src));
	    						}
	    						j.addProperty(fieldName, s);
	    					}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
	    		}
	    	} else
				try {
					j.add(field.getName(), gson.toJsonTree(field.get(src)));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
	    }
 		return j;
	}

}
