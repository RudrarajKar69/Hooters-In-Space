package elements;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Event_Handler {

	public boolean Left, Right, Shot, Up, Down, Enter;
	KeyEvent k;

	public Event_Handler(KeyEvent k, MouseEvent e) {
		if (k != null) {
			this.k = k;
			keyPressed(this.k);
		} else
			keyReleased();
		mousePressed(e);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_D:
			Right = true;
		case KeyEvent.VK_RIGHT:
			Right = true;
			break;
		case KeyEvent.VK_A:
			Left = true;
		case KeyEvent.VK_LEFT:
			Left = true;
			break;
		case KeyEvent.VK_W:
			Up = true;
		case KeyEvent.VK_UP:
			Up = true;
			break;
		case KeyEvent.VK_S:
			Down = true;
		case KeyEvent.VK_DOWN:
			Down = true;
			break;
		case KeyEvent.VK_ENTER:
			Enter = true;
			break;
		}
	}

	public void keyReleased() {
		Right = false;
		Left = false;
		Up = false;
		Down = false;
		Enter = false;
	}

	public void mousePressed(MouseEvent e) {
		if (e != null)
			Shot = true;
		if (e == null)
			Shot = false;
	}

}
