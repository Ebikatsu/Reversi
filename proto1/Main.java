import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.util.Arrays;

public class Main extends Applet implements MouseListener {
	int boardSize = 12; // 盤面の大きさ
	int[] start = { 30, 30 }; // 盤面の位置
	int width = 50; // マスの横幅
	int height = 50; // マスの縦幅
	int[] changeNum = new int[8];

	/*
	 * 石の配置
	 * 0：何もない
	 * 1：黒
	 * 2：白
	 */
	int[][] board = new int[boardSize][boardSize];
	boolean turn = true;

	/*
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		super.init();
		setSize(1000, 1000);
		addMouseListener(this);
	}

	public void start() {
		initBoard();
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		// 盤の表示
		for (int i = start[0]; i < start[0] + width * boardSize; i += width) {
			for (int j = start[1]; j < start[1] + height * boardSize; j += height) {
				g.setColor(Color.black);
				g.drawRect(i, j, width, height);
				g.setColor(Color.green);
				g.fillRect(i + 1, j + 1, width - 1, height - 1);
			}
		}
		setDisks(g);
        int textY = start[1] + height*boardSize+20;
		g.setColor(Color.white);
		g.fillRect(start[0], textY, 200, 100);

		g.setColor(Color.black);

		Font fnt = new Font("SansSerif", Font.BOLD, 22);
		g.setFont(fnt);
		if (turn)
			g.drawString("黒の番です。", start[0], textY+40);
		else
			g.drawString("白の番です。", start[0], textY+40);
	}

	/**
	 * 石の配置の初期化
	 */
	public void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 0;
			}
		}
        int X = (int)(boardSize/2);
        int Y = (int)(boardSize/2);

		board[X-1][Y-1] = 2;
		board[X-1][Y] = 1;
		board[X][Y-1] = 1;
		board[X][Y] = 2;
	}

	/**
	 * 石を配置する
	 */
	public void setDisks(Graphics g) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				int x = start[0] + width * i + (int) (width * 0.1);
				int y = start[1] + height * j + (int) (height * 0.1);

				if (board[i][j] == 1) {
					g.setColor(Color.black);
					g.fillOval(x, y, (int) (width * 0.8), (int) (height * 0.8));
				} else if (board[i][j] == 2) {
					g.setColor(Color.white);
					g.fillOval(x, y, (int) (width * 0.8), (int) (height * 0.8));
				}
			}
		}
	}

	/**
	 * その位置に石が置けるか判定
	 * 置ける場合はtrue、それ以外はfalseを返す
	 * @param i
	 * @param j
	 * @return place
	 */
	public boolean enablePlace(int i, int j) {
		boolean place = false;
		int disk = turn ? 1 : 2;
		int diskR = turn ? 2 : 1;
		Arrays.fill(changeNum, 0);

		if (board[i][j] == 0) {
			// 上
			for (int k = 1; k < j + 1; k++) {
				if (board[i][j - k] == 0) { // 石が置かれていないとき
					break;
				} else if (board[i][j - k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[1] = k - 1;
						break;
					}
				} else if (board[i][j - k] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 右
			for (int k = 1; k < boardSize - i; k++) {
				if (board[i + k][j] == 0) { // 石が置かれていないとき
					break;
				} else if (board[i + k][j] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[3] = k - 1;
						break;
					}
				} else if (board[i + k][j] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 下
			for (int k = 1; k < boardSize - j; k++) { // 石が置かれていないとき
				if (board[i][j + k] == 0) {
					break;
				} else if (board[i][j + k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[5] = k - 1;
						break;
					}
				} else if (board[i][j + k] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 左
			for (int k = 1; k < i + 1; k++) { // 石が置かれていないとき
				if (board[i - k][j] == 0) {
					break;
				} else if (board[i - k][j] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[7] = k - 1;
						break;
					}
				} else if (board[i - k][j] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 左上
			for (int k = 1; k < i + 1 && k < j + 1; k++) { // 石が置かれていないとき
				if (board[i - k][j - k] == 0) {
					break;
				} else if (board[i - k][j - k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[0] = k - 1;
						break;
					}
				} else if (board[i - k][j - k] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 右上
			for (int k = 1; k < boardSize - i && k < j + 1; k++) { // 石が置かれていないとき
				if (board[i + k][j - k] == 0) {
					break;
				} else if (board[i + k][j - k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[2] = k - 1;
						break;
					}
				} else if (board[i + k][j - k] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 右下
			for (int k = 1; k < boardSize - i && k < boardSize - j; k++) { // 石が置かれていないとき
				if (board[i + k][j + k] == 0) {
					break;
				} else if (board[i + k][j + k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[4] = k - 1;
						break;
					}
				} else if (board[i + k][j + k] == diskR) { // 相手の石と同じ色のとき

				}
			}
			// 左下
			for (int k = 1; k < i + 1 && k < boardSize - j; k++) { // 石が置かれていないとき
				if (board[i - k][j + k] == 0) {
					break;
				} else if (board[i - k][j + k] == disk) { // 自分の石と同じ色のとき
					if (k == 1) {
						break;
					} else {
						place = true;
						changeNum[6] = k - 1;
						break;
					}
				} else if (board[i - k][j + k] == diskR) { // 相手の石と同じ色のとき

				}
			}
		}
		return place;

	}

	/**
	 * 石を置けるかどうか判定
	 */
	public boolean enablePlace() {
		boolean place = false;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				place |= enablePlace(i, j);
			}
		}
		return place;
	}

	/**
	 *
	 * @param i
	 * @param j
	 */
	public void reverseDisks(int i, int j) {
		int myDisk = turn ? 1 : 2;
		for (int k = 0; k <= changeNum[1]; k++) {
			board[i][j - k] = myDisk;
		}
		for (int k = 0; k <= changeNum[3]; k++) {
			board[i + k][j] = myDisk;
		}
		for (int k = 0; k <= changeNum[5]; k++) {
			board[i][j + k] = myDisk;
		}
		for (int k = 0; k <= changeNum[7]; k++) {
			board[i - k][j] = myDisk;
		}
		for (int k = 0; k <= changeNum[0]; k++) {
			board[i - k][j - k] = myDisk;
		}
		for (int k = 0; k <= changeNum[2]; k++) {
			board[i + k][j - k] = myDisk;
		}
		for (int k = 0; k <= changeNum[4]; k++) {
			board[i + k][j + k] = myDisk;
		}
		for (int k = 0; k <= changeNum[6]; k++) {
			board[i - k][j + k] = myDisk;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int x = e.getX();
		int y = e.getY();
		boolean onBoard = x > start[0] && x < start[0] + width * boardSize && y > start[1]
				&& y < start[1] + height * boardSize;
		if (onBoard) {
			int i = (int) ((x - start[0]) / width);
			int j = (int) ((y - start[1]) / height);
			if (enablePlace(i, j)) {
				reverseDisks(i, j);
				turn = turn ? false : true;
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
