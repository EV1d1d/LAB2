/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 *
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.MediaTools;
import acm.util.RandomGenerator;
import acm.util.SoundClip;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;


public class Breakout extends GraphicsProgram {


	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 800;

	/** Dimensions of game board (usually the same) */
	private static int WIDTH = APPLICATION_WIDTH;
	private static int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 150;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	private static final double HEART_SIZE = 40;

	private static final Color BACKGROUND = Color.CYAN;
	private final RandomGenerator random = RandomGenerator.getInstance();

	//ui
	private GImage paddle;
	private GImage ball;
	private GImage brick;
	private GImage Menu;
	private GImage gameBackground;
	private GImage levelOneIcon;
	private GImage levelTwoIcon;
	private GImage levelThreeIcon;
	private GRect selectedLevelBorder;
	private int selectedLevel;
	private boolean menuActive;
	private volatile boolean levelChosen;

	private GImage heartIcon;
	private GLabel livesLabel;
	private GLabel pointsLabel;
	private static final int HUD = HEIGHT - (HEIGHT-(HEIGHT/100));
	//sounds
	private AudioClip ballSound;
	private AudioClip brickSound;
	private AudioClip brickSound2;
	private AudioClip brickSound3;
	private AudioClip musicInMenu;
	private AudioClip musicInGame;
	private AudioClip WallHit1;
	private AudioClip WallHit2;
	private AudioClip WallHit3;
	private AudioClip brickDestroyed;

	//game logic
	private int lives;
	private double vx,vy;
	private int points;
	private double ballSpeed;
	private boolean strongBricks;
	private int heartAnimationFrames;
	private double heartX,heartY;


	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setup();
		musicInMenu = MediaTools.loadAudioClip("Music/menuMusic.wav");
		setSafeVolume(musicInMenu, 0.05);
		musicInGame = MediaTools.loadAudioClip("Music/gameMusic.wav");
		setSafeVolume(musicInGame, 0.15);
		WallHit1 = MediaTools.loadAudioClip("Music/WallHit1.wav");
		setSafeVolume(WallHit1, 0.5);
		WallHit2 = MediaTools.loadAudioClip("Music/WallHit2.wav");
		setSafeVolume(WallHit2, 0.5);
		WallHit3 = MediaTools.loadAudioClip("Music/WallHit3.wav");
		setSafeVolume(WallHit3, 0.5);
		brickDestroyed = MediaTools.loadAudioClip("Music/brickDestroyed.wav");
		setSafeVolume(brickDestroyed, 0.5);
		addMouseListeners();

