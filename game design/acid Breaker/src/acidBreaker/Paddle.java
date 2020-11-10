package acidBreaker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class Paddle {

	private int x;
	final int YPOS = 850, WIDTH = 200, HEIGHT = 30, SPEED = 15;
	private Ball ball;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int wid =  (int) screenSize.getWidth();
	private ClipLoader cl;   
	
	
	
	public Paddle(Ball b) {
		//initial location for paddle midway in screen x direction
		x = (wid) / 2; 		
		ball = b;
	}

	public void move(int hztlMouse) {
		hztlMouse = hztlMouse - 50;//center offset regulator
		//int diff = Math.abs(x - hztlMouse);
		if (hztlMouse > this.x) { // moving right
			//if (diff < 5)
				x = hztlMouse;
//			else
//				x += SPEED;
		} else if (hztlMouse < this.x) { // moving left
			//if (diff < 5)
				x = hztlMouse;
//			else
//				x -= SPEED;
		}
		checkOutOfBounds();
	}

	/**
	 * Called in each iteration of the run() method. finds if the ball has
	 * collided with the paddle. If a collision has occured, then ball's Speed is altered.
	 */
	public void paddleballCollider(ClipLoader cl2) 
	{
		int ballX = ball.getX() + 10;
		if (ball.getY() >= 830 && ball.getY() <= 854) 
		{
			if (ballX > this.x && ballX < this.x + 200) 
			{
				cl2.play("hitPaddle", true);
				ball.invertvertSpeed();
				alterhztlSpeed();
			}
		}
	}

	
	//changes the x axis Speed of the ball according to where the ball hits paddle. 
	public void alterhztlSpeed() 
	{
		int ballX = ball.getX() + 15;//ball location
		
		if (ballX >= x && ballX < x + 20)
			ball.sethztlSpeed(-7);
		
		if (ballX >= x + 20 && ballX < x + 40)
			ball.sethztlSpeed(-6);
		
		if (ballX >= x + 40 && ballX < x + 60)
			ball.sethztlSpeed(-5);
		
		if (ballX >= x + 60 && ballX < x + 80)
			ball.sethztlSpeed(-4);
		
		if (ballX >= x + 80 && ballX < x + 100)
			ball.sethztlSpeed(-2);
		
		if (ballX >= x + 100 && ballX < x + 120)
			ball.sethztlSpeed(2);
		
		if (ballX >= x + 120 && ballX < x + 140)
			ball.sethztlSpeed(4);
		
		if (ballX >= x + 140 && ballX < x + 160)
			ball.sethztlSpeed(5);
		
		if (ballX >= x + 160 && ballX < x + 180)
			ball.sethztlSpeed(6);
		
		if (ballX >= x + 180 && ballX < x + 200)
			ball.sethztlSpeed(7);

		System.out.println("Collision detected at " + ballX + "");
		
		System.out.println("Ball hztlSpeed updated to = " + ball.gethztlSpeed());
	}

	private void checkOutOfBounds() {
		if (x < 0)
			x = 0;
		if (x > wid-50)
			x = wid-50;
	}

	public void draw(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(x, YPOS, WIDTH, HEIGHT);
	}
}