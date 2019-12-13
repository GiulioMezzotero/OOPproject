package univpm.op.project.entity;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import univpm.op.project.data.Data;
import univpm.op.project.exception.InvalidFilterException;
import univpm.op.project.utils.Utils;

/**
 * Classe che contiene le informazioni sull'entità
 * @author Giulio Mezzotero e Giovanni Alessandro Clini
 *
 */
public class Entity {

		public String indic_bt;
		public String nace_r2;
		public String s_adj;
		public String unit;
		public String country; 
		public Map<Integer, Double> indexes;

		/**
		 * Costruttore della Classe
		 */
		public Entity() {
			indexes = new HashMap<Integer, Double>();
		}

		/**
		 * Metodo che restituisce l'HashMap 
		 * @return HashMap
		 */
		public Map<Integer, Double> getIndexes() {
			return indexes;
		}
		
		/**
		 * Metodo che setta gli indici 
		 * @param indexes Indici contenuti nell'HashMap
		 */
		public void setIndexes(Map<Integer, Double> indexes) {
			this.indexes = indexes;
		}

		/**
		 * Metodo che restituisce indic_bt
		 * @return indic_bt
		 */
		public String getIndic() {
			return indic_bt;
		}

		/**
		 * Metodo che setta indic_bt
		 * @param indic_bt indic_bt che si intende impostare
		 */
		public void setIndic(String indic_bt) {
			this.indic_bt = indic_bt;
		}

		/**
		 * Metodo che restituisce nace_r2
		 * @return nace_r2
		 */
		public String getNace() {
			return nace_r2;
		}

		/**
		 * Metodo che setta nace_r2
		 * @param nace_r2 nace_r2 che si intende impostare
		 */
		public void setNace(String nace_r2) {
			this.nace_r2 = nace_r2;
		}

		/**
		 * Metodo che restituisce s_adj
		 * @return s_adj
		 */
		public String getAdj() {
			return s_adj;
		}

		/**
		 * Metodo che setta s_adj
		 * @param s_adj s_adj che si intende impostare
		 */
		public void setAdj(String s_adj) {
			this.s_adj = s_adj;
		}

		/**
		 * Metodo che restituisce unit
		 * @return unit
		 */
		public String getUnit() {
			return unit;
		}

		/**
		 * Metodo che setta unit
		 * @param unit unit che si intende impostare
		 */
		public void setUnit(String unit) {
			this.unit = unit;
		}

		/**
		 * Metodo che restituisce country
		 * @return country
		 */
		public String getCountry() {
			return country;
		}

		/**
		 * Metodo che setta country
		 * @param country country che si intende impostare
		 */
		public void setCountry(String country) {
			this.country = country;
		}
		
