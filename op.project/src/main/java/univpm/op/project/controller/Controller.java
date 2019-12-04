package univpm.op.project.controller;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import univpm.op.project.data.Data;
import univpm.op.project.entity.Entity;
import univpm.op.project.utils.Utils;

@RestController
public class Controller {

	@RequestMapping("/")
	public String index()
    {
    	return "Dataset size: " + Data.getData().size() + " entity.";
    }

    @RequestMapping("/full")
    public JSONObject getWholeData()
    {
    	return Data.getJSONData();
    }

    @SuppressWarnings("unchecked")
    public String getMetadata( )
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

    public JSONObject getAnalytics()
	{
		return Data.DataAnalytic( Data.getData() );
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getFilteredAnalytics(String filter)
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
			filteredJSON.put("WARNING!", "CONFLITTO PARSING RICHIESTA FILTRO");
			return filteredJSON;
		} catch (ParseException e) {
			filteredJSON = new JSONObject();
			filteredJSON.put("WARNING!", "CONFLITTO PARSING RICHIESTA FILTRO");
			return filteredJSON;
		}

		JSONObject responseValidator = Utils.validFilter( jsonFilters );
		
		if( ! responseValidator.containsKey("SUCCESS!") )
			return responseValidator;
		
		for( Entity e: Data.getData() )
		{
			if( e.filterApplication( jsonFilters ) )
				data.add( e );
		}
		
		return Data.DataAnalytic( data );
	}
}
   

