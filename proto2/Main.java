package reversi;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends Applet implements MouseListener {
	Reversi rvs;

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
	}

	/*
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		// TODO 自動生成されたメソッド・スタブ
		super.start();
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
		// TODO 自動生成されたメソッド・スタブ
		int x = e.getX();
		int y = e.getY();
		boolean onBoard = x > rvs.start[0] && x < rvs.start[0] + rvs.width * rvs.boardSize && y > rvs.start[1]
				&& y < rvs.start[1] + rvs.height * rvs.boardSize;
		if (onBoard) {
			int i = (int) ((x - rvs.start[0]) / rvs.width);
			int j = (int) ((y - rvs.start[1]) / rvs.height);
			if (rvs.enablePlace(i, j)) {
				rvs.reverseDisks(i, j);
				rvs.turn = rvs.turn ? false : true;
			}
		}

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

}
