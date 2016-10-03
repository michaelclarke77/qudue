package userInterface;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import dataModels.Note;
import databaseConnection.CloudStorage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

public class AudioNoteView {

	static Media media;
	public static Button play;
	static URI resource;
	static File audioFile;
	static boolean playing;
	static byte[] data;

	public static Node getView(Note note) {

		audioFile = CloudStorage.getObject(note.getFile());
		
		if (audioFile != null){
			
		Path path = Paths.get(audioFile.getAbsolutePath());
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		VBox vBox = new VBox(30);

		// Create label for title & date
		Label label = new Label();
		label.setText(note.getTitle() + " (" + note.getDate() + ")");

		ImageView imageView = new ImageView();

		URL url = HomeScreen.class.getClassLoader().getResource("play.png");
		Image image = new Image(url.toString());
		
		imageView.setImage(image);

		imageView.setFitWidth(200);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		play = new Button();

		play.setGraphic(imageView);
		play.setOnAction(e -> playButtonEventHandler());

		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(label, play);

		return vBox;
		
		}
		
		return null;

	}

	private static void playButtonEventHandler() {

		try {
			playAudioFromByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play from a byte array.
	 * 
	 * @throws Exception
	 */
	public static void playAudioFromByteArray() throws Exception {
		AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
		InputStream input = new ByteArrayInputStream(data);
		final AudioInputStream ais = new AudioInputStream(input, format, data.length / format.getFrameSize());
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		Runnable runner = new Runnable() {
			int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
			byte buffer[] = new byte[bufferSize];

			public void run() {
				try {
					int count;
					playing = true;
					while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
						if (count > 0) {
							line.write(buffer, 0, count);
						}
						if (!playing)
							break;
					}
					playing = false;
					line.drain();
					line.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Thread playThread = new Thread(runner);
		playThread.start();
	}

}
