package userInterface;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class ProgressBarGraphic {
	
	public static ProgressBar progressBar;
	
	float value;
	

	public HBox display() {
		
		
		try {
			
			value = ((float)EssayView.wordCount)/((float)EssayView.wordLimit);
			
		} catch (ArithmeticException arithmeticException){
			arithmeticException.printStackTrace();
		}
		

		progressBar = new ProgressBar();
		progressBar.setProgress(value);
		progressBar.setMinWidth(220);
		progressBar.setMinHeight(25);
		

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(progressBar);

		return hBox;
	}

}
