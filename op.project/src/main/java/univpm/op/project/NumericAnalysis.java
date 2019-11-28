package univpm.op.project;

import java.util.ArrayList;
import java.util.List;

public class NumericAnalysis {

	private double sum = 0;
	private int count = 0;
	private double average;
	private double stddev = 0;
	private double min;
	private double max;
	
	private List<Double> data;
	
	public NumericAnalysis() {
		
		this.data = new ArrayList<Double>();
		this.sum = 0;
		this.count = 0;
		this.average = 0;
		this.stddev = 0;
	}
	
	public void addValue(double value) {
		
		this.data.add(value);
		this.sum += value;
		this.count++;

		if( this.count == 1 )
		{
			this.min = value;
			this.max = value;
		}else{
			this.min = Math.min(value, this.min);
			this.max = Math.max(value, this.max);
		}
		
		calculateAvg();
	}

	private void calculateAvg() {
	
		this.average = this.sum / this.count;
		
	}

	public void calcDev() {
		
		double stddev = 0;
		for( Double value : this.data )
		{
			stddev += Math.pow( (this.average - value) , 2);
		}
		this.stddev = stddev;
		
	}

	public Object getSum() {
		
		return sum;
	
	}

	public Object getCount() {
		
		return count;
		
	}

	public Object getAverage() {
		
		return average;
		
	}

	public Object getMin() {
		
		return min;
		
	}

	public Object getMax() {
		
		return max;
		
	}

	public Object getStdDev() {
		
		return stddev;
		
	}

}
