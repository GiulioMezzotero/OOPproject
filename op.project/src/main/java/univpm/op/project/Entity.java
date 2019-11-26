package univpm.op.project;

import java.util.ArrayList;
import java.util.List;

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


}
