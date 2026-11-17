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
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static int WIDTH = APPLICATION_WIDTH;
	private static int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

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

	private static final Color BACKGROUND = Color.CYAN;
	private final RandomGenerator random = RandomGenerator.getInstance();

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

	private AudioClip ballSound;
	private AudioClip brickSound;
	private AudioClip brickSound2;
	private AudioClip brickSound3;
	private AudioClip musicInMenu;
	private AudioClip musicInGame;

	private int lives;
	private double vx,vy;
	private int points;
	private double ballSpeed;


	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setup();
		musicInMenu = MediaTools.loadAudioClip("Music/menuMusic.wav");
		setSafeVolume(musicInMenu, 0.12);
		drawBricksLVL1();
		addMouseListeners();
		menu();
		
		switch (selectedLevel) {
			case 1:
				lvl1();
				break;
			case 2:
				lvl2();
				break;
			case 3:
				lvl3();
				break;
		}


	}

	private void lvl3() {
	}

	private void lvl2() {
	}

	private void lvl1() {
	}

	private void menu() {
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
		setBackground(BACKGROUND);
	}

	private void drawBricksLVL1(){
		for(int i = 0; i<NBRICK_ROWS;i++){
			for(int j = 0; j<NBRICKS_PER_ROW;j++){
				double xBrick = BRICK_SEP+j*(BRICK_WIDTH+BRICK_SEP);
				double yBrick=BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_SEP);
				GRect BRICK = new GRect(xBrick,yBrick,BRICK_WIDTH,BRICK_HEIGHT);
				BRICK.setFilled(true);
				if(i<NBRICK_ROWS/3){
					BRICK.setColor(Color.RED);
				}else if(i<NBRICK_ROWS*2/3){
					BRICK.setColor(Color.GREEN);
				}else{
					BRICK.setColor(Color.YELLOW);
				}
				add(BRICK);
			}
		}
	}

	private void createBall(){
		ball = new GImage("Images/pig.png");
		ball.setSize(2*BALL_RADIUS,2*BALL_RADIUS);
		vy=ballSpeed;
		vx=random.nextDouble(0.25,0.75)*ballSpeed;
		if(random.nextBoolean()){
			vx = -vx;
		}
		add(ball,getWidth()/2.0 - BALL_RADIUS*2,getHeight()/2.0 - BALL_RADIUS*2);
	}


	public void mousePressed(MouseEvent e) {
		GImage clickedIcon = null;

		if (levelOneIcon != null && levelOneIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 1;
			clickedIcon = levelOneIcon;
		} else if (levelTwoIcon != null && levelTwoIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 2;
			clickedIcon = levelTwoIcon;
		} else if (levelThreeIcon != null && levelThreeIcon.contains(e.getX(), e.getY())) {
			selectedLevel = 3;
			clickedIcon = levelThreeIcon;
		}

		if (clickedIcon == null) {
			return;
		}

		musicInMenu.stop();

		if (selectedLevelBorder != null) {
			remove(selectedLevelBorder);
		}

		double borderSize = 6;
		selectedLevelBorder = new GRect(
				clickedIcon.getX() - borderSize,
				clickedIcon.getY() - borderSize,
				clickedIcon.getWidth() + borderSize * 2,
				clickedIcon.getHeight() + borderSize * 2
		);
		selectedLevelBorder.setFilled(false);
		selectedLevelBorder.setColor(Color.YELLOW);
		add(selectedLevelBorder);
	}

}
