package univpm.op.project;

import java.util.Hashtable;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings({ "serial", "rawtypes" })
public class HTCustom<T1, T2> extends Hashtable {
	
	@SuppressWarnings("unchecked")
	public void set (T1 key, T2 newValue) {
		this.remove(key);
		this.put(key, newValue);
	}

	@SuppressWarnings("unchecked")
	public void setIncKey(T1 key) {
		
        if(!(key instanceof String)) return;
		
		if(this.containsKey(key)) {
			Object oldValue = this.get(key);
			if(!(oldValue instanceof Integer)) return;
			Integer newValue = (Integer)oldValue;
			newValue++;
			this.set(key, (T2) newValue);
		}
		else {
			this.set(key, (T2) new Integer(1));
		}
		
	}

	@SuppressWarnings("unchecked")
	public JSONArray getJSONValues() {
		
		JSONArray json = new JSONArray();
		Set<String> setkey = this.keySet();
		
		for (String key : setkey ) {
			JSONObject json_obj = new JSONObject ();
			json_obj.put("Valore", key);
			json_obj.put("Conteggio", this.get(key));
			json.add(json_obj);
		}	
		return json;
	}

	
}
