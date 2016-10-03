package processing;

public class CreateSource {
	
	public static String generate(String fName, String lName, String title, String city, String year, String publisher, String pages){
		
		String source = "";
		
		if (pages.equals("")){
			
			 source = 
					
					lName + ", " +
					fName.charAt(0)+ ". "+
					"(" + year + "). "+
					title + ". "+
					city + ": " +
					publisher + ".\n\n"
					;
		} else {
			
			 source = 
					
					lName + ", " +
					fName.charAt(0)+ ". "+
					"(" + year + "). "+
					title + ". "+
					city + ": " +
					publisher + ", p." +
					pages + ".\n\n"
					;
		}
		
		
		return source;
		
	}

}
