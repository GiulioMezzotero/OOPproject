package univpm.op.project.controller;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import univpm.op.project.Data;

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
}
