package reversi;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends Applet implements MouseListener {
	Reversi rvs;
	int px, py;
	String msg;
	boolean isPlaying;

	/*
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		super.init();
		rvs = new Reversi();
		int width = rvs.start[0] * 2 + rvs.width * rvs.boardSize;
		int height = rvs.start[1] * 2 + rvs.height * rvs.boardSize + 200;
		setSize(width, height); // アップレットのサイズを指定
		addMouseListener(this);
		// addMouseMotionListener(this);
		px = 0;
		py = 0;
		isPlaying = true;
	}

	/*
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		// TODO 自動生成されたメソッド・スタブ
		super.start();
		rvs.initBoard();
	}

	/*
	 * @see java.applet.Applet#stop()
	 */
	@Override
	public void stop() {
		// TODO 自動生成されたメソッド・スタブ
		super.stop();
	}

	/*
	 * @see java.applet.Applet#destroy()
	 */
	@Override
	public void destroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.destroy();
	}

	/*
	 * @see java.applet.Applet#paint()
	 */
	@Override
	public void paint(Graphics g) {
		// TODO 自動生成されたメソッド・スタブ
		Dimension size = getSize();
		// 背景色の設定
		g.setColor(Color.white);
		g.fillRect(0, 0, size.width - 1, size.height - 1);

		showBoard(g); // 盤の表示

		rvs.setDisks(g);
		int textY = rvs.start[1] + rvs.height * rvs.boardSize + 50;
		g.setColor(Color.white);
		g.fillRect(rvs.start[0], textY, 200, 100);

		g.setColor(Color.black);

		Font fnt = new Font("SansSerif", Font.BOLD, 22);
		g.setFont(fnt);
		if (isPlaying) {
			if (rvs.turn) {
				msg = "黒の番です";
				g.drawString(msg, rvs.start[0], textY);
			} else {
				msg = "白の番です";
				g.drawString(msg, rvs.start[0], textY);
			}
		} else {
			msg = rvs.getWinner();
			g.drawString(msg, rvs.start[0], textY);
			destroy();
		}
		g.setColor(Color.black);
		String point = "黒 " + rvs.diskCounts[0] + " vs " + rvs.diskCounts[1] + " 白";
		g.drawString(point, 200, textY);
	}

	/*
	 * @see java.applet.Applet#repiant()
	 */
	@Override
	public void repaint() {
		// TODO 自動生成されたメソッド・スタブ
		super.repaint();
	}

	/*
	 * @see java.applet.Applet#update()
	 */
	public void update() {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int x = e.getX();
		int y = e.getY();
		boolean onBoard = x > rvs.start[0] && x < rvs.start[0] + rvs.width * rvs.boardSize && y > rvs.start[1]
				&& y < rvs.start[1] + rvs.height * rvs.boardSize;
		if (onBoard) {
			int i = (int) ((x - rvs.start[0]) / rvs.width);
			int j = (int) ((y - rvs.start[1]) / rvs.height);
			if (rvs.enablePlace(i, j, rvs.turn)) {
				rvs.reverseDisks(i, j);
				rvs.turn = rvs.turn ? false : true;
				if (!rvs.enablePlace(rvs.turn)) {
					rvs.turn = rvs.turn ? false : true;
					if (!rvs.enablePlace(rvs.turn)) {
						isPlaying = false;
					}
				}
			}
		}
		rvs.countDisk();

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void showBoard(Graphics g) {
		int width = rvs.width;
		int height = rvs.height;
		g.setColor(Color.green);
		g.fillRect(rvs.start[0], rvs.start[1], width * rvs.boardSize, height * rvs.boardSize);
		Color lightgreen = new Color(173, 255, 47);
		for (int i = 0; i < rvs.boardSize; i++) {
			for (int j = 0; j < rvs.boardSize; j++) {
				int x0 = rvs.start[0] + width * i;
				int y0 = rvs.start[1] + height * j;
				if (rvs.enablePlace(i, j, rvs.turn)) {
					g.setColor(Color.red);
					g.drawRect(x0, y0, width - 1, height - 1);
					g.setColor(lightgreen);
					g.fillRect(x0 + 1, y0 + 1, width - 2, height - 2);
				} else {
					g.setColor(Color.black);
					g.drawRect(x0, y0, width - 1, height - 1);
				}
			}
		}
	}
}
