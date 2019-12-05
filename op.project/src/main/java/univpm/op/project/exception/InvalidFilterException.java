package univpm.op.project.exception;

public class InvalidFilterException extends Exception
{
	private String errorMessage;
	
	public InvalidFilterException() {
		this.errorMessage = "Filtro non valido.";
	}
	
	public InvalidFilterException( String errorMessage ) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.errorMessage;
	}

}


// throw new InvalidFilterException("Chiave non alfanumerica.");