package br.com.appletpong.core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite extends Object {
	private ImageEntity entity;
	protected Point2D pos;
	protected Point2D vel;
	protected double rotRate;
	protected int currentState;

	public Sprite(Applet a, Graphics2D g2d) {
		this.entity = new ImageEntity(a);
		this.entity.setGraphics(g2d);
		this.entity.setAlive(false);
		this.pos = new Point2D(0, 0);
		this.vel = new Point2D(0, 0);
		this.rotRate = 0.0;
		this.currentState = 0;
	}

	public void transform() {
		this.entity.setX(this.pos.X());
		this.entity.setY(this.pos.Y());
		this.entity.transform();
	}

	public void draw() {
		this.entity.g2d.drawImage(this.entity.getImage(), this.entity.at,
				this.entity.applet);
	}

	public void drawBounds(Color c) {
		this.entity.g2d.setColor(c);
		this.entity.g2d.draw(getBounds());
	}

	public void updatePosition() {
		this.pos.setX(this.pos.X() + this.vel.X());
		this.pos.setY(this.pos.Y() + this.vel.Y());
	}

	public void updatePositionDown() {
		this.pos.setY(this.pos.Y() + this.vel.Y());
	}

	public void updatePositionUp() {
		this.pos.setY(this.pos.Y() - this.vel.Y());
	}

	public double rotationRate() {
		return rotRate;
	}

	public void setRotationRate(double rate) {
		rotRate = rate;
	}

	public void updateRotation() {
		setFaceAngle(faceAngle() + this.rotRate);
		if (faceAngle() < 0)
			setFaceAngle(360 - this.rotRate);
		else if (faceAngle() > 360)
			setFaceAngle(this.rotRate);
	}

	public int state() {
		return currentState;
	}

	public void setState(int state) {
		currentState = state;
	}

	public Rectangle getBounds() {
		return entity.getBounds();
	}

	public Point2D position() {
		return pos;
	}

	public void setPosition(Point2D pos) {
		this.pos = pos;
	}

	public Point2D velocity() {
		return vel;
	}

	public void setVelocity(Point2D vel) {
		this.vel = vel;
	}

	public Point2D center() {
		return (new Point2D(this.entity.getCenterX(), this.entity.getCenterY()));
	}

	public boolean alive() {
		return entity.isAlive();
	}

	public void setAlive(boolean alive) {
		entity.setAlive(alive);
	}

	public double faceAngle() {
		return entity.getFaceAngle();
	}

	public void setFaceAngle(double angle) {
		entity.setFaceAngle(angle);
	}

	public void setFaceAngle(float angle) {
		entity.setFaceAngle((double) angle);
	}

	public void setFaceAngle(int angle) {
		entity.setFaceAngle((double) angle);
	}

	public double moveAngle() {
		return entity.getMoveAngle();
	}

	public void setMoveAngle(double angle) {
		entity.setMoveAngle(angle);
	}

	public void setMoveAngle(float angle) {
		entity.setMoveAngle((double) angle);
	}

	public void setMoveAngle(int angle) {
		entity.setMoveAngle((double) angle);
	}

	public int imageWidth() {
		return entity.width();
	}

	public int imageHeight() {
		return entity.height();
	}

	public boolean collidesWith(Rectangle rect) {
		return (rect.intersects(getBounds()));
	}

	public boolean collidesWith(Sprite sprite) {
		return (getBounds().intersects(sprite.getBounds()));
	}

	public boolean collidesWith(Point2D point) {
		return (getBounds().contains(point.X(), point.Y()));
	}

	public Applet applet() {
		return entity.applet;
	}

	public Graphics2D graphics() {
		return entity.g2d;
	}

	public Image image() {
		return entity.image;
	}

	public void setImage(Image image) {
		entity.setImage(image);
	}

}
