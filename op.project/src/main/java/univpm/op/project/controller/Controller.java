package univpm.op.project.controller;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import univpm.op.project.data.Data;
import univpm.op.project.entity.Entity;
import univpm.op.project.exception.InvalidFilterException;
import univpm.op.project.utils.Utils;

@RestController
public class Controller {

	@RequestMapping("/")
	public String index()
    {
    	return "Dataset size: " + Data.getData().size() + " entity.";
    }

    @RequestMapping(value="/full", produces="application/json")
    public JSONObject getWholeData()
    {
    	return Data.getJSONData();
    }

    @RequestMapping(value="/getMetadata", produces="application/json")
    public String getMetadata()  
    {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonSchemaGenerator generatorSchema = new JsonSchemaGenerator(objectMapper);
		JsonSchema schema;
		try {
			schema = generatorSchema.generateSchema(Entity.class);
			return objectMapper.writeValueAsString(schema);
		} catch (JsonMappingException e) {
			JSONObject obj = new JSONObject();
			obj.put("Errore", e.getMessage());
			return obj.toString();
		} catch (JsonProcessingException e) {
			JSONObject obj = new JSONObject();
			obj.put("Errore", e.getMessage());
			return obj.toString();
		}		
    }

    @RequestMapping(value="/getAnalytics", produces="application/json")
    public JSONObject getAnalytics()
	{
		return Data.DataAnalytic( Data.getData() );
	}
	
    /**
     * Questa funzione restiuisce un insieme di dati filtrati in base al filtro che imposta l'utente.
     * @param filter Il filtro in JSON passato con richiesta POST.
     * @return L'insieme delle analisi sui dati filtrati.
     */
    @RequestMapping( value = "/getAnalytics/filtered", method=RequestMethod.POST, produces="application/json" )
	public JSONObject getFilteredAnalytics(@RequestBody(required = false) String filter)
	{
		if( filter == null )
		{
			return Data.DataAnalytic( Data.getData() );
		}
		
		List<Entity> data = new ArrayList<Entity>();
		
		JSONParser parser = new JSONParser();
		JSONObject filteredJSON;
		JSONObject jsonFilters;
		
		try {
			jsonFilters = (JSONObject) parser.parse(filter);
		} catch (ClassCastException e) {
			filteredJSON = new JSONObject();
			filteredJSON.put("Error", "ERRORE PARSING RICHIESTA FILTRO. JSON NON VALIDO.");
			return filteredJSON;
		} catch (ParseException e) {
			filteredJSON = new JSONObject();
			filteredJSON.put("Error", "ERRORE PARSING RICHIESTA FILTRO. JSON NON VALIDO.");
			return filteredJSON;
		}

		try {
			
			for( Entity e: Data.getData() )
			{
				if( e.filterApplication( jsonFilters ) )
				{
					data.add( e );
				}
			}
			
		}catch(InvalidFilterException e)
		{
			JSONObject json = new JSONObject();
			json.put("Error", e.getMessage());
			return json;
		}
		
		
		return Data.DataAnalytic( data );
	}
}
   

