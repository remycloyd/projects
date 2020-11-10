package br.com.appletpong.core;
public class BaseGameEntity {
	protected boolean alive;
	protected double x, y;
	protected double velX, velY;
	protected double moveAngle, faceAngle;

	public boolean isAlive() {
		return alive;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public double getMoveAngle() {
		return moveAngle;
	}

	public double getFaceAngle() {
		return faceAngle;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public void setFaceAngle(double angle) {
		this.faceAngle = angle;
	}

	public void setMoveAngle(double angle) {
		this.moveAngle = angle;
	}

	public BaseGameEntity() {
		setAlive(false);
		setX(0.0);
		setY(0.0);
		setVelX(0.0);
		setVelY(0.0);
		setMoveAngle(0.0);
		setFaceAngle(0.0);
	}
}
