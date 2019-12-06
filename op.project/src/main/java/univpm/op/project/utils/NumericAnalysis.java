package univpm.op.project.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che estrae l'analisi dai dati
 * @author Giulio Mezzotero e Giovanni Alessandro Clini 
 *
 */
public class NumericAnalysis {

	private double sum = 0;
	private int count = 0;
	private double average;
	private double stddev = 0;
	private double min;
	private double max;
	
	private List<Double> data;
	
	/**
	 * Costruttore della classe
	 */
	public NumericAnalysis() {
		
		this.data = new ArrayList<Double>();
		this.sum = 0;
		this.count = 0;
		this.average = 0;
		this.stddev = 0;
	}
	
	/**
	 * Metodo che inserisce un nuovo valore in lista 
	 * @param value Valore da inserire
	 */
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

	/**
	 * Metodo per calcolare la media dei valori inseriti
	 */
	private void calculateAvg() {
	
		this.average = this.sum / this.count;
		
	}

	/**
	 * Metodo che calcola la deviazione standard dei valori inseriti
	 */
	public void calcDev() {
		
		double stddev = 0;
		for( Double value : this.data )
		{
			stddev += Math.pow( (this.average - value) , 2);
		}
		this.stddev = stddev;
		
	}
	
	/**
	 * Metodo che restituisce la somma dei valori inseriti
	 * @return Somma dei valori inseriti
	 */
		public Object getSum() {
			
			return sum;
		
		}
		
	/**
	 * Metodo che restituisce il conteggio dei valori inseriti
	 * @return Conteggio dei valori inseriti
	 */
		public Object getCount() {
			
			return count;
		
	}

	/**
	 *  Metodo che restituisce la media dei valori inseriti.
	 * @return Media dei valori inseriti
	 */
	public Object getAverage() {
		
		return average;
		
	}

	/**
	 * Metodo che restituisce il minimo dei valori inseriti.
	 * @return Minimo dei valori inseriti
	 */
	public Object getMin() {
		
		return min;
		
	}

	/**
	 * Metodo che restituisce il massimo dei valori inseriti.
	 * @return Massimo dei valori inseriti
	 */
	public Object getMax() {
		
		return max;
		
	}

	/**
	 * Metodo che restituisce la deviazione standard dei valori inseriti
	 * @return Deviazione standard dei valori inseriti
	 */
	public Object getStdDev() {
		
		return stddev;
		
	}

}
