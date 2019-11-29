package univpm.op.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import univpm.op.project.utils.Utils;

public class Entity {

		public String indic_bt;
		public String nace_r2;
		public String s_adj;
		public String unit;
		public String country;
		public List<NData> indexes;

		Entity() {
			indexes = new ArrayList<NData>();
		}


		public void addIndexes(NData n) {
			indexes.add(n);
		}


		public String getIndic() {
			return indic_bt;
		}


		public void setIndic(String indic_bt) {
			this.indic_bt = indic_bt;
		}


		public String getNace() {
			return nace_r2;
		}


		public void setNace(String nace_r2) {
			this.nace_r2 = nace_r2;
		}


		public String getAdj() {
			return s_adj;
		}


		public void setAdj(String s_adj) {
			this.s_adj = s_adj;
		}


		public String getUnit() {
			return unit;
		}


		public void setUnit(String unit) {
			this.unit = unit;
		}


		public String getCountry() {
			return country;
		}


		public void setCountry(String country) {
			this.country = country;
		}


		public List<NData> getIndexes() {
			return indexes;
		}


		public void setIndexes(List<NData> indexes) {
			this.indexes = indexes;
		}
		
		
		@SuppressWarnings("unused")
		public boolean filterApplication(JSONObject JSONfilters)
		{
			
			for( Object keyfield_obj : JSONfilters.keySet() )
			{
				String field = (String)keyfield_obj;
				
				List<String> rightFields = Utils.getRightFields();
				
				if( !rightFields.contains( field ) ) return false;
					
				
				JSONObject fInfo = (JSONObject)JSONfilters.get(field);
				
				
				for(Object fType : fInfo.keySet() )
				{
					
					String filter = (String)fType;
						
					JSONArray fRange, fArr;
					JSONObject[] fObjectArr;
					Object generic_object;
					Object[] objectArr;
					String[] fRangeArrStr;
					Double[] fRangeArrDouble;
					Double fValue;
					String fString;
					boolean isTrue;
					
					switch (filter) 
					
					{
					
					//Logical Operators
					case "$and":
						fArr = (JSONArray)fInfo.get(fType);
						fObjectArr = (JSONObject[]) fArr.toArray();
						
						isTrue = true;
						for(JSONObject obj : fObjectArr)
						{
							Object[] keys = obj.keySet().toArray();
							for( Object t : keys )
							{
								isTrue = isTrue && this.equalValue((String)field, obj.get(t));
								if(!isTrue) break;
							}
						}
						
						if(!isTrue) return false;
						
						break;
						
					case "$or":
						fArr = (JSONArray)fInfo.get(fType);
						fObjectArr = (JSONObject[]) fArr.toArray();
						
						isTrue = false;
						for(JSONObject js : fObjectArr)
						{
							Object[] keys = js.keySet().toArray();
							for( Object k : keys )
							{
								isTrue = isTrue || this.equalValue((String)field, js.get(k));
								if(isTrue) break;
							}							
						}
						
						if(!isTrue) return false;
						
						break;       
					
					case "$not":
						fString = (String)fInfo.get(fType);
						
						isTrue = !( this.equalValue((String)filter, fString ) );

						if(!isTrue) return false;
						
						break;
						
					case "$nin":
						fRange = (JSONArray)fInfo.get(fType);
						objectArr = fRange.toArray();
						fRangeArrStr = Arrays.copyOf(objectArr, objectArr.length, String[].class);
						
						isTrue = !( this.equalValue( (String)field, fRangeArrStr ) );
						
						if(!isTrue) return false;
						
						break;
						
					case "$in":
						fRange = (JSONArray)fInfo.get(fType);
						objectArr = fRange.toArray();
						fRangeArrStr = Arrays.copyOf(objectArr, objectArr.length, String[].class);

						isTrue = this.equalValue( (String)field, fRangeArrStr );
						
						if(!isTrue) return false;
						
						break;
						
		         
						
					}
			

}
				
			}
			return false;
		}


		private boolean checkValue(String field, double min, double max) {
			// TODO Auto-generated method stub
			return false;
		}


		private boolean equalValue(String field, Object object) {
			// TODO Auto-generated method stub
			return false;
		}
}
