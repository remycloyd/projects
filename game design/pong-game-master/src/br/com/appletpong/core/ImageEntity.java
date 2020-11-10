package br.com.appletpong.core;

import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class ImageEntity extends BaseGameEntity {
	protected Image image;
	protected Applet applet;
	protected AffineTransform at;
	protected Graphics2D g2d;

	public ImageEntity(Applet a) {
		this.applet = a;
		setImage(null);
		setAlive(true);
	}

	public Image getImage() {
		return image;
	}

	/**
	 * Defini propriedades da imagem
	 * 
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
		double x = this.applet.getSize().width / 2 - width() / 2;
		double y = this.applet.getSize().height / 2 - height() / 2;
		this.at = AffineTransform.getTranslateInstance(x, y);
	}

	public int width() {
		if (this.image != null)
			return this.image.getWidth(this.applet);
		else
			return 0;
	}

	public int height() {
		if (this.image != null)
			return this.image.getHeight(this.applet);
		else
			return 0;
	}

	public double getCenterX() {
		return getX() + width() / 2;
	}

	public double getCenterY() {
		return getY() + height() / 2;
	}

	public void setGraphics(Graphics2D g) {
		this.g2d = g;
	}

	// Coloca efeito na entidade
	public void transform() {
		this.at.setToIdentity();
		this.at.translate((int) getX() + width() / 2, (int) getY() + height()
				/ 2);
		this.at.rotate(Math.toRadians(getFaceAngle()));
		this.at.translate(-width() / 2, -height() / 2);
	}

	public void draw() {
		this.g2d.drawImage(getImage(), this.at, this.applet);
	}

	public Rectangle getBounds() {
		Rectangle r;
		r = new Rectangle((int) getX(), (int) getY(), width(), height());
		return r;
	}

}
