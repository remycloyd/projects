package br.com.appletpong.core;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import br.com.appletpong.enums.State;
import br.com.appletpong.utils.GameUtils;

/**
 * Classe abstrata para que da suporte para o jogo
 * 
 * @author paulo
 * 
 */
public abstract class AbstractPong extends Applet implements Runnable,
		KeyListener {
	/** */
	private static final long serialVersionUID = 1L;

	// Imagens
	protected static final String BACKGROUND_IMAGE = "background.jpg";
	protected static final String BALL_IMAGE = "xball.png";
	protected static final String PADDLE_IMAGE = "xpaddle.png";

	// Sons
	protected static final String COLLISION_SOUND = "pingpong.wav";
	protected static final String GAME_OVER_SOUND = "fimgame.wav";

	// Pontuação
	protected static final int HIGHSCORE_ACCUMULATED = 10;
	protected static final long HIGHSCORE_INITIAL = 0;

	// Tamanho do applet
	protected static int SCREENWIDTH = 800;
	protected static int SCREENHEIGHT = 500;

	protected Thread gameLoop;

	// estados do jogo running, menu e game over
	protected State gameState;

	protected Random rand = new Random();
	protected BufferedImage backbuffer;
	protected Graphics2D g2d;
	protected Image background;

	// Sprites do jogo
	protected AnimatedSprite ball;
	protected AnimatedSprite paddle;

	protected Long score = 0L;

	/**
	 * Recupera audio
	 * 
	 * @param fileName
	 * @return {@link AudioClip}
	 */
	private AudioClip getAudioClip(String fileName) {
		return getAudioClip(GameUtils.getSoundURL(), fileName);
	}

	/**
	 * Toca som de colisão
	 */
	protected void playSoundCollision() {
		AudioClip audioClip = getAudioClip(COLLISION_SOUND);
		audioClip.play();
	}

	/**
	 * Toca som de game over
	 */
	protected void playSoundGameOver() {
		AudioClip audioClip = getAudioClip(GAME_OVER_SOUND);
		audioClip.play();
	}

	/**
	 * Muda direção da bola quando colide com a raquete
	 */
	protected void changeBallDirectionCollidesWithPaddle() {
		// muda direção do angulo
		this.ball.setFaceAngle(360 - this.ball.rotRate);
		double x = this.ball.velocity().X() * -1;// muda direção
		this.ball.setVelocity(new Point2D(x, this.ball.velocity().Y()));
		x = ball.position().X() + 10.0; // afasta um pouco da raquete
		this.ball.setPosition(new Point2D(x, this.ball.position().Y()));
	}

	protected void changeBallDirectionX() {
		double x = this.ball.velocity().X() * -1;
		this.ball.setVelocity(new Point2D(x, this.ball.velocity().Y()));
		x = this.ball.position().X();
		this.ball.setPosition(new Point2D(x, this.ball.position().Y()));
	}

	protected void changeBallDirectionY() {
		double y = this.ball.velocity().Y() * -1;
		this.ball.setVelocity(new Point2D(this.ball.velocity().X(), y));
		y = this.ball.position().Y() + this.ball.velocity().Y();
		this.ball.setPosition(new Point2D(this.ball.position().X(), y));

	}

	/**
	 * Verifica se é hora de rodar o jogo
	 * 
	 * @return true caso a thread atual seja a do jogo
	 */
	protected boolean isThreadGame() {
		Thread t = Thread.currentThread();
		return t == getGameLoop();
	}

	/**
	 * Exibi apções do jogo
	 */
	protected void drawStateGameOver() {
		this.g2d.setFont(new Font("Verdana", Font.BOLD, 36));
		this.g2d.setColor(new Color(200, 30, 30));
		this.g2d.drawString("GAME OVER", 270, 200);
		this.g2d.setFont(new Font("Arial", Font.CENTER_BASELINE, 20));
		this.g2d.setColor(Color.WHITE);
		this.g2d.drawString("Press ENTER to restart", 270, 250);
	}

	protected void drawStateRunning() {
		this.g2d.setFont(new Font("Verdana", Font.BOLD, 25));
		this.g2d.setColor(Color.WHITE);
		// this.g2d.drawString("Position: " + this.ball.position().X() + ","
		// + this.ball.position().Y(), 5, 10);
		// this.g2d.drawString("Velocity: " + this.ball.velocity().X() + ","
		// + this.ball.velocity().Y(), 5, 25);
		// this.g2d.drawString("Animation: " + this.ball.currentFrame(), 5, 40);
		this.g2d.drawString("SCORE: " + getScore(), 300, 50);
	}

	protected void drawStateMenu() {
		this.g2d.setColor(Color.WHITE);
		this.g2d.drawString("PONG GAME", 252, 202);
		this.g2d.setColor(new Color(200, 30, 30));

		int x = 270, y = 15;
		this.g2d.setColor(Color.WHITE);
		this.g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		this.g2d.drawString("CONTROLS:", x, ++y * 20);
		this.g2d.drawString("PADDLE - KeyBoard UP/DOWN", x + 20, ++y * 20);
		this.g2d.drawString("ENTER - Start", x + 20, ++y * 20);

		this.g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		this.g2d.drawString("Press ENTER to start", 280, 570);

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public State getGameState() {
		return gameState;
	}

	public void setGameState(State gameState) {
		this.gameState = gameState;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Thread getGameLoop() {
		return this.gameLoop;
	}

	public void setGameLoop(Thread gameLoop) {
		this.gameLoop = gameLoop;
	}

}
