package Engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse
{

	private static boolean leftButtonDown = false;
	private static volatile int mouseX = 0;
	private static volatile int mouseY = 0;

	private static final MouseListener mouseListener = new MouseListener()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				leftButtonDown = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				leftButtonDown = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}
	};

	private static final MouseMotionListener mouseMotionListener = new MouseMotionListener()
	{
		@Override
		public void mouseDragged(MouseEvent e)
		{
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			mouseX = e.getX();
		}
	};

	private Mouse()
	{
	}

	public static MouseListener getMouseListener()
	{
		return mouseListener;
	}

	public static MouseMotionListener getMouseMotionListener()
	{
		return mouseMotionListener;
	}

	public static boolean isLeftButtonDown()
	{
		return leftButtonDown;
	}

	public static boolean isLeftButtonUp()
	{
		return !leftButtonDown;
	}

	public static int getMouseX()
		{
		return mouseX;
		}

	public static int getMouseY()
	{
		return mouseY;
	}

	public static double getAngleTo(double originX, double originY)
	{
		return Math.atan2(originY - mouseY, originX - mouseX);
	}
}