package univpm.op.project.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

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
		
		return null;
	}

}
