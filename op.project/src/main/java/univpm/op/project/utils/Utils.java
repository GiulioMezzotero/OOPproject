package univpm.op.project.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import univpm.op.project.data.Data;


public abstract class Utils {
	
	public static JSONObject parseStringToJson(String str) throws ClassCastException, ParseException
	{
		JSONParser parser = new JSONParser();
		JSONObject jsonObj;
		
		jsonObj = (JSONObject) parser.parse(str);
		
		return jsonObj;
		
	}
	
	public static void downloadFile(String url, String file)
	{
		if( url == null || file == null || url == "" || file == "")
			return;
		
		try
		{
			byte buffer[] = new byte[2048];
		    int Rbytes;
		    
		    System.out.println("Effettuiamo il download del file");
		    
		    URL urlObj = null;
			HttpURLConnection con = null;
			String location;
			
			location = url;
			
			while( location != null )
			{
				urlObj = new URL( location );
				con = (HttpURLConnection) urlObj.openConnection();
				location = con.getHeaderField("Location");
			}
			
		    
			InputStream datasetStream = con.getInputStream();
			BufferedInputStream inputStream = new BufferedInputStream( datasetStream  );
			
			FileOutputStream fileOutputStream = new FileOutputStream( file );
			
		    while ((Rbytes = inputStream.read(buffer, 0, 2048)) != -1) {
		        fileOutputStream.write(buffer, 0, Rbytes);
		    }
		    
		    System.out.println("Download effettuato");
		    
		    datasetStream.close();
		    fileOutputStream.close();
		    inputStream.close();
			
		    
		} catch (IOException e)
		
		{
			System.err.println("Errore");
			System.exit(-1);
		}
	}

