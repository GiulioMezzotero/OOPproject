package univpm.op.project.data;

import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import univpm.op.project.entity.Entity;
import univpm.op.project.utils.Utils;
import univpm.op.project.utils.NumericAnalysis;

	/**
	 * Classe per la gestione dei dati in formato TSV
	 * @author Giulio Mezzotero e Giovanni Alessandro Clini
	 *
	 */
	public class Data {
		
		public static List<Entity> data = new ArrayList<Entity>();
	
		/**
		 * Metodo per poter aggiungere i dati di una nuova entità alla lista
		 * @param e Dati della nuova entità da aggiungere
		 */
		public static void addEntity(Entity e) {
			data.add(e);
		}
	
	/**
	 * Metodo per la restituzione dei dati delle entità in JSON
	 * @return Dati delle entità in JSON
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJSONData() {
		
			JSONObject dataJSONObj = new JSONObject();
			JSONArray dataJSONArr = new JSONArray();
	
			for (Entity e : Data.getData())  //for each: riempio gli oggetti e inserisco nell'array
			{
				JSONObject entityObject = new JSONObject();
	
				entityObject.put("indic_bt", e.getIndic() );
				entityObject.put("nace_r2", e.getNace() );
				entityObject.put("s_adj", e.getAdj() );
				entityObject.put("unit", e.getUnit() );
				entityObject.put("country", e.getCountry() );
	
	
				JSONObject dataObj = new JSONObject();  //indici: lista degli NData
				
				Map<Integer, Double> indexes = e.getIndexes();
				
				for(Integer key : indexes.keySet())
				{
					dataObj.put(key, indexes.get(key));	
				}
				
				
	
				entityObject.put("Data", dataObj );
	
				dataJSONArr.add(entityObject);	
			}
	
	
			dataJSONObj.put("EntityData", dataJSONArr);
			dataJSONObj.put("Count", Data.getData().size() );
			dataJSONObj.put("Success", true);
	
			return dataJSONObj;
		}
	
	
	/**
	 * Metodo che restituisce la lista delle entità inserite 
	 * @return Lista delle entità inserite
	 */
	public static List<Entity> getData() {
		
		return data;
	}

	
/**
 * Metodo che carica in memoria e parsa i dati dal file TSV scaricato
 * @param file File da parsare
 */
	public static void dataParsing( String file )
	{
		
		System.out.println("PARSING DEL FILE \""+file+"\".");
		
		try {
			FileReader readingData = new FileReader( file );
			BufferedReader bufferedReader = new BufferedReader( readingData );

			String[] entityData;
			String[] headingData;

			headingData = bufferedReader.readLine().split("[,\\t]");  //divide una stringa in un array per ogni virgola o tab incontrato

            for(String lineData; (lineData = bufferedReader.readLine()) != null; )
            {
            	Entity e = new Entity();
            	Map<Integer, Double> datiAnnuali = new HashMap<Integer, Double>();

            	entityData = lineData.split("[,\\t]");
            	
            	if(entityData.length != headingData.length) continue;

            	e.setIndic( entityData[0].trim() );
            	e.setNace( entityData[1].trim() );
                e.setAdj( entityData[2].trim() );
                e.setUnit( entityData[3].trim() );
                e.setCountry( entityData[4].trim() );


                int i;
            	for( i = 5; i < headingData.length && i < entityData.length ; i++ )
            	{
            		if( !entityData[i].trim().split(" ")[0].equals(":") )
            		{
            			datiAnnuali.put(Integer.parseInt( headingData[i].trim() ), Double.parseDouble( entityData[i].trim().split(" ")[0] ));
            		} else {
            			datiAnnuali.put(Integer.parseInt( headingData[i].trim() ), (double) 0);
            		}
            	}
            	
            	e.setIndexes(datiAnnuali);

            	Data.addEntity(e);
            }

            bufferedReader.close();
            readingData.close();

		} catch (IOException i) {
			System.err.println("ERRORE");
			i.printStackTrace();			
		}
		System.out.println("PARSING EFFETTUATO");

	}

	/**
	 * Metodo che restituisce l'anno più piccolo dai metadati 
	 * @return La costante int 1992
	 */
	public static int getAnnoMinimo()
	{
		return 1992;
	}
	
	/**
	 * Metodo che restituisce l'anno più grande dai metadati 
	 * @return La costante int 2018
	 */
	public static int getAnnoMassimo()
	{
		return 2018;
	}
	
	/**
	 * Metodo che analizza la lista di entità passata come parametro 
	 * restituendone un'analisi statistica
	 * @param data Lista da analizzare
	 * @return JSONObject contenente l'analisi
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject DataAnalytic( List<Entity> data )
	{
		if( data.size() == 0 )
		{
			JSONObject x = new JSONObject();
			x.put("ERRORE", "Nessun dato trovato");
			return x;
		}
		
		int annoMinimo = getAnnoMinimo();
		
		NumericAnalysis[] n = new NumericAnalysis[ data.get(0).getIndexes().size() ];
		for( int i = 0; i < n.length; i++ )
			n[i] = new NumericAnalysis();
		
		JSONObject analysisObj = new JSONObject();
		JSONArray analysisArr = new JSONArray();

		Map<String, Integer> indicbtHT = new HashMap<String, Integer>();
		Map<String, Integer> nacer2HT = new HashMap<String, Integer>();
		Map<String, Integer> sadjHT= new HashMap<String, Integer>();
		Map<String, Integer> unitHT = new HashMap<String, Integer>();
		Map<String, Integer> countryHT = new HashMap<String, Integer>();

		
		
		for (Entity e : data)
		{
		
			indicbtHT = Utils.setIncKey( indicbtHT, e.getIndic() );
			nacer2HT = Utils.setIncKey( nacer2HT, e.getNace() );
			sadjHT = Utils.setIncKey( sadjHT, String.valueOf( e.getAdj() ) );
			unitHT = Utils.setIncKey( unitHT, e.getUnit() );
			countryHT = Utils.setIncKey( countryHT, e.getCountry() );
			
			Map<Integer, Double> indexes = e.getIndexes();
			
			for(Integer key : indexes.keySet())
			{
				int index = key - Data.getAnnoMinimo();
				n[index].addValue( indexes.get(key) );	
			}
				
		}
		
		
		for (int i=0; i<n.length; i++)
		{
			n[i].calcDev();
		}
		
		
		JSONObject indicbtJSONData = new JSONObject();
		JSONObject nacer2JSONData = new JSONObject();
		JSONObject sadjJSONData = new JSONObject();
		JSONObject unitJSONData = new JSONObject();
		JSONObject countryJSONData = new JSONObject();
		
		indicbtJSONData.put("Attributo", "indic_bt");
		indicbtJSONData.put("Dati", Utils.getJSONFromHashMap(indicbtHT) );
		indicbtJSONData.put("TipoDato", "String" );

		nacer2JSONData.put("Attributo", "nace_r2");
		nacer2JSONData.put("Dati", Utils.getJSONFromHashMap(nacer2HT) );
		nacer2JSONData.put("TipoDato", "String" );

		sadjJSONData.put("Attributo", "s_adj");
		sadjJSONData.put("Dati", Utils.getJSONFromHashMap(sadjHT) );
		sadjJSONData.put("TipoDato", "String" );

		unitJSONData.put("Attributo", "unit");
		unitJSONData.put("Dati", Utils.getJSONFromHashMap(unitHT) );
		unitJSONData.put("TipoDato", "String" );

		countryJSONData.put("Attributo", "country");
		countryJSONData.put("Dati", Utils.getJSONFromHashMap(countryHT) );
		countryJSONData.put("TipoDato", "String" );

		analysisArr.add( indicbtJSONData );
		analysisArr.add( nacer2JSONData );
		analysisArr.add( sadjJSONData );
		analysisArr.add( unitJSONData );
		analysisArr.add( countryJSONData );
		
		
		for (int i=0; i<n.length; i++)
		{
			JSONObject dataObject = new JSONObject();
			JSONObject data_j = new JSONObject();
			dataObject.put("Attributo", String.valueOf( i + annoMinimo ) );
			data_j.put("Somma", n[i].getSum() );
			data_j.put("Conteggio", n[i].getCount() );
			data_j.put("Media", n[i].getAverage() );
			data_j.put("Minimo", n[i].getMin() );
			data_j.put("Massimo", n[i].getMax() );
			data_j.put("DeviazioneStandard", n[i].getStdDev() );
			
			dataObject.put("Dati", data_j);
			dataObject.put("TipoDato", "Numerico");
			analysisArr.add(dataObject);
		}
					
		
		analysisObj.put("Dati", analysisArr);
		return analysisObj;
	
	}

}
