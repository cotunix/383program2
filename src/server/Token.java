package server;
import java.util.Random;
public class Token {

	public String password;
	public String userToken;
	public Random rd;
	
	
	public Token(String password) {
		
		this.password = password;
		Integer userNum = rd.nextInt(Integer.MAX_VALUE);
		
		userToken = userNum.toString();
		
	}
}
