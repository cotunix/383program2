package server;
import java.util.Random;
public class Token {

	public String password;
	public String userToken;
	public Random rd;
	
	
	public Token(String password) {
		
		this.password = password;
		Integer userNum = rd.nextInt(65530) * rd.nextInt(65530);
		
		userToken = userNum.toString();
		
	}
}
