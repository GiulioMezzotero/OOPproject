package univpm.op.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import univpm.op.project.data.Data;
import univpm.op.project.data.StringName;
import univpm.op.project.utils.Utils;

@SpringBootApplication
public class Application {

	public static void main(String[] args)  {
	
			Utils.downloadFile();
			
			Data.DataParsing( StringName.NOMEFILE );
			
		    SpringApplication.run(Application.class, args);
	}

}
