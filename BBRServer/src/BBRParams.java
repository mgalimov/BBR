import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class BBRParams {
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
	
	private void readString(String queryString) {
		String[] params;
		String[] param;

		if (queryString == null)
			return;
		
		try {
			params = URLDecoder.decode(queryString, "UTF-8").split("&");
		} catch (UnsupportedEncodingException ex) {
			params = queryString.split("&");
		}
		
		for (int i = 0; i < params.length; i++) {
			param = params[i].split("=");
			if (param.length >= 2) {
				parameters.put(param[0], param[1]);
			} else
				if (param.length == 1) {
					parameters.put(param[0], "");
				}
		}		
	}
	
	public BBRParams(String queryString) {
		readString(queryString);
	}
		
	public BBRParams(BufferedReader reader) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			
		};

		readString(jb.toString());
	}
	
	public String get(String param) {
		return parameters.get(param);
	}
	
	public int getLength() {
		return parameters.size();
	}

	public List<Hashtable<String, String>> getArray(String param) {
		List<Hashtable<String, String>> res = new ArrayList<Hashtable<String, String>>();
		
		for (String key : parameters.keySet()) {
			if (key.startsWith(param + "[")) {
				String k = "";
				String v = "";
				boolean b = false;
				boolean e = false;
				int j;
				for (j = 0; (j < key.length()) && !e; j++) {
					if (key.charAt(j) == ']')
						e = true;
					if (b && !e)
						k += key.charAt(j);
					if (key.charAt(j) == '[')
						b = true;
				}
				b = false;
				e = false;
				for (; (j < key.length()) && !e; j++) {
					if (key.charAt(j) == ']')
						e = true;
					if (b && !e)
						v += key.charAt(j);
					if (key.charAt(j) == '[')
						b = true;
				}
				int index = Integer.parseInt(k);
				if (res.size() == 0)
					res.add(index, new Hashtable<String, String>());
				if (res.get(index) == null)
					res.add(index, new Hashtable<String, String>());
				Hashtable<String, String> element = res.get(index);
				element.put(v, parameters.get(key));
				res.set(index, element);
			}
		}
		
		return res;
	}

}