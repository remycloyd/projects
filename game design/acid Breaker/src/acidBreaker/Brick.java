package acidBreaker;import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class Brick {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final int wid =  (int) screenSize.getWidth();
	//private int hei = (int) screenSize.getHeight();
	public final static int width = 165;
	final int Y = 5;
	private Ball ball;
	private int x;
	private ClipLoader cl;

	public Brick(Ball b, int x) {
		ball = b;
		this.x = x;
	}

	public void draw(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(x, Y, width, 20);
	}

	public boolean brickballCollider(ClipLoader cl2) {
		int ballX = ball.getX() + 10;
		if (ball.getY() <= 15 && ball.getY() >= 5) {
			if (ballX >= x && ballX <= x + width) {
				cl2.play("hitbrick", true);
				ball.invertvertSpeed();
				return true;
			}
		}
		return false;
	}
}