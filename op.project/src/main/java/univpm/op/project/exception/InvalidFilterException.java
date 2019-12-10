package univpm.op.project.exception;

/**
 * Classe che gestisce le eccezioni per filtri 
 * @author Giulio Mezzotero e Giovanni Alessandro Clini
 *
 */
public class InvalidFilterException extends Exception
{
	private String errorMessage;
	
	/**
	 * Costruttore che setta la stringa errorMessage se non specificata
	 */
	public InvalidFilterException() {
		this.errorMessage = "Filtro non valido.";
	}
	
	/**
	 * Costruttore che setta la stringa errorMessage
	 * @param errorMessage Passaggio della stringa del particolare errore 
	 */
	public InvalidFilterException( String errorMessage ) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Override del metodo che restituisce l'errore
	 */
	@Override
	public String getMessage() {
		return this.errorMessage;
	}

}