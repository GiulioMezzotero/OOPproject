package univpm.op.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import univpm.op.project.data.Data;
import univpm.op.project.data.StringName;
import univpm.op.project.utils.Utils;

@SpringBootApplication
public class Application {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {
	
			Utils.downloadFile(StringName.URL, StringName.NOMEFILE_JSON);
			
			String stringaJson = "";
			
			try {
				File jsonFile = new File(StringName.NOMEFILE_JSON);
				Scanner jsonFileReader = new Scanner(jsonFile);
				
				while (jsonFileReader.hasNextLine())
				{
					stringaJson += jsonFileReader.nextLine();
				}
				jsonFileReader.close();
				
			} catch (FileNotFoundException e)
			{
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			JSONObject jsonRisposta;
			
			try {
				jsonRisposta = Utils.parseStringToJson(stringaJson);
			} catch (ClassCastException e) {
				System.out.println("Errore nel casting della classe.");
				return;
			} catch (ParseException e) {
				System.out.println("Json di risposta non valido.");
				return;
			}
			
			String urlTsv = (String) ((JSONObject) ( (JSONArray) ( (JSONObject) jsonRisposta.get("result") ).get("resources") ).get(1)).get("url");
			
			
			Utils.downloadFile(urlTsv, StringName.NOMEFILE_TSV);
			
			Data.dataParsing( StringName.NOMEFILE_TSV );
			
		    SpringApplication.run(Application.class, args);
	}

}
