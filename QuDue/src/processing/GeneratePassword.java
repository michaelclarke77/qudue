package processing;

import java.util.Random;

public class GeneratePassword {
	
	public static String generate(){
		String password = "";
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int length = 8;
		Random random = new Random();
		
	    char[] text = new char[length];
	    for (int count = 0; count < length; count++)
	    {
	        text[count] = characters.charAt(random.nextInt(characters.length()));
	        password += text[count];
	    }
	    
	    System.out.println(password);
	    
	    return password;
	}

}