		/**
		 * Metodo che applica i filtri all'oggetto JSON 
		 * @param jsonFilters filtri in formato JSON
		 * @return true se il filtro è applicato correttamente all'oggetto, false altrimenti
		 * @throws InvalidFilterException Genera un errore per i filtri invalidi
		 */
		public boolean filterApplication(JSONObject jsonFilters) throws InvalidFilterException
		{
			
			for( Object keyfield_obj : jsonFilters.keySet() )
			{
				String field;
				try {
					field = (String)keyfield_obj;
				}catch(Exception e) {
					throw new InvalidFilterException("Chiave non alfanumerica.");
				}
				
				List<String> rightFields = Utils.getRightFields();
				
				if( !rightFields.contains( field ) && !field.equals("$and") && !field.equals("$or"))
				{
					throw new InvalidFilterException("Chiave non valida.");
				}
					
				
				
				JSONObject fInfo = null;
					
				JSONArray fRange, fArr;
				JSONObject[] fObjectArr;
				Object generic_object;
				Object[] objectArr;
				String[] fRangeArrStr;
				Double[] fRangeArrDouble;
				Double fValue;
				String fString;
				boolean isTrue;
				
			    //controlla se lo applica su valori String
				if(field.equals("indic_bt") || field.equals("nace_r2") || field.equals("s_adj") || field.equals("unit") || field.equals("country"))
				{
					try {
						fInfo = (JSONObject)jsonFilters.get(field);
						for(Object fType : fInfo.keySet() )
						{
						
					
							String filter = (String)fType;
							
							switch(filter) {
							
								case "$not":
									fString = (String)fInfo.get(filter);
									
									isTrue = !( this.equalValue((String)field, fString ) );
			
									if(!isTrue) return false;
									
									break;
									
								case "$nin":
									fRange = (JSONArray)fInfo.get(filter);
									objectArr = fRange.toArray();
									fRangeArrStr = Arrays.copyOf(objectArr, objectArr.length, String[].class);
									
									isTrue = !( this.containmentValue( (String)field, fRangeArrStr ) );
									
									if(!isTrue) return false;
									
									break;
									
								case "$in":
									fRange = (JSONArray)fInfo.get(filter);
									objectArr = fRange.toArray();
									fRangeArrStr = Arrays.copyOf(objectArr, objectArr.length, String[].class);
			
									isTrue = this.containmentValue( (String)field, fRangeArrStr );
									
									if(!isTrue) return false;
									
									break;
							}
							
						}
							
						}catch(Exception e) {
							throw new InvalidFilterException("Valore del filtro non valido.");  //problemi in fase di richiesta
						}
					

			       }else if( !field.equals("$or") && !field.equals("$and")) {
					
					try {

						fInfo = (JSONObject)jsonFilters.get(field);
					
						for(Object fType : fInfo.keySet() )
						{
							String filter = (String)fType;
		                      switch (filter) {
		                  	  
		 						case "$bt":
		 							fRange = (JSONArray)fInfo.get(filter);
		 							
		 							objectArr = fRange.toArray();
		 							fRangeArrDouble = Arrays.copyOf(objectArr, objectArr.length, Double[].class);
		 							
		 							isTrue = checkValue(field, Math.min(fRangeArrDouble[0], fRangeArrDouble[1]), Math.max(fRangeArrDouble[0], fRangeArrDouble[1]) );
		 							if(!isTrue) return false;
		 							break;
		 						
		 						case "$gt":
		 							generic_object = fInfo.get(filter);
		 							if( generic_object instanceof Long )
		 							{
		 								fValue = ((Long)generic_object).doubleValue();
		 							}else {
		 								fValue = (Double)generic_object;
		 							}
		 							isTrue = this.greaterValue(field, fValue);
		 							if(!isTrue) return false;
		 							break;
		 							
		 						case "$gte":
		 							generic_object = fInfo.get(filter);
		 							if( generic_object instanceof Long )
		 							{
		 								fValue = ((Long)generic_object).doubleValue();
		 							}else {
		 								fValue = (Double)generic_object;
		 							}
		 							
		 							isTrue = this.greaterValue(field, fValue);
		 							isTrue = isTrue || this.equalValue(field, fValue);
		 							if(!isTrue) return false;
		 							break;
		 							
		 						case "$lt":
		 							generic_object = fInfo.get(filter);
		 							if( generic_object instanceof Long )
		 							{
		 								fValue = ((Long)generic_object).doubleValue();
		 							}else {
		 								fValue = (Double)generic_object;
		 							}
		 							isTrue = this.lowerValue(field, fValue);
		 							if(!isTrue) return false;
		 							break;
		 							
		 						case "$lte":
		 							generic_object = fInfo.get(filter);
		 							if( generic_object instanceof Long )
		 							{
		 								fValue = ((Long)generic_object).doubleValue();
		 							}else {
		 								fValue = (Double)generic_object;
		 							}
		 							isTrue = this.lowerValue(field, fValue);
		 							isTrue = isTrue || this.equalValue(field, fValue);
		 							if(!isTrue) return false;
		 							break;
		                      }
						}
					} catch (Exception e) {
						throw new InvalidFilterException("Valore del filtro non valido.");
					}
			}else {
					try {
	
						fArr = (JSONArray)fInfo.get(field);
						fObjectArr = (JSONObject[]) fArr.toArray();
						
						switch (field)
						{
							case "$and":
								isTrue = true;
								for(JSONObject obj : fObjectArr)
								{
									Object[] keys = obj.keySet().toArray();
									for( Object t : keys )
									{
										isTrue = isTrue && this.equalValue((String)t, obj.get(t));
										if(!isTrue) break;
									}
									if(!isTrue) break;
								}
								
								if(!isTrue) return false;
								
								break;
								
							case "$or":
								isTrue = false;
								for(JSONObject js : fObjectArr)
								{
									Object[] keys = js.keySet().toArray();
									for( Object k : keys )
									{
										isTrue = isTrue || this.equalValue((String)k, js.get(k));
										if(isTrue) break;
									}							
								}
								
								if(!isTrue) return false;
								
								break;    
								
								
						}
					} catch (Exception e) {
						throw new InvalidFilterException("Valore del filtro non valido.");
					}
					
				}
				
			}
			return true;
		}
		

		/**
		 * Metodo che verifica se la variabile passata come argomento si trova all'interno dell'array
		 * passato anch'esso come parametro. 
		 * @param field Variabile passata (nome dato in automatico dal compilatore) su cui eseguire la verifica
		 * @param fValue Valore con il quale effettuare la verifica
		 * @return true se la verifica si conclude con un successo
		 */
		private boolean containmentValue(String field, Object fValue) {
			
			List<String> rightFields = Utils.getRightFields();
			
			if( !rightFields.contains(field) ) return false;
			
			if( !(fValue instanceof String[]) ) return false;
			
			List<String> s = Arrays.asList((String[])fValue);
			
			
			switch(field)
			{
				case "indic_bt": return (s.contains( this.getIndic() ));
				
				case "nace_r2": return (s.contains( String.valueOf( this.getNace() ) ));
				
				case "s_adj": return (s.contains( this.getAdj() ));
				
				case "unit": return (s.contains( this.getUnit() ));
				
				case "country": return (s.contains( this.getCountry() ));
			}
			
			return false;
		}

