package br.com.appletpong.core;

import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import br.com.appletpong.utils.GameUtils;

/**
 * Representa uma animação do jogo encapsula metodos de animação
 */
public class AnimatedSprite extends Sprite {
	// this image holds the large tiled bitmap
	private Image animimage;
	BufferedImage tempImage;
	Graphics2D tempSurface;
	private int currFrame, totFrames;
	private int animDir;
	private int frCount, frDelay;
	private int frWidth, frHeight;
	private int cols;

	public AnimatedSprite(Applet applet, Graphics2D g2d) {
		super(applet, g2d);
		this.currFrame = 0;
		this.totFrames = 0;
		this.animDir = 1;
		this.frCount = 0;
		this.frDelay = 0;
		this.frWidth = 0;
		this.frHeight = 0;
		this.cols = 0;
	}

	/**
	 * Carrega imagem
	 * 
	 * @param filename
	 * @param columns
	 * @param rows
	 * @param width
	 * @param height
	 */

	public void load(String filename, int columns, int rows, int width,
			int height) {
		this.animimage = GameUtils.getImage(filename);
		setColumns(columns);
		setTotalFrames(columns * rows);
		setFrameWidth(width);
		setFrameHeight(height);

		this.tempImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		this.tempSurface = this.tempImage.createGraphics();
		super.setImage(this.tempImage);
	}

	public int currentFrame() {
		return currFrame;
	}

	public void setCurrentFrame(int frame) {
		currFrame = frame;
	}

	public int frameWidth() {
		return frWidth;
	}

	public void setFrameWidth(int width) {
		frWidth = width;
	}

	public int frameHeight() {
		return frHeight;
	}

	public void setFrameHeight(int height) {
		frHeight = height;
	}

	public int totalFrames() {
		return totFrames;
	}

	public void setTotalFrames(int total) {
		totFrames = total;
	}

	public int animationDirection() {
		return animDir;
	}

	public void setAnimationDirection(int dir) {
		animDir = dir;
	}

	public int frameCount() {
		return frCount;
	}

	public void setFrameCount(int count) {
		frCount = count;
	}

	public int frameDelay() {
		return frDelay;
	}

	public void setFrameDelay(int delay) {
		frDelay = delay;
	}

	public int columns() {
		return cols;
	}

	public void setColumns(int num) {
		cols = num;
	}

	/**
	 * Atualiza animação
	 */
	public void updateAnimation() {
		this.frCount++;
		// verifica se esta na hora de atualizar animação
		if (frameCount() > frameDelay()) {
			setFrameCount(0);
			setCurrentFrame(currentFrame() + animationDirection());
			if (currentFrame() > totalFrames() - 1) {
				// caso ultrapasse o limite de frames volta a zero
				setCurrentFrame(0);
			} else if (currentFrame() < 0) {
				// vai ao extremo
				setCurrentFrame(totalFrames() - 1);
			}
		}
	}

	public void draw() {
		// calculo para pegar frame da imagem correto
		int frameX = (currentFrame() % columns()) * frameWidth();
		int frameY = (currentFrame() / columns()) * frameHeight();

		this.tempSurface.drawImage(this.animimage, 0, 0, frameWidth() - 1,
				frameHeight() - 1, frameX, frameY, frameX + frameWidth(),
				frameY + frameHeight(), applet());

		super.setImage(this.tempImage);
		super.transform();
		super.draw();
	}

}