		while (true) {
			levelChosen = false;
			removeAll();
			menu();

			while (!levelChosen) {
				pause(50);
			}

			if (selectedLevel == 1) {
				lvl1();
			} else if (selectedLevel == 2) {
				lvl2();
			} else if (selectedLevel == 3) {
				lvl3();
			}
		}
	}

	private void lvl3() {
		lives = 1;
		ballSpeed=5;
		strongBricks=true;
		startLevel();
	}

	private void lvl2() {
		lives = 3;
		ballSpeed = 4;
		strongBricks = true;
		startLevel();
	}

	private void lvl1() {
		lives = 5;
		ballSpeed = 3;
		strongBricks = false;
		startLevel();
	}

	private void startLevel(){
		startGameMusic();
		removeAll();
		setGameBackground();
		drawBricks();
		createPaddle();
		createBall();
		createHUD();
		startBallFlight();
	}

    private void createHUD() {

    }

    private void brickHitted() {
		points++;
    }

    private void startGameMusic() {
		menuActive = false;
		musicInMenu.stop();
		musicInGame.stop();
		musicInGame.loop();
	}

	private void menu() {
		menuActive = true;
		musicInGame.stop();
		musicInMenu.stop();
		musicInMenu.loop();

		Menu = new GImage("Images/menu.png");
		Menu.setSize(WIDTH, HEIGHT);
		add(Menu);

		GLabel title = new GLabel("Breakout");
		title.setFont(new Font("Arial", Font.BOLD, 38));
		title.setColor(Color.WHITE);
		add(title, (WIDTH - title.getWidth()) / 2, 55);

		double iconWidth = 96;
		double iconHeight = 144;
		double gap = 18;
		double rowWidth = iconWidth * 3 + gap * 2;
		double startX = (WIDTH - rowWidth) / 2;
		double iconY = 220;

		levelOneIcon = new GImage("Images/pig.png");
		levelOneIcon.setSize(iconWidth, iconHeight);
		add(levelOneIcon, startX, iconY);
		addLevelLabel("Level 1", startX, iconY, iconWidth, iconHeight);

		levelTwoIcon = new GImage("Images/pig.png");
		levelTwoIcon.setSize(iconWidth, iconHeight);
		add(levelTwoIcon, startX + iconWidth + gap, iconY);
		addLevelLabel("Level 2", startX + iconWidth + gap, iconY, iconWidth, iconHeight);

		levelThreeIcon = new GImage("Images/pig.png");
		levelThreeIcon.setSize(iconWidth, iconHeight);
		add(levelThreeIcon, startX + (iconWidth + gap) * 2, iconY);
		addLevelLabel("Level 3", startX + (iconWidth + gap) * 2, iconY, iconWidth, iconHeight);
	}

	private void setSafeVolume(AudioClip sound, double volume) {
		if (sound instanceof SoundClip) {
			((SoundClip) sound).setVolume(volume);
		}
	}

	private void playRandomWallHit() {
		double soundIndex = random.nextDouble(0, 3);
		if (soundIndex < 1) {
			WallHit1.play();
		} else if (soundIndex < 2) {
			WallHit2.play();
		} else {
			WallHit3.play();
		}
	}

	private void addLevelLabel(String text, double iconX, double iconY,
							   double iconWidth, double iconHeight) {
		double labelY = iconY + iconHeight + 24;

		GRect whiteRectangle = new GRect(iconX, labelY, iconWidth, 38);
		whiteRectangle.setFilled(true);
		whiteRectangle.setColor(Color.WHITE);
		add(whiteRectangle);

		GLabel levelLabel = new GLabel(text);
		levelLabel.setFont(new Font("Arial", Font.BOLD, 25));
		levelLabel.setColor(new Color(80, 40, 50));
		double labelX = iconX + (iconWidth - levelLabel.getWidth()) / 2;
		double textY = labelY + (38 + levelLabel.getAscent()) / 2 - 2;
		add(levelLabel, labelX, textY);
	}

	private void setup() {
		setGameBackground();
		lives = NTURNS;
		ballSpeed=3; // у лвл прибрати to fix

	}

	private void setGameBackground() {
		gameBackground = new GImage("Images/gameBackground.jpg");
		gameBackground.setSize(WIDTH, HEIGHT);
		add(gameBackground);
	}

	private void drawBricks(){
		int luckyRow = random.nextInt(0,NBRICK_ROWS);
		int luckyCol = random.nextInt(0,NBRICKS_PER_ROW);

		for(int i = 0; i<NBRICK_ROWS;i++){
			for(int j = 0; j<NBRICKS_PER_ROW;j++){

				double xBrick = BRICK_SEP+j*(BRICK_WIDTH+BRICK_SEP);
				double yBrick=BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_SEP);

				Color color;
				if(i<NBRICK_ROWS/3){
					color = Color.RED;
				}else if(i<NBRICK_ROWS*2/3){
					color = Color.GREEN;
				}else{
					color = Color.YELLOW;
				}

				boolean isLucky = (i==luckyRow && j == luckyCol);
				if(strongBricks && (isLucky || random.nextBoolean(0.25))){
					color = Color.GRAY;
				}
				add(createBrick(xBrick,yBrick,color));
			}
		}
	}

	private GObject createBrick(double x, double y , Color color){
		GRect brick = new GRect (x,y,BRICK_WIDTH,BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		return brick;
	}

	private void createBall(){
		ball = new GImage("Images/pig.png");
		int ratio = (WIDTH+HEIGHT) / 300;
		ball.setSize(ratio*BALL_RADIUS,ratio*BALL_RADIUS);
		double angle = random.nextDouble(20, 160);
		vx = Math.cos(Math.toRadians(angle)) * ballSpeed;
		vy = Math.sin(Math.toRadians(angle)) * ballSpeed;
		add(ball,getWidth()/2.0 - BALL_RADIUS,getHeight()/2.0 - BALL_RADIUS);
	}

	private void startBallFlight() {
		while (lives > 0) {
			double previousY = ball.getY();
			ball.move(vx, vy);


			if (ball.getX() <= 0) {
				ball.setLocation(0, ball.getY());
				vx = Math.abs(vx);
				playRandomWallHit();
			} else if (ball.getX() + ball.getWidth() >= getWidth()) {
				ball.setLocation(getWidth() - ball.getWidth(), ball.getY());
				vx = -Math.abs(vx);
				playRandomWallHit();
			}

			if (ball.getY() <= 0) {
				ball.setLocation(ball.getX(), 0);
				vy = Math.abs(vy);
				playRandomWallHit();
			}

			if (vy > 0
					&& previousY + ball.getHeight() <= paddle.getY()
					&& ball.getY() + ball.getHeight() >= paddle.getY()
					&& ball.getX() + ball.getWidth() > paddle.getX()
					&& ball.getX() < paddle.getX() + paddle.getWidth()) {
				ball.setLocation(ball.getX(), paddle.getY() - ball.getHeight());

				double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
				double ballCenter = ball.getX() + ball.getWidth() / 2.0;
				double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
				hitOffset = Math.max(-1, Math.min(1, hitOffset));
				vx = hitOffset * ballSpeed * 0.85;
				vy = -Math.sqrt(ballSpeed * ballSpeed - vx * vx);
				playRandomWallHit();
			}

			checkBrickCollision(previousY);

			if (ball.getY() > getHeight()) {
				lives--;
				if (lives > 0) {
					ball.setLocation(getWidth() / 2.0 - ball.getWidth() / 2.0,
							getHeight() / 2.0 - ball.getHeight() / 2.0);
					setRandomBallDirection();
				}
			}

			pause(10);
		}
	}

	private void setRandomBallDirection() {
		double angle = random.nextDouble(20, 160);
		vx = Math.cos(Math.toRadians(angle)) * ballSpeed;
		vy = Math.sin(Math.toRadians(angle)) * ballSpeed;
	}



	private void checkBrickCollision(double previousY) {
		for (int i = 0; i < getElementCount(); i++) {
			GObject object = getElement(i);
			if (!(object instanceof GRect)) {
				continue;
			}

			GRect brick = (GRect) object;
			if (brick.getWidth() != BRICK_WIDTH || brick.getHeight() != BRICK_HEIGHT) {
				continue;
			}

			boolean overlaps = ball.getX() + ball.getWidth() > brick.getX()
					&& ball.getX() < brick.getX() + brick.getWidth()
					&& ball.getY() + ball.getHeight() > brick.getY()
					&& ball.getY() < brick.getY() + brick.getHeight();
			if (overlaps) {
				if (brick.getColor().equals(Color.GRAY)) {
					brick.setColor(Color.LIGHT_GRAY);
				} else {
					brickHitted();
					remove(brick);
				}
				brickDestroyed.play();
				if (previousY + ball.getHeight() <= brick.getY()
						|| previousY >= brick.getY() + brick.getHeight()) {
					vy = -vy;
				} else {
					vx = -vx;
				}
				return;
			}
		}
	}

	private void createPaddle(){
		paddle = new GImage("Images/paddle.png");
		int paddleRatioWidth = (WIDTH/PADDLE_WIDTH)+PADDLE_WIDTH;
		int paddleRatioHeight = (HEIGHT/PADDLE_HEIGHT/10)+PADDLE_HEIGHT;
		paddle.setSize(paddleRatioWidth,paddleRatioHeight);
		add(paddle,(getWidth()-PADDLE_WIDTH)/2.0,getHeight() - PADDLE_Y_OFFSET-PADDLE_HEIGHT);
	}

	public void mouseMoved(MouseEvent e){
		if (paddle == null) {
			return;
		}
		double x = e.getX() - paddle.getWidth()/2.0;
		x = Math.max(0, Math.min(x, getWidth() - paddle.getWidth()));
		paddle.setLocation(x, paddle.getY());
	}

	public void mousePressed(MouseEvent e) {
		if (!menuActive) {
			return;
		}

		if (levelOneIcon != null && levelOneIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 1;
		} else if (levelTwoIcon != null && levelTwoIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 2;
		} else if (levelThreeIcon != null && levelThreeIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 3;
		} else {
			return;
		}

		levelChosen = true;
	}

}
