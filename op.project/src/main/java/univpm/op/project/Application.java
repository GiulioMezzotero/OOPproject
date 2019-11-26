package univpm.op.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import univpm.op.project.utils.Utils;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		Utils.downloadFile("http://data.europa.eu/euodp/data/api/3/action/package_show?id=GeGKzwDc03b3j0olhD5DQ","Dataset.json");

		
		
		//Utils.downloadFile("","Dataset.tsv");
		
		SpringApplication.run(Application.class, args);
	}

}
