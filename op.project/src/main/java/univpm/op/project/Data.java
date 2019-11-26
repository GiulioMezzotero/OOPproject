package univpm.op.project;

import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private static List<Entity> data = new ArrayList<Entity>();

	public static void addEntity(Entity e) {
		data.add(e);
	}
}
