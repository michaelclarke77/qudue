package dataModels;

/**
 * Data model for Course object
 * @author michaelclarke
 *
 */
public class Course {
	
	private String courseId;
	
	private String courseTitle;
	
	public Course(){
		
	}
	
	

	public Course(String courseId, String courseTitle) {
		this.courseId = courseId;
		this.courseTitle = courseTitle;
	}



	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	
	

}
