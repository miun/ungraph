package miun.android;

public class FileNotSupportedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5031808161149224433L;

	public FileNotSupportedException(String reason){
		super(reason);
	}
}
