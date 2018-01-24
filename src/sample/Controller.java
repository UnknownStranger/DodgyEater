package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements Initializable {

	//obvious variables are obvious
	static int scoreValue = 0;
	static double refreshRate = 30;
	static double speed = 5;
	static int direction;
	static int delayTimer;
	static int delayTotal = 10;
	double differenceX, differenceY;
	double snakeX, snakeY;
	double lastX, lastY;
	public static boolean gameOver = false;
	public static boolean screenFilled = false;
	static boolean restart = false;


	//death squares array list

	//getting handles for fxml objects
	@FXML
	public Rectangle square;
	public Circle circle;
	public Text score;
	public Text text;
	public Pane background;
	public Canvas mycanvas;
	public Text gameOverText;
	public Text tryAgainText;

	static ArrayList<Double> boxesX = new ArrayList<Double>();
	static ArrayList<Double> boxesY = new ArrayList<Double>();

	//initialize at starting locations and maintain snake momentum as decided by moveSnake
	@FXML
	@Override
	public void initialize(URL url, ResourceBundle rb) {


		GraphicsContext gc = mycanvas.getGraphicsContext2D();
		gc.fillRect(300, 300, 15, 15);
		square.setFill(Color.GREEN);
		circle.setFill(Color.WHITE);
		score.setFill(Color.YELLOW);
		text.setFill(Color.YELLOW);
		score.setText("0");
		//initial circle spawn
		circle.setCenterX(Math.round(ThreadLocalRandom.current().nextDouble(0, 555) / 15) * 15 + 7.5);
		circle.setCenterY(Math.round(ThreadLocalRandom.current().nextDouble(0, 555) / 15) * 15 + 7.5);


		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(refreshRate),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								// Call update method for every sec.
								moveSnake(direction);
								checkCollision();
								if (delayTimer < delayTotal) {
									delayTimer++;
								} else {
									deathCheck();
								}
								if (!gameOver) {
									gameOverText.setText("");
									tryAgainText.setText("");
								}
								score.setText(Integer.toString(scoreValue));
								draw(gc);
							}
						}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();


	}

	//Feeds snake direction and speed into timeline loop
	private void moveSnake(int direction) {


		if (gameOver) {
			return;
		}

		//Snake directional movement modified by speed
		if (direction == 37) {
			if (snakeX <= 0) {
				square.setX(585);
			} else if (snakeX > 0) {
				square.setX(square.getX() - speed);
			}
		}

		if (direction == 39) {
			if (snakeX >= 585) {
				square.setX(15);
			} else if (snakeX < 600) {
				square.setX(square.getX() + speed);
			}
		}

		if (direction == 38) {
			if (snakeY <= 0) {
				square.setY(585);
			} else if (snakeY > 0) {
				square.setY(square.getY() - speed);
			}
		}

		if (direction == 40) {
			if (snakeY >= 585) {
				square.setY(15);
			} else if (snakeY < 600) {
				square.setY(square.getY() + speed);
			}
		}
		//updating snake location for other functions
		snakeX = square.getX();
		snakeY = square.getY();
	}

	private void checkCollision() {
		if (snakeX >= circle.getCenterX()) {
			differenceX = snakeX - (circle.getCenterX() - 7.5);
		} else if (circle.getCenterX() >= snakeX) {
			differenceX = (circle.getCenterX() - 7.5) - snakeX;
		}

		if (snakeY >= circle.getCenterY()) {
			differenceY = snakeY - (circle.getCenterY() - 7.5);
		} else if (circle.getCenterY() >= snakeY) {
			differenceY = (circle.getCenterY() - 7.5) - snakeY;
		}


		if (differenceX <= 7.5 && differenceY <= 7.5) {

			//sound initialization
			AudioClip collisionSound = new AudioClip(this.getClass().getClassLoader().getResource("sample/collisionSound.mp3").toString());
			collisionSound.play();
			//adding box locations to avoid for death check and food spawning
			boxesX.add(snakeX);
			boxesY.add(snakeY);
			scoreValue += 10;
			spawnCircle();
			score.setText(Integer.toString(scoreValue));
			lastX = snakeX;
			lastY = snakeY;
		}
	}

	private void deathCheck() {
		for (int i = 0; i < boxesX.size(); i++) {
			if (Math.abs(snakeX - boxesX.get(i)) <= 15 && Math.abs(snakeY - boxesY.get(i)) <= 15) {
				gameOver();
			}
		}
	}

	private void spawnCircle() {
		double circleX = Math.round(ThreadLocalRandom.current().nextDouble(0, 555) / 15) * 15 + 7.5;
		double circleY = Math.round(ThreadLocalRandom.current().nextDouble(0, 555) / 15) * 15 + 7.5;

		for (int i = 0; i < boxesX.size(); i++) {
			if (Math.abs(circleX - boxesX.get(i)) < 7.5) {
				spawnCircle();
			} else if (Math.abs(circleY - boxesY.get(i)) < 7.5) {
				spawnCircle();
			} else {
				circle.setCenterX(circleX);
				circle.setCenterY(circleY);
				delayTimer = 0;
			}
		}


	}

	private void draw(GraphicsContext gc) {
		if (lastX > 0 && lastY > 0) {
			gc.setFill(Color.RED);
			gc.fillRect(lastX, lastY, 15, 15);
			lastX = 0; lastY = 0;
		} if (gameOver && !screenFilled) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, 600, 600);
			screenFilled = true;
			restart = false;
		}
	}

	public static void setDirection(int newDirection) {
		direction = newDirection;
	}

	private void gameOver() {
		if (gameOver) {
			return;
		}
		gameOver = true;
		AudioClip deathSound = new AudioClip(this.getClass().getClassLoader().getResource("sample/deathSound.wav").toString());
		deathSound.play();

		gameOverText.setText("GameOver");
		tryAgainText.setText("Press 'Y' to try again");
	}

	public static void restart() {
		scoreValue = 0;
		gameOver = false;
		boxesX.clear();
		boxesY.clear();
		screenFilled = false;
	}
}