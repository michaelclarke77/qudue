package userInterface;



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import dataModels.Note;
import databaseConnection.CloudStorage;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PhotoNoteView {
	
		
		public static Node getView(Note note){
			
			VBox vBox = new VBox(20);
			
			// Create label for title & date
					Label label = new Label();
					label.setText(note.getTitle() + " (" + note.getDate() + ")");

					ImageView imageView = new ImageView();
					
					File file = CloudStorage.getObject(note.getFile());
					
					BufferedImage bufferedImage = null;
					try {
						FileInputStream fis = new FileInputStream(file); 
						bufferedImage = ImageIO.read(fis);
					} catch (IOException e) {
					} 
					
					if (bufferedImage != null){
						Image image = SwingFXUtils.toFXImage(bufferedImage, null);
						imageView.setImage(image);
					} 
					
					imageView.setFitWidth(300);
					imageView.setPreserveRatio(true);
					imageView.setSmooth(true);
			
					vBox.setAlignment(Pos.CENTER);
					vBox.getChildren().addAll(label, imageView);
					
					return vBox;
			
		}

	}

