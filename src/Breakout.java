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

	private AudioClip ballSound;
	private AudioClip brickSound;
	private AudioClip brickSound2;
	private AudioClip brickSound3;
	private AudioClip musicInMenu;
	private AudioClip musicInGame;


	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setup();
		drawBricksLVL1();
		addMouseListeners();


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

	public void mousePressed(MouseEvent e) {

	}

}
