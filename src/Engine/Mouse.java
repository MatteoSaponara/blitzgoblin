package Engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse {

	private static boolean leftButtonDown = false;

	private static final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				leftButtonDown = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				leftButtonDown = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	};

	private Mouse() { }

	public static MouseListener getMouseListener() {
		return mouseListener;
	}

	public static boolean isLeftButtonDown() {
		return leftButtonDown;
	}

	public static boolean isLeftButtonUp() {
		return !leftButtonDown;
	}
}