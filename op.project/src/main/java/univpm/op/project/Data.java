package univpm.op.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Data {
	
	public static List<Entity> data = new ArrayList<Entity>();

	public static void addEntity(Entity e) {
		data.add(e);
	}

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
			for (NData nd : e.getIndexes())
			{
				dataObj.put(nd.getYear(), nd.getValue());				
			}

			entityObject.put("Data", dataObj );

			dataJSONArr.add(entityObject);	
		}


		dataJSONObj.put("EntityData", dataJSONArr);
		dataJSONObj.put("Count", Data.getData().size() );
		dataJSONObj.put("Success", true);

		return dataJSONObj;
	}
	
	
	
	
public static List<Entity> getData() {
	
	return data;
}

	

	public static void DataParsing( String file )
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


            	entityData = lineData.split("[,\\t]");

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
                		NData n;
                		n = new NData( Integer.parseInt( headingData[i].trim() ), Double.parseDouble( entityData[i].trim().split(" ")[0] ) );
                		e.addIndexes( n );
            		} else {
            			NData n;
                		n = new NData( Integer.parseInt( headingData[i].trim() ), 0 );
                		e.addIndexes( n );
            		}
            	}
            	
            	for( ; i < headingData.length; i++ )
            	{
            		NData n;
            		n = new NData( Integer.parseInt( headingData[i].trim() ), 0 );
            		e.addIndexes( n );	
            	}
            

            	Data.addEntity(e);
            }

            bufferedReader.close();
            readingData.close();

		} catch (IOException i) {
			System.err.println("ERRORE");
			i.printStackTrace();			
		}
		System.out.println("PARSING EFFETTUATO");
		// FINE PARSING
	}

	
	public static int getAnnoMinimo()
	{
		return Data.getAnnoMinimo( Data.getData() );
	}
	
	
	public static int getAnnoMinimo( List<Entity> data )
	{
		int annoMinimo=3000;
		
		
		for (NData n : data.get(0).getIndexes() )
		{
			int year = n.getYear();
			if(annoMinimo > year) annoMinimo = year;
		}
		return annoMinimo;
		
}
	
	public static int getAnnoMassimo()
	{
		return Data.getAnnoMassimo( Data.getData() );
	}
	

	
	public static int getAnnoMassimo( List<Entity> data )
	{
		int annoMassimo = 0;		
		
		for (NData n : data.get(0).getIndexes() )
		{
			int year = n.getYear();
			if(annoMassimo < year) annoMassimo = year;
		}
		return annoMassimo;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static JSONObject DataAnalytic( List<Entity> data )
	{
		if( data.size() == 0 )
		{
			JSONObject x = new JSONObject();
			x.put("ERRORE", "Nessun dato trovato");
			return x;
		}
		
		int annoMinimo = Data.getAnnoMinimo( data );
		
		NumericAnalysis[] n = new NumericAnalysis[ data.get(0).getIndexes().size() ];
		for( int i = 0; i < n.length; i++ )
			n[i] = new NumericAnalysis();
		
		JSONObject analysisObj = new JSONObject();
		JSONArray analysisArr = new JSONArray();

		HTCustom<String, Integer> indicbtHT = new HTCustom<String, Integer>();
		HTCustom<String, Integer> nacer2HT = new HTCustom<String, Integer>();
		HTCustom<String, Integer> sadjHT= new HTCustom<String, Integer>();
		HTCustom<String, Integer> unitHT = new HTCustom<String, Integer>();
		HTCustom<String, Integer> countryHT = new HTCustom<String, Integer>();

		
		
		for (Entity e : data)
		{
		
			indicbtHT.setIncKey( e.getIndic() );
			nacer2HT.setIncKey( e.getNace() );
			sadjHT.setIncKey( String.valueOf( e.getAdj() ) );
			unitHT.setIncKey( e.getUnit() );
			countryHT.setIncKey( e.getCountry() );
			
			for (NData nd : e.getIndexes() )
			{
				int index = nd.getYear() - annoMinimo;
				n[index].addValue( nd.getValue() );				
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
		indicbtJSONData.put("Dati", indicbtHT.getJSONValues() );
		indicbtJSONData.put("TipoDato", "String" );

		nacer2JSONData.put("Attributo", "nace_r2");
		nacer2JSONData.put("Dati", nacer2HT.getJSONValues() );
		nacer2JSONData.put("TipoDato", "String" );

		sadjJSONData.put("Attributo", "s_adj");
		sadjJSONData.put("Dati", sadjHT.getJSONValues() );
		sadjJSONData.put("TipoDato", "String" );

		unitJSONData.put("Attributo", "unit");
		unitJSONData.put("Dati", unitHT.getJSONValues() );
		unitJSONData.put("TipoDato", "String" );

		countryJSONData.put("Attributo", "country");
		countryJSONData.put("Dati", countryHT.getJSONValues() );
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
			data_j.put("DeviazioneStandard", n[i].getDevstd() );
			
			dataObject.put("Dati", data_j);
			dataObject.put("TipoDato", "Numerico");
			analysisArr.add(dataObject);
		}
					
		
		analysisObj.put("Dati", analysisArr);
		return analysisObj;
	
	}

}
