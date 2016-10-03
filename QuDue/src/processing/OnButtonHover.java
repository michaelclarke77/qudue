package processing;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Class which defines the style of a button when it is hovered over
 * 
 * @author michaelclarke
 *
 */
public class OnButtonHover {

	/**
	 * Sets the mouseOnEntered value to the buttonHover value and mouseOnExited value to
	 * the buttonNormal value
	 * 
	 * @param button
	 * @param buttonNormal
	 * @param buttonHover
	 */
	public static void action(Button button, String buttonNormal, String buttonHover) {

		button.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				button.setId(buttonHover);
			}
		});

		button.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				button.setId(buttonNormal);

			}
		});
	}

}
