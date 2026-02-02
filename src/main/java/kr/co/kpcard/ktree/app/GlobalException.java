package kr.co.kpcard.ktree.app;


import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private long issueNo;
	private String message; 
	private String customMsg;
	
	public GlobalException(String message, String customMsg)
    {
        super(message);
        
        this.issueNo = 0; // issueNo For System error. 
        this.message = message;
        this.customMsg = customMsg;
    }

	
	public GlobalException(long issueNo, String message, String customMsg)
    {
        super(message);
        
        this.issueNo = issueNo;
        this.message = message;
        this.customMsg = customMsg;
    }
	
	
	
}
