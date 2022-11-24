
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import org.magiclen.magicaudioplayer.AudioPlayer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class gamePanelv2 extends JPanel implements ActionListener {
	JButton jb, jb2, jb3, jb4;
	static final int SCREEN_WIDTH = 600;    //鎖定遊戲視窗的框
	static final int SCREEN_HEIGHT = 600;
	static int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
	private static final Graphics Graphics = null;
	int DELAY = 100; //畫面延遲的速度
	final int X[] = new int[GAME_UNITS];
	final int Y[] = new int[GAME_UNITS];
	int width[] = new int[30]; // 下雪的寬
	int high[] = new int[30]; // 下雪的高
	int bodyparts = 3; // 身體長度
	int pizzboxX; // 盒子的x
	int pizzboxY; // 盒子的y
	int pizzaX; // pizza的x
	int pizzaY; // pizza的y
	int sodaX; // soda的x
	int sodaY; // soda的y
	int applesEaten = 0;
	char direction = 'R'; // 蛇頭方
	int running = 1;
	int speed = 0;
	Timer timer;
	Random radom;
	boolean stop = true;
	boolean newgame = true; // 判斷是否重新一局
	boolean point = true;
	boolean changebackground1 = false, changebackground2 = false, 
			changebackground3 = false, changebackground4 = false,
			changebackground5 = false, changebackground6 = false,
			changebackground7 = false;   //判斷背景照片更換
	private BufferedImage pizzabox, bkp1, pizza, soda, bkp2, bkp3, bkp4, bkp5, bkp6, bkp7;
	boolean choose = true;
	boolean gameover = false;
	boolean issnow = true; // 判斷要下雪的時間點
	boolean ismute = true;  //判斷背景音樂是否靜音
	File bgm0 = new File("登入音樂");
	File bgm1 = new File("遊戲背景1");
	File bgm2 = new File("遊戲背景音樂2");
	File bgm3 = new File("遊戲背景音樂3");
	File bgm4 = new File("死亡音效");
	File bgm5 = new File("得分音效");
	AudioPlayer BackGroundMusic1 = AudioPlayer.createPlayer(bgm0);
	AudioPlayer BackGroundMusic2 = AudioPlayer.createPlayer(bgm1);
	AudioPlayer BackGroundMusic3 = AudioPlayer.createPlayer(bgm2);
	AudioPlayer BackGroundMusic4 = AudioPlayer.createPlayer(bgm3);
	AudioPlayer BackGroundMusic5 = AudioPlayer.createPlayer(bgm4);
	AudioPlayer BackGroundMusic6 = AudioPlayer.createPlayer(bgm5);
	int i = 1;
	int show;
	JTextField j1, j2;
	Connection conn;
	boolean name;
	String na;
	String pointofrank;
	String getname;
	String getpas;
	String getid;
	LinkedList<String> rkpoint, top5rank;
	int a = 0;


	gamePanelv2() {
		radom = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());
		running = 1;
		BackGroundMusic1.play();
		background();
		snow();

	}

	public void snow() {
		// 背景下雪
		for (int i = 0; i < 30; i++) {
			width[i] = (int) (Math.random() * 600);
			high[i] = (int) (Math.random() * 600);
		}
	}

	public void startGame() {
		// 建立新局
		running = 2;
		DELAY = 1000;
		timer = new Timer(100, this);
		timer.start();
		newApple();
		blueApple();
		orangeApple();
		direction = 'R';
		newgame = true;
		point = true;
		music();
		speed();
		changebackground1 = changebackground2 = changebackground3 = changebackground4 = changebackground5 = changebackground6 = false;
		BackGroundMusic1.stop();
		name = true;
		snow();
		try {
			j1.setVisible(false);
			j2.setVisible(false);
			jb.setVisible(false);
			jb2.setVisible(false);
		} catch (Exception e) {
		}
		i = 1;
		issnow = true;
		show = 3;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		gameOver(g);
		paintrank(g);
		drawrank(g);
		backgroundpic(g);
		paintsnow(g);
		painttop(g);
	}

	public void paintsnow(Graphics g) {
		if (issnow) {
			if (applesEaten >= 30) {
				g.setColor(Color.WHITE);
				for (int i = 0; i < width.length; i++) {
					g.fillOval(width[i], high[i], 8, 8); // 劃出30個雪點
				}

				for (int i = 0; i < 30; i++) {
					width[i]--;
					high[i]++;
					if (high[i] > 600) {
						high[i] = 0;
					}
					if (width[i] < 0) {
						width[i] = 600;
					}
				}

			}
		}
	}

	public void draw(Graphics g) {

		if (running == 2) {
			g.drawImage(bkp1, 0, 0, null);
			if (changebackground1) {
				g.drawImage(bkp2, 0, 0, null);
			}
			if (changebackground2) {
				g.drawImage(bkp3, 0, 0, null);
			}
			if (changebackground3) {
				g.drawImage(bkp4, 0, 0, null);
			}
			if (changebackground4) {
				g.drawImage(bkp5, 0, 0, null);
			}
			if (changebackground5) {
				g.drawImage(bkp6, 0, 0, null);
			}
			if (changebackground6) {
				g.drawImage(bkp7, 0, 0, null);
			}

			// 那邊是劃出格線
//			  for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { g.drawLine(i *
//			 UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); g.drawLine(0, i * UNIT_SIZE,
//			  SCREEN_WIDTH, i * UNIT_SIZE); }
			g.setColor(Color.RED); // 畫紅色的東西
			switch (show) {
			case 1:
				g.drawImage(pizzabox, pizzboxX, pizzboxY, null);
				break;
			case 2:
				g.drawImage(pizzabox, pizzboxX, pizzboxY, null);
				break;
			case 3:
				g.drawImage(pizzabox, pizzboxX, pizzboxY, null);
				break;
			}
			g.drawImage(pizza, pizzaX, pizzaY, null);
			if (applesEaten < 30) {
				// g.drawImage(soda, orangeX, orangeY, null);
				g.drawImage(soda, sodaX, sodaY, null);
			} else if (applesEaten >= 30) {
				g.drawImage(soda, -50, -50, null);
			}
			if (newgame) {
				for (int i = 0; i < bodyparts; i++) {
					if (i == 0) {
						g.setColor(new Color(0, 255, 255));
						g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
					} else {
						g.setColor(new Color(124, 252, 0));
						g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
				g.setColor(Color.orange);
				g.setFont(new Font("INK Free", Font.BOLD, 40));
				FontMetrics metrics = getFontMetrics(g.getFont());
				// Metrics 指標
				g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten)) / 2,
						g.getFont().getSize());

			} else {
				newgame = true;
				bodyparts = 3;
				for (int i = 0; i < bodyparts; i++) {
					if (i == 0) {
						g.setColor(new Color(50, 205, 50));
						g.fillRect(X[i] = 0, Y[i] = 0, UNIT_SIZE, UNIT_SIZE);
					} else {
						g.setColor(new Color(124, 252, 0));
						g.fillRect(X[i] = 0, Y[i] = 0, UNIT_SIZE, UNIT_SIZE);
					}
				}
			}

		}
		if (!stop) {
			if (changebackground6) {
				g.drawImage(bkp7, 0, 0, null);
			} else if (changebackground5) {
				g.drawImage(bkp6, 0, 0, null);
			} else if (changebackground4) {
				g.drawImage(bkp5, 0, 0, null);
			} else if (changebackground3) {
				g.drawImage(bkp4, 0, 0, null);
			} else if (changebackground2) {
				g.drawImage(bkp3, 0, 0, null);
			} else if (changebackground1) {
				g.drawImage(bkp2, 0, 0, null);
			} else {
				g.drawImage(bkp1, 0, 0, null);
			}
			g.setColor(Color.red);
			g.setFont(new Font("INK Free", Font.BOLD, 75));
//			FontMetrics metrics = getFontMetrics(g.getFont());
			// Metrics 指標
			g.drawString("stop", 225, 300);
		}
	}

	public void backgroundpic(Graphics g) {
		if (running == 1) {

			setBackground(Color.BLACK);
			g.setColor(new Color(0, 127, 255));
			g.setFont(new Font("INK Free", Font.BOLD, 75));
			g.drawString("1.solo", 125, 200);
			g.setColor(new Color(255, 115, 0));
			g.drawString("2.rank", 120, 300);
			g.setColor(new Color(220, 20, 60));
			g.drawString("3.score", 120, 400);
			g.setColor(new Color(210, 105, 30));
			g.drawString("4.Top5", 120, 500);
		}
	}

	public void speed() {
		if (applesEaten >= 50) {
			timer.setDelay(40);
		} else if (applesEaten >= 40) {
			timer.setDelay(45);
		} else if (applesEaten >= 30) {
			timer.setDelay(50);
			changebackground6 = true;
			BackGroundMusic3.stop();
			BackGroundMusic4.play();
		} else if (applesEaten >= 25) {
			if (i == 9) {
				timer.setDelay(60);
				i = 10;
			} else if (i == 10) {
				if (timer.getDelay() == 50) {
					timer.setDelay(50);
				} else {
					timer.setDelay(60);
				}

			}
			changebackground5 = true;
		} else if (applesEaten >= 20) {
			if (i == 7) {
				timer.setDelay(70);
				i = 8;
			} else if (i == 8) {
				i = 9;
				if (timer.getDelay() == 50) {
					timer.setDelay(50);
				} else {
					timer.setDelay(70);
				}
			}
			changebackground4 = true;
		} else if (applesEaten >= 15) {
			if (i == 5) {
				timer.setDelay(80);
				i = 6;
			} else if (i == 6) {
				i = 7;
				if (timer.getDelay() == 50) {
					timer.setDelay(50);
				} else {
					timer.setDelay(80);
				}
			}
			changebackground3 = true;
			BackGroundMusic2.stop();
			BackGroundMusic3.play();
		} else if (applesEaten >= 10) {
			if (i == 3) {
				timer.setDelay(90);
				i = 4;
			} else if (i == 4) {
				i = 5;
				if (timer.getDelay() == 50) {
					timer.setDelay(50);
				} else {
					timer.setDelay(90);
				}
			}
			changebackground2 = true;
		} else if (applesEaten >= 5) {
			if (i == 1) {
				timer.setDelay(95);
				i = 2;
			} else if (i == 2) {
				i = 3;
				if (timer.getDelay() == 50) {
					timer.setDelay(50);
				} else {
					timer.setDelay(95);
				}
			}
			changebackground1 = true;
		}

	}

	public void newApple() {
		pizzboxX = radom.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * 25;
		pizzboxY = radom.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * 25;

	}

	public void blueApple() {
		pizzaX = radom.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * 25;
		pizzaY = radom.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * 25;
	}

	public void orangeApple() {
		sodaX = radom.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * 25;
		sodaY = radom.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * 25;

	}

	public void move() {
		for (int i = bodyparts; i > 0; i--) {
			X[i] = X[i - 1]; // 讓身體跟上頭
			Y[i] = Y[i - 1]; // 讓身體跟上頭
		}
		switch (direction) {
		case 'U':
			Y[0] = Y[0] - UNIT_SIZE;
			break;
		case 'D':
			Y[0] = Y[0] + UNIT_SIZE;
			break;
		case 'L':
			X[0] = X[0] - UNIT_SIZE;
			break;
		case 'R':
			X[0] = X[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkApple() {
		if (X[0] == pizzboxX && Y[0] == pizzboxY) {
			bodyparts++;
			applesEaten++;
			newApple();
			BackGroundMusic6.play();
//			show=3;
			DELAY = 0;
		}
		if (X[0] == pizzaX && Y[0] == pizzaY) {
			bodyparts--;
			applesEaten++;
			blueApple();
			BackGroundMusic6.play();
		}
		if (X[0] == sodaX && Y[0] == sodaY) {
			BackGroundMusic6.play();
			timer.setDelay(50);
			applesEaten++;
			orangeApple();
		}
	}

	public void checkCollision() {
		// checkCollision 檢查碰撞
		// 下面101檢查蛇頭有沒有碰到身體
		for (int i = bodyparts; i > 0; i--) {
			if ((X[0] == X[i]) && Y[0] == Y[i]) {
				running = 3;
				timer.stop();
				newgame = false;
				point = false;
				issnow = false;
				gameover = true;
				BackGroundMusic5.play();
				name = false;
			}
		}
		// 檢查蛇頭有沒有碰到左邊欄位
		if (X[0] < 0) { // 左邊的0是視窗的基礎點x=0
			running = 3;
			timer.stop();
			newgame = false;
			point = false;
			gameover = true;
			BackGroundMusic5.play();
			name = false;
			issnow = false;
		}
		// 檢查蛇頭有沒有碰到右邊欄位
		if (X[0] > SCREEN_WIDTH - 25) {
			running = 3;
			timer.stop();
			newgame = false;
			point = false;
			gameover = true;
			BackGroundMusic5.play();
			name = false;
			issnow = false;
		}
		// 檢查蛇頭有沒有碰到上方欄位
		if (Y[0] < 0) {
			running = 3;
			timer.stop();
			newgame = false;
			point = false;
			gameover = true;
			BackGroundMusic5.play();
			name = false;
			issnow = false;
		}
		// 檢查蛇頭有沒有碰到下方欄位
		if (Y[0] > SCREEN_HEIGHT - 25) {
			running = 3;
			timer.stop();
			newgame = false;
			point = false;
			gameover = true;
			BackGroundMusic5.play();
			name = false;
			issnow = false;
		}
		if (bodyparts <= 1) {
			running = 3;
			timer.stop();
			newgame = false;
			point = false;
			gameover = true;
			BackGroundMusic5.play();
			name = false;
			issnow = false;
		}
	}

	public void gameOver(Graphics g) {
		// Metrics 指標
		// 畫出分數
		if (running == 3) {
			if (changebackground6) {
				g.drawImage(bkp7, 0, 0, null);
			} else if (changebackground5) {
				g.drawImage(bkp6, 0, 0, null);
			} else if (changebackground4) {
				g.drawImage(bkp5, 0, 0, null);
			} else if (changebackground3) {
				g.drawImage(bkp4, 0, 0, null);
			} else if (changebackground2) {
				g.drawImage(bkp3, 0, 0, null);
			} else if (changebackground1) {
				g.drawImage(bkp2, 0, 0, null);
			} else {
				g.drawImage(bkp1, 0, 0, null);
			}
			g.setColor(Color.orange);
			g.setFont(new Font("INK Free", Font.BOLD, 40));
			FontMetrics metrics1 = getFontMetrics(g.getFont());
			g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:" + applesEaten)) / 2,
					g.getFont().getSize());
			g.setColor(Color.red);
			g.setFont(new Font("INK Free", Font.BOLD, 75));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			// Metrics 指標
			g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
			g.setFont(new Font("INK Free", Font.BOLD, 40));
			g.drawString("Press R To New Game", 100, 400);
			repaint();
			if (j1 != null) {
				if (name == false) {
					getpoint();
				}
				name = true;
			}
		}
	}

	public void rank() {
		running = 4;
		jb3 = new JButton();
		j1 = new JTextField("name");
		j2 = new JTextField("password");
		jb2 = new JButton("sign");
		jb = new JButton("login");
		j1.setBounds(230, 250, 100, 30);
		j2.setBounds(230, 300, 100, 30);
		jb2.setBounds(400, 250, 100, 30);
		jb.setBounds(400, 300, 100, 30);
		add(j1);
		add(j2);
		add(jb);
		add(jb2);
		addEventListener();
		jb2.addActionListener(this);
		jb.addActionListener(this);
		repaint();
	}

	public void showrank() {

		if (j1 == null || j2 == null) {
			j1 = new JTextField("name");
			j2 = new JTextField("password");
			jb2 = new JButton();
			jb3 = new JButton("login");
			jb = new JButton();
			j1.setBounds(230, 250, 100, 30);
			j2.setBounds(230, 300, 100, 30);
			jb3.setBounds(400, 300, 100, 30);
			add(j1);
			add(j2);
			add(jb3);
			addEventListener();
			jb3.addActionListener(this);
			running = 5;
		} else {
			running = 5;
		}
		try {
			showpoint();
		} catch (Exception e) {
		}
		repaint();
	}

	public void paintrank(Graphics g) {

		if (running == 4) {
			repaint();
			setBackground(Color.black);
			g.setFont(new Font("INK Free", Font.BOLD, 55));
			g.setColor(Color.ORANGE);
			g.drawString("Create Name", 125, 200);
			repaint();

		}
	}

	public void showtopfive() {
		running = 6;
		repaint();
		topfive();
	}

	public void painttop(Graphics g) {
		if (running == 6) {
			setBackground(Color.YELLOW);
			g.setFont(new Font("ink free", Font.BOLD, 45));
			g.setColor(Color.black);
			g.drawString("Top 5", 250, 50);
			g.drawString("id", 100, 100);
			g.drawString("name", 250, 100);
			g.drawString("score", 425, 100);
			repaint();
			try {
				g.drawString(top5rank.getFirst(), 100, 175);
				g.drawString(top5rank.get(1), 250, 175);
				g.drawString(top5rank.get(2), 425, 175);
				g.drawString(top5rank.get(3), 100, 250);
				g.drawString(top5rank.get(4), 250, 250);
				g.drawString(top5rank.get(5), 425, 250);
				g.drawString(top5rank.get(6), 100, 325);
				g.drawString(top5rank.get(7), 250, 325);
				g.drawString(top5rank.get(8), 425, 325);
				g.drawString(top5rank.get(9), 100, 400);
				g.drawString(top5rank.get(10), 250, 400);
				g.drawString(top5rank.get(11), 425, 400);
				g.drawString(top5rank.get(12), 100, 475);
				g.drawString(top5rank.get(13), 250, 475);
				g.drawString(top5rank.get(14), 425, 475);
				repaint();
			} catch (Exception e) {
			}
		}
	}

	public void drawrank(Graphics g) {
		if (running == 5) {
			setBackground(Color.YELLOW);
			g.setFont(new Font("INK Free", Font.BOLD, 45));
			g.setColor(Color.black);
			g.drawString(j1.getText(), 225, 100);
			repaint();
			try {
				g.drawString(rkpoint.getFirst(), 150, 150);
				g.drawString(rkpoint.get(1), 350, 150);
				g.drawString(rkpoint.get(2), 150, 225);
				g.drawString(rkpoint.get(3), 350, 225);
				g.drawString(rkpoint.get(4), 150, 300);
				g.drawString(rkpoint.get(5), 350, 300);
				g.drawString(rkpoint.get(6), 150, 375);
				g.drawString(rkpoint.get(7), 350, 375);
				g.drawString(rkpoint.get(8), 150, 450);
				g.drawString(rkpoint.get(9), 350, 450);
				repaint();
			} catch (Exception e) {
			}
		}
	}

	public void musicstop() {
		if (ismute) {
			BackGroundMusic2.mute();
			BackGroundMusic3.mute();
			BackGroundMusic4.mute();
			ismute = false;
		} else {
			BackGroundMusic2.fullPower();
			BackGroundMusic3.fullPower();
			BackGroundMusic4.fullPower();
			ismute = true;
		}
	}

	public void restart() {
		BackGroundMusic2.stop();
		BackGroundMusic3.stop();
		BackGroundMusic4.stop();
		if (point == false) {
			applesEaten = 0;
			point = true;
		}
		changebackground1 = changebackground2 = changebackground3 = changebackground4 = changebackground5 = changebackground6 = false;
		startGame();
		repaint();
		newgame = false;
		point = false;
		timer.setDelay(100);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running == 2) {
			move();
			checkApple();
			checkCollision();
			speed();
		}
		repaint();
	}

	private void addEventListener() {
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (creatname()) {
					j1.setVisible(false);
					j2.setVisible(false);
					jb.setVisible(false);
					jb2.setVisible(false);
					startGame();
				}
			}
		});
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sign()) {
					j1.setVisible(false);
					j2.setVisible(false);
					jb.setVisible(false);
					jb2.setVisible(false);
					startGame();
				} else {
				}
			}
		});
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sign()) {
					j1.setVisible(false);
					j2.setVisible(false);
					jb.setVisible(false);
					jb3.setVisible(false);
					showpoint();
				} else {
				}
			}
		});
	}

	class myKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE:
				if (stop) {
					BackGroundMusic2.pause();
					BackGroundMusic3.pause();
					BackGroundMusic4.pause();
					timer.stop();
					stop = false;
					repaint();
				} else {
					if (applesEaten >= 30) {
						BackGroundMusic4.play();
					} else if (applesEaten >= 15) {
						BackGroundMusic3.play();
					} else if (applesEaten < 15) {
						BackGroundMusic2.play();
					}
					timer.start();
					stop = true;
					repaint();
				}
				break;
			}
		}

		public void keyReleased(KeyEvent e) {
			// 依照按鍵數字不同執行不同模式或方法
			if (e.getKeyCode() == 82) {
				// 按r重新一局
				if (newgame == false) {
					restart();
				}
			}
			if (e.getKeyCode() == 49) {
				// 按1開始新一局
				startGame();
			}
			if (e.getKeyCode() == 87) {
				// 按w存重照片
				saveJPEG();
			}
			if (e.getKeyCode() == 50) {
				// 按2進入積分模式
				rank();
			}
			if (e.getKeyCode() == 51) {
				// 按3查看自己積分
				showrank();
			}
			if (e.getKeyCode() == 52) {
				// 按4查看最高的前5位
				showtopfive();
			}
			if (e.getKeyCode() == 53) {
				// 按5遊戲背景靜音
				musicstop();
			}

		}
	}

	public void background() {
		try {
			bkp1 = ImageIO.read(new File("image/road.jpg"));
			bkp2 = ImageIO.read(new File("image/Tokyo.jpg"));
			bkp3 = ImageIO.read(new File("image/greecejpg.jpg"));
			bkp4 = ImageIO.read(new File("image/londan.jpg"));
			bkp5 = ImageIO.read(new File("image/island.jpg"));
			bkp6 = ImageIO.read(new File("image/boston.jpg"));
			bkp7 = ImageIO.read(new File("image/sky.jpg"));
			pizza = ImageIO.read(new File("image/pizza.png"));
			soda = ImageIO.read(new File("image/soda.png"));
			pizzabox = ImageIO.read(new File("image/pizzabox.png"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void music() {
		BackGroundMusic2.play();
	}

	public void saveJPEG() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		paint(g2d);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss");
		String time = sdf.format(date);
		try {
			ImageIO.write(img, "jpeg", new File("duck/" + time + ".jpg"));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public boolean sign() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/rank", "root", "root");
			String sql = "select * from member where name=? and password=?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, j1.getText());
			pst.setString(2, j2.getText());
			ResultSet rs = pst.executeQuery();
			rs.next();
			getname = rs.getString("name");
			getpas = rs.getString("password");
			getid = rs.getString("id");
			if (j1.getText().equals(getname) && j2.getText().equals(getpas)) {
				conn.close();
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.toString() + "sign");
		}
		return false;
	}

	public void getpoint() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.100.164:3306/rank", "root", "root");
			String sql = "INSERT INTO point (nameid,point) VALUES (?,?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(2, applesEaten + "");
			pst.setString(1, getid);
			pst.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public boolean creatname() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.100.164:3306/rank", "root", "root");
			String sql = "INSERT into member (name,password) values(?,?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, j1.getText());
			pst.setString(2, j2.getText());
			pst.executeUpdate();
			String sql2 = "select * from member where name=? &&password=?";
			PreparedStatement pst2 = conn.prepareStatement(sql2);
			pst2.setString(1, j1.getText());
			pst2.setString(2, j2.getText());
			ResultSet rs = pst2.executeQuery();
			rs.next();
			getid = rs.getString("id");
			conn.close();
			return true;

		} catch (Exception e) {
			System.out.println(e.toString() + "creatname");
		}
		return false;
	}

	public boolean showpoint() {
		rkpoint = new LinkedList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/rank", "root", "root");
			String sql = "SELECT gamecount,name ,point from point RIGHT JOIN member on nameid =id WHERE name =? && password=? ORDER BY point DESC LIMIT 5";

			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, j1.getText());
			pst.setString(2, j2.getText());
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String poi = rs.getString("point");
				rkpoint.add(name);
				rkpoint.add(poi);
			}
			if (na == null && pointofrank == null) {
				return false;
			}
			conn.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return true;
	}

	public boolean topfive() {
		top5rank = new LinkedList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/rank", "root", "root");
			String sql = "SELECT id,name ,point from point RIGHT JOIN member on nameid =id ORDER BY point DESC LIMIT 5";

			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String point = rs.getString("point");
				top5rank.add(id);
				top5rank.add(name);
				top5rank.add(point);
			}
			if (na == null && pointofrank == null) {
				return false;
			}
			conn.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return true;
	}
}
