package dataModels;

public class Note {
	
	private int id;
	
	private String title;
	
	private String content;
	
	private String date;
	
	private String type;
	
	private String file;
	
	public Note(){
		
	}

	public Note(int id, String title, String content, String date, String type, String file) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.setType(type);
		this.setFile(file);
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		
		this.file = file;
		
	}
	
	

}
