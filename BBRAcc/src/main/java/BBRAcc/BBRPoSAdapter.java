package BBRAcc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import BBR.BBRGPS;
import BBR.BBRUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class BBRPoSAdapter extends TypeAdapter<BBRPoS> {
     public BBRPoS read(JsonReader reader) throws IOException {
       if (reader.peek() == JsonToken.NULL) {
         reader.nextNull();
         return null;
       }

       BBRPoS pos = new BBRPoS();
       SimpleDateFormat df = new SimpleDateFormat("HH:mm");

       reader.beginObject();
       while (reader.hasNext()) {
    	 String name = reader.nextName();
    	 String value = reader.nextString();
         if (name.equals("id")) pos.setId(Long.parseLong(value));
         if (name.equals("title")) pos.setTitle(value);
         if (name.equals("locationDescription")) pos.setLocationDescription(value);
         if (name.equals("locationGPS")) pos.setLocationGPS(new BBRGPS(value));
         try {
        	 if (name.equals("startWorkHour")) pos.setStartWorkHour(df.parse(value));
        	 if (name.equals("endWorkHour")) pos.setEndWorkHour(df.parse(value));
         } catch (ParseException ex) {
         }
       }
       reader.endObject();
       return pos;
     }
     
     public void write(JsonWriter writer, BBRPoS value) throws IOException {
       if (value == null) {
         writer.nullValue();
         return;
       }
       
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String s;
		writer.beginObject();
		writer.name("id").value(value.getId());
		writer.name("title").value(value.getTitle());
		writer.name("locationDescription").value(value.getLocationDescription());
		writer.name("locationGPS").value(BBRUtil.gson.toJson(value.getLocationGPS()));
		if (value.getStartWorkHour() != null)
			s = df.format(value.getStartWorkHour());
		else
			s = "08:00";
		writer.name("startWorkHour").value(s);
		if (value.getEndWorkHour() != null)
			s = df.format(value.getEndWorkHour());
		else
			s = "21:00";
		writer.name("endWorkHour").value(s);
		writer.endObject();
     }
}