		/**
		 * Metodo che verifica se la variabile passata come argomento ha un  valore minore del valore passato
		 * come argomento.
		 * @param field Variabile passata (nome dato in automatico dal compilatore) su cui eseguire la verifica
		 * @param fValue Valore con il quale effettuare la verifica
		 * @return true se la verifica si conclude con un successo
		 */
		private boolean lowerValue(String field, Object fValue) {
			
			List<String> rightFields = Utils.getRightFields();
			
			if( !rightFields.contains(field) ) return false;
			
			Map<Integer, Double> indexes = this.getIndexes();
			
			for(Integer year : indexes.keySet())
			{
				if( year == Integer.parseInt((String)field) )
				{
					if( fValue instanceof String )
						return indexes.get(year) < Double.parseDouble((String)fValue);
						
					if( fValue instanceof Long)
						return indexes.get(year) < (Long)fValue;
						
					if( fValue instanceof Double )
						return indexes.get(year) < (Double)fValue;
					
					return false;
				}
			}
		
			return false;
		}


		/**
		 * Metodo che verifica se la variabile passata come argomento ha un  valore maggiore del valore passato
		 * come argomento.
		 * @param field Variabile passata (nome dato in automatico dal compilatore) su cui eseguire la verifica
		 * @param fValue Valore con il quale effettuare la verifica
		 * @return true se la verifica si conclude con un successo
		 */
		private boolean greaterValue(String field, Object fValue) {
			
			List<String> rightFields = Utils.getRightFields();
			
			if( !rightFields.contains(field) ) return false;
			
			Map<Integer, Double> indexes = this.getIndexes();
			
			for(Integer year : indexes.keySet())
			{
				if( year == Integer.parseInt((String)field) )
				{
					if(fValue instanceof String )
						return indexes.get(year) > Double.parseDouble((String)field);
						
					if( fValue instanceof Long)
						return indexes.get(year) > (Long)fValue;
						
					if(fValue instanceof Double )
						return indexes.get(year) > (Double)fValue;
					
					return false;
				}
			}
		
			return false;
		}

		/**
		 * Metodo che verifica se la variabile passata come argomento ha un  valore compreso tra i valori passati 
		 * come parametri. 
		 * @param field Variabile passata (nome dato in automatico dal compilatore) su cui eseguire la verifica
		 * @param min Valore minore
		 * @param max Valore maggiore
		 * @return true se la verifica si conclude con un successo
		 */
		private boolean checkValue(String field, Object min, Object max) {
			
			return ( this.lowerValue(field, max) && this.greaterValue(field, min) )	|| this.equalValue(field, max) || this.equalValue(field, min);
		}

		/**
		 * Metodo che verifica se la variabile passata come argomento ha un  valore uguale al valore passato
		 * come argomento.
		 * @param field Variabile passata (nome dato in automatico dal compilatore) su cui eseguire la verifica
		 * @param fValue Valore con il quale effettuare la verifica
		 * @return true se la verifica si conclude con un successo
		 */
		private boolean equalValue(String field, Object fValue) {
			
			List<String> rightFields = Utils.getRightFields();
			
			if( !rightFields.contains(field) ) return false;
			
			switch(field)
			{
				case "indic_bt": return ( fValue instanceof String && ((String)fValue).equals(this.getIndic()) );
				
				case "nace_r2": return ( fValue instanceof String && ((String)fValue).equals( String.valueOf(this.getNace()) ) );
				
				case "s_adj": return ( fValue instanceof String && ((String)fValue).equals( this.getAdj() ) );
				
				case "unit": return ( fValue instanceof String && ((String)fValue).equals( this.getUnit() ) );
				
				case "country": return ( fValue instanceof String && ((String)fValue).equals( this.getCountry() ) );
				
			}
			
			Map<Integer, Double> indexes = this.getIndexes();
			
			
			
			for(Integer year : indexes.keySet())
			{
				if( year == Integer.parseInt((String)field) )
				{
					if( fValue instanceof String )
						return indexes.get(year) == Double.parseDouble((String)fValue);
						
					if( fValue instanceof Long)
						return indexes.get(year) == Double.longBitsToDouble((Long)fValue);
						
					if( fValue instanceof Double )
						return indexes.get(year) == (Double)fValue;
					
					return false;
				}
			}
		
			return false;
		}
}
