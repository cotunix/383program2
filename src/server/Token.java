package server;

public class Token {

	public String password;
	public String userToken;
	public static Integer userNum = 0;
	
	public Token(String password) {
		
		this.password = password;
		userToken = userNum.toString();
		userNum++;
	}
}
