package univpm.op.project;

public class NData {

	public int year;
	public double index;

	NData(int year, double index) {
		this.year = year;
		this.index = index;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getValue() {
		return index;
	}

	public void setValue(double index) {
		this.index = index;
	}


}

