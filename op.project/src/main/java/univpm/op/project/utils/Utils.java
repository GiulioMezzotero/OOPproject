package univpm.op.project.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import univpm.op.project.data.Data;
import univpm.op.project.data.StringName;


public abstract class Utils {
	
	public static void downloadFile()
	{
		try
		{
			byte buffer[] = new byte[2048];
		    int Rbytes;
		    
		    System.out.println("Effettuiamo il download del file");
			
			InputStream datasetStream = new URL( StringName.URL ).openStream();
			BufferedInputStream inputStream = new BufferedInputStream( datasetStream  );
			
			FileOutputStream fileOutputStream = new FileOutputStream( StringName.NOMEFILE );
			
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
	
	@SuppressWarnings("unchecked")
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
		}
		
		return filteredData;
		
}			

}
