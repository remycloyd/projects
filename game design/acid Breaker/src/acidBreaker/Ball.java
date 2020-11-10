package acidBreaker;import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class Ball {

	private boolean isMoving;
	private int x, y, hztlSpeed, vertSpeed;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int wid =  (int) screenSize.getWidth();
	//private int hei = (int) screenSize.getHeight();
	

	public Ball() {
		x = (wid-40) / 2;
		y = (400 - 80) / 2;
		hztlSpeed = (int) (Math.random() * 7 - 3);
		vertSpeed = 15;
		isMoving = false;
	}

	public void startMoving() {
		isMoving = true;
	}

	public void move() {
		if (isMoving) {
			x += hztlSpeed;
			y += vertSpeed;
			ballWallCollider();
		}
	}

	
	private void ballWallCollider() 
	{
		
		
		if (x < 2) 
		{
			x = 2;
			hztlSpeed = -hztlSpeed;
		}
		if (x > wid-5) 
		{
			x = wid -6;
			hztlSpeed = -hztlSpeed;
		}
		if (y < 0) 
		{
			y = 0;
			vertSpeed = -vertSpeed;
		}
	}

	public void sethztlSpeed(int x) {
		hztlSpeed = x;
	}

	public void setvertSpeed(int y) {
		vertSpeed = y;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int gethztlSpeed() {
		return hztlSpeed;
	}

	public int getvertSpeed() {
		return vertSpeed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void inverthztlSpeed() {
		hztlSpeed = - hztlSpeed;
	}

	public void invertvertSpeed() {
		vertSpeed = - vertSpeed;
	}

	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(x, y, 20, 20);
	}
}