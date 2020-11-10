package br.com.appletpong.core;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import br.com.appletpong.enums.State;
import br.com.appletpong.utils.GameUtils;

public class Pong extends AbstractPong {
	
	
	private static final long serialVersionUID = 1L;

	public void initialize() {
		loadBackground();
		loadBall();
		loadPaddle();
		setFocusable(true);
		addKeyListener(this);
		setGameState(State.GAME_MENU);
	}

	private void loadBackground() {
		this.backbuffer = new BufferedImage(SCREENWIDTH, SCREENHEIGHT,
				BufferedImage.TYPE_INT_RGB);
		this.g2d = this.backbuffer.createGraphics();
		this.background = GameUtils.getImage(BACKGROUND_IMAGE);
	}

	private void loadPaddle() {
		this.paddle = new AnimatedSprite(this, this.g2d);
		this.paddle.load(PADDLE_IMAGE, 1, 1, 24, 100);
		this.paddle.setPosition(new Point2D(20, 240));
		// this.paddle.setFrameDelay(1);
		this.paddle.setVelocity(new Point2D(20, 20));
		this.paddle.setAlive(true);
	}

	private void loadBall() {
		this.ball = new AnimatedSprite(this, this.g2d);
		this.ball.load(BALL_IMAGE, 8, 8, 64, 64);
		this.ball.setPosition(new Point2D(300, 200));
		this.ball.setFrameDelay(1);
		this.ball.setVelocity(new Point2D(1, 1));
		this.ball.setRotationRate(1.0);
	}

	public void start() {
		setGameLoop(new Thread(this));
		getGameLoop().start();
	}

	public void stop() {
		setGameLoop(null);
	}

	public void run() {
		while (isThreadGame()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (getGameState().equals(State.GAME_RUNNING)) {
				gameUpdate();
			}
			repaint();
		}
	}

	private void gameUpdate() {
		this.ball.updateAnimation();
		this.ball.updatePosition();
		checkCollision();
		checkGameOver();
		this.ball.updateRotation();
	}

	private void checkGameOver() {
		if ((this.ball.position().X() < 0)) {
			gameOver();
		}
	}

	private void checkCollision() {
		if (this.ball.position().X() > SCREENWIDTH - 64) {
			changeBallDirectionX();
		}
		if ((this.ball.position().Y() < 0)
				|| (this.ball.position().Y() > SCREENHEIGHT - 64)) {
			changeBallDirectionY();
		}
		if ((this.ball.collidesWith(this.paddle))) {
			playSoundCollision();
			changeBallDirectionCollidesWithPaddle();
			setScore(getScore() + HIGHSCORE_ACCUMULATED);
		}
	}

	private void gameOver() {
		playSoundGameOver();
		this.ball.setPosition(new Point2D(500, 200));
		this.paddle.setPosition(new Point2D(20, 240));
		this.paddle.setAlive(false);
		setScore(HIGHSCORE_INITIAL);
		setGameState(State.GAME_OVER);

	}

	public void update(Graphics g) {
		this.g2d.drawImage(this.background, 0, 0, SCREENWIDTH - 1,
				SCREENHEIGHT - 1, this);
		this.ball.draw();
		this.paddle.draw();
		drawGameState();
		paint(g);

	}

	private void drawGameState() {
		if (getGameState().equals(State.GAME_MENU)) {
			drawStateMenu();
		} else if (getGameState().equals(State.GAME_RUNNING)) {
			drawStateRunning();
		} else if (getGameState().equals(State.GAME_OVER)) {
			drawStateGameOver();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(this.backbuffer, 0, 0, this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			movePaddleDown();
			break;
		case KeyEvent.VK_UP:
			movePaddleUp();
			break;
		case KeyEvent.VK_ENTER:
			restartGame();
			break;
		default:
			break;
		}
	}

	private void restartGame() {
		this.paddle.setAlive(true);
		if (getGameState().equals(State.GAME_MENU)) {
			setGameState(State.GAME_RUNNING);
		} else if (getGameState().equals(State.GAME_OVER)) {
			setGameState(State.GAME_RUNNING);
		}
	}

	private void movePaddleUp() {
		if (this.paddle.position().Y() > 0 && this.paddle.alive()) {
			this.paddle.updatePositionUp();
		}
	}

	private void movePaddleDown() {
		if (this.paddle.position().Y() < SCREENHEIGHT - 100
				&& this.paddle.alive()) {
			this.paddle.updatePositionDown();
		}
	}
}
