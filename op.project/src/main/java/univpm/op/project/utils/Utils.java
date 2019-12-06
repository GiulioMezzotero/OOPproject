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

/**
 * Classe astratta contenente dei metodi di utilità
 * @author Giulio Mezzotero e Giovanni Alessandro Clini
 *
 */
public abstract class Utils {
	
	/**
	 * Metodo che converte la stringa in oggetto JSON
	 * @param str Stringa da convertire
	 * @return Oggetto JSON dalla stringa passata come argomento
	 * @throws ClassCastException Genera un errore per il casting forzato
	 * @throws ParseException Genera un errore per il parsing
	 */
	public static JSONObject parseStringToJson(String str) throws ClassCastException, ParseException
	{
		JSONParser parser = new JSONParser();
		JSONObject jsonObj;
		
		jsonObj = (JSONObject) parser.parse(str);
		
		return jsonObj;
		
	}
	
	/**
	 * Metodo che scarica il file iniziale
	 * @param url Stringa passata con l'url del Dataset
	 * @param file Stringa passata con il nome del file dove scaricare il dataset
	 */
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
	
	/**
	 * Restituisce una lista di campi corretti per l'applicazione dei filtri
	 * @return Lista dei campi corretti
	 */
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
	
	/**
	 * Metodo che crea la chiave nel caso in cui questa non esista, 
	 * altrimenti incrementa il contatore di quella esistente
	 * @param hashmap HashMap contenente i dati delle stringhe 
	 * @param key Chiave per identificare i valori della HashMap 
	 * @return La HashMap con i nuovi valori 
	 */
	public static Map<String, Integer> setIncKey(Map<String, Integer> hashmap, String key)
	{

		if(hashmap.containsKey(key)) {
			Object oldValue = hashmap.get(key);
			Integer newValue = (Integer)oldValue + 1;
			hashmap.remove(key);
			hashmap.put(key, newValue);
		}
		else {
			hashmap.put(key, 1);
		}
		
		return hashmap;
	}

	/**
	 * Metodo che restituisce un oggetto JSON dalla HashMap 
	 * @param hashmap La HashMap da cui prendere i valori per costruire l'oggetto JSON 
	 * @return Oggetto JSON 
	 */
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