	public static List<String> getRightFields() {
		
		int annoMinimo = Data.getAnnoMinimo();
		int annoMassimo = Data.getAnnoMassimo();
		
		List<String> rightFields = new ArrayList<String>();
		rightFields.add("indic_bt");
		rightFields.add("nace_r2");
		rightFields.add("s_adj");
		rightFields.add("unit");
		rightFields.add("country");
		
		for(int y = annoMinimo; y < annoMassimo + 1 ; y++)
		{
			rightFields.add( String.valueOf(y) );	
		}
		
		return rightFields;
}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static JSONObject validFilter( JSONObject JSONfilter )
	{
		int annoMinimo = Data.getAnnoMinimo();
		int annoMassimo = Data.getAnnoMassimo();
		
		List<String> rightFields = Utils.getRightFields();
		
		JSONObject filteredData = new JSONObject();
		
		Object[] key = JSONfilter.keySet().toArray();
		
		for( int i = 0; i < key.length; i++ )
		{
			Object k = key[i];
			
			if(! (k instanceof String ) )
			{
				filteredData.put("ERRORE", "Ci sono delle chiavi non valide");
				filteredData.put("ChiaveNonValida", k);
				return filteredData;
			}
			
			String filterAttribute = (String)k;
			
			try {
			
			
				int keyNumber = Integer.parseInt( filterAttribute );
				if( annoMinimo <= keyNumber && keyNumber <= annoMassimo ) {
					
					Object object = JSONfilter.get(k);
					
					if(!( object instanceof JSONObject) )
					{
						filteredData.put("Errore", "Filtro non valido.");
						filteredData.put("ChiaveNonValida", object);
						return filteredData;
					}
					
					JSONObject filterJSON;
					filterJSON = (JSONObject) object;
					
					Object[] inner_key = filterJSON.keySet().toArray();
					for( int j = 0; j < inner_key.length; j++ )
					{
						Object keysInner = inner_key[j];
						
						if(! (keysInner instanceof String ) )	
						{
							filteredData.put("ERRORE", "Ci sono delle chiavi non valide");
							filteredData.put("ChiaveNonValida", keysInner);
							return filteredData;
						}
						
						String keysInner_string = (String)keysInner;
						
						switch (keysInner_string) {
						
						case "$gt": case "$gte": case "$lt": case "$lte":
							
							if( !( filterJSON.get(keysInner) instanceof Double || filterJSON.get(keysInner) instanceof Long) )
							{
								filteredData.put("ERRORE", "Non posso applicare il filtro \""+keysInner_string+"\" con valore \""+String.valueOf(filterJSON.get(keysInner))+"\" al campo \""+filterAttribute+"\".");
								filteredData.put("ChiaveNonValida", filterJSON.get(keysInner));
								return filteredData;									
							}
							continue;
							
						case "$bt":
							
							if( filterJSON.get(keysInner) instanceof JSONArray )
							{
								if( ((JSONArray)filterJSON.get(keysInner)).size() == 2 &&
                                         (
											((JSONArray)filterJSON.get(keysInner)).toArray()[0] instanceof Long || ((JSONArray)filterJSON.get(keysInner)).toArray()[0] instanceof Double ) && 
                                         ( 
											((JSONArray)filterJSON.get(keysInner)).toArray()[1] instanceof Long || ((JSONArray)filterJSON.get(keysInner)).toArray()[1] instanceof Double ) )
								
								{ continue; }
								
								filteredData.put("ERRORE", "Non è possibile applicare il filtro \""+keysInner_string+"\" con valore \""+String.valueOf(filterJSON.get(keysInner))+"\" al campo \""+filterAttribute+"\".");
								filteredData.put("ChiaveNonValida", filterJSON.get(keysInner));
								return filteredData;	
							}

							filteredData.put("ERRORE", "Non è possibile applicare il filtro \""+keysInner_string+"\" con valore \""+String.valueOf(filterJSON.get(keysInner))+"\" al campo \""+filterAttribute+"\".");
							filteredData.put("ChiaveNonValida", filterJSON.get(keysInner));
							return filteredData;									
							

							
						default:
							filteredData.put("Errore", "Non è possibile applicare il filtro \""+keysInner_string+"\" al campo \""+filterAttribute+"\".");
							filteredData.put("ChiaveNonValida", keysInner_string);
							return filteredData;
					}
				}
			}
				
		} catch(NumberFormatException e){ 
			
		}
			
			switch( filterAttribute )
			{
				case "$or": case "$and":
					
					JSONArray filterJSON_arr;
					if(! (JSONfilter.get(k) instanceof JSONArray ) )	
					{
						filteredData.put("ERRORE", "C'è un valore dell'oggetto non valido.");
						filteredData.put("ChiaveNonValida", JSONfilter.get(k));
						return filteredData;
					}
					
					
					filterJSON_arr = (JSONArray) JSONfilter.get(k);
					
					Object[] objectsInner = filterJSON_arr.toArray();
					for( int j = 0; j < objectsInner.length; j++ )
					{
						Object objectInner = objectsInner[j];
						JSONObject innerObject;
						if(! (objectInner instanceof JSONObject ) )	
						{
							filteredData.put("ERRORE", "C'è un valore dell'oggetto non valido.");
							filteredData.put("ChiaveNonValida", objectInner);
							return filteredData;
						}
						
						innerObject = (JSONObject)objectInner;
						
						Object[] keysInner = innerObject.keySet().toArray();
						for( int t = 0; t < keysInner.length; t++ )
						{
							if(! (keysInner[t] instanceof String ) )	
							{
								filteredData.put("ERRORE", "Ci sono delle chiavi non valide");
								filteredData.put("ChiaveNonValida", keysInner);
								return filteredData;
							}
							
							
							if( ! rightFields.contains(keysInner) )
							{
								filteredData.put("ERRORE", "Chiave non presente tra gli attributi");
								filteredData.put("ChiaveNonValida", keysInner);
								return filteredData;
							}
						}
						
					}
					break;
			
				case "indic_bt": case "nace_r2": case "s_adj": case "unit": 
						JSONObject filterJson;
						if(! (JSONfilter.get(k) instanceof JSONObject ) )	
						{
							filteredData.put("ERRORE", "C'è un valore dell'oggetto non è valido");
							filteredData.put("ChiaveNonValida", JSONfilter.get(k));
							return filteredData;
						}
						
						filterJson = (JSONObject) JSONfilter.get(k);
						
						Object[] keysInner = filterJson.keySet().toArray();
						for( int j = 0; j < keysInner.length; j++ )
						{
							Object keyInner = keysInner[j];
							
							if(! (keyInner instanceof String ) )	
							{
								filteredData.put("ERRORE", "Almeno una chiave dell'oggetto non è valida.");
								filteredData.put("ChiaveNonValida", keyInner);
								return filteredData;
							}
							String keyInner_string = (String)keyInner;
							switch(keyInner_string)
							{
								case "$not":
									if( !( filterJson.get(keyInner) instanceof String) )
									{
										filteredData.put("ERRORE", "Non è possibile applicare il filtro \""+keyInner_string+"\" con valore \""+String.valueOf(filterJson.get(keyInner))+"\" al campo \""+filterAttribute+"\".");
										filteredData.put("ChiaveNonValida", filterJson.get(keyInner));
										return filteredData;									
									}
									continue;
									
								case "$in": case "$nin":
									if( !( filterJson.get(keyInner) instanceof JSONArray) )
									{
										filteredData.put("ERRORE", "Non è possibile applicare il filtro \""+keyInner_string+"\" con valore \""+String.valueOf(filterJson.get(keyInner))+"\" al campo \""+filterAttribute+"\".");
										filteredData.put("ChiaveNonValida", filterJson.get(keyInner));
										return filteredData;									
									}
									continue;
								
								default:
									filteredData.put("ERRORE", "Non è possibile applicare il filtro \""+keyInner_string+"\" al campo \""+filterAttribute+"\".");
									filteredData.put("ChiaveNonValida", keyInner_string);
									return filteredData;
							}
						}
					
					break;
			}
		}
				
		filteredData.put("Successo", true);
		
		return filteredData;
	}

	public static Map<String, Integer> setIncKey(Map<String, Integer> hashmap, String key)
	{

		if(hashmap.containsKey(key)) {
			Object oldValue = hashmap.get(key);
			Integer newValue = (Integer)oldValue + 1;
			hashmap.remove(key);
			hashmap.put(key, newValue);
		}
		else {
			hashmap.put(key, new Integer(1));
		}
		
		return hashmap;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJSONFromHashMap(Map<String, Integer> hashmap)
	{
		JSONObject json = new JSONObject();

		for (String key : hashmap.keySet())
		{
	        Integer value = hashmap.get(key);
	        json.put(key, value);
	    }
		
		return json;
	}
}
