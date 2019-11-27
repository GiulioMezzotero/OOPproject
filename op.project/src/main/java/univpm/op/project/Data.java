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

		for (Entity e : Data.getData())
		{
			JSONObject entityObject = new JSONObject();

			entityObject.put("indic_bt", e.getIndic() );
			entityObject.put("nace_r2", e.getNace() );
			entityObject.put("s_adj", e.getAdj() );
			entityObject.put("unit", e.getUnit() );
			entityObject.put("country", e.getCountry() );


			JSONObject dataObj = new JSONObject();
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
		try {
			FileReader readingData = new FileReader( file );
			BufferedReader bufferedReader = new BufferedReader( readingData );

			String[] entityData;
			String[] headingData;

			headingData = bufferedReader.readLine().split("[,\\t]");

            for(String lineData; (lineData = bufferedReader.readLine()) != null; )
            {
            	Entity e = new Entity();


            	entityData = lineData.split("[,\\t]");

            	e.setIndic( entityData[0].trim() );
            	e.setNace( entityData[1].trim() );
                e.setAdj( entityData[2].trim() );
                e.setUnit( entityData[3].trim() );
                e.setCountry( entityData[4] );

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

}
