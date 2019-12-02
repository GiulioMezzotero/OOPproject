package univpm.op.project.controller;

import org.json.simple.JSONObject;
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
	@RequestMapping(value = "/get/metadata", method = RequestMethod.GET, produces="application/json")
    public String metaGet( )
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

}
   

