package BBRClientApp;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;

public class BBRParams {
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
	
	private String clear(String s) {
		if (s == null)
			return "";
		return s.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("#amp#", "&");
	}
	
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
			if (param.length > 0) {
				String prm = "";
				String vle = "";
				if (param[0].endsWith("[]")) {
					prm = param[0].substring(0, param[0].length() - 2);
					vle = parameters.get(prm);
					if (param.length >= 2) { 
						if (vle == null)
							vle = clear(param[1]);
						else
							vle = vle + "," + clear(param[1]);
					}
				}
				else {
					prm = param[0];
					if (param.length >= 2) { 
						vle = clear(param[1]);
					}
				}
				parameters.put(prm, vle);
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

	public Hashtable<Integer, Hashtable<String, String>> getArray(String param) {
		Hashtable<Integer, Hashtable<String, String>> res = new Hashtable<Integer, Hashtable<String, String>>();
		
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
				Hashtable<String, String> element;
				if (res.get(index) == null)
					element = new Hashtable<String, String>();
				else
					element = res.get(index);
				element.put(v, parameters.get(key));
				res.put(index, element);
			}
		}
		
		return res;
	}

	public Hashtable<String, String> getList(String param) {
		Hashtable<String, String> res = new Hashtable<String, String>();
		
		for (String key : parameters.keySet()) {
			if (key.startsWith(param + "[")) {
				String k = "";
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
				res.put(k, parameters.get(key));
			}
		}
		
		return res;
	}

}