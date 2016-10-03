package dataModels;

public class Module {

	private String id;

	private String title;

	private int lecturerId;

	public Module() {

	}

	public Module(String id, String title, int lecturerId) {
		this.id = id;
		this.title = title;
		this.lecturerId = lecturerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(int lecturerId) {
		this.lecturerId = lecturerId;
	}

}
