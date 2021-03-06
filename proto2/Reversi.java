package reversi;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class Reversi {

	public Reversi() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	int boardSize = 8; // 1列のマスの数
	int[] start = { 30, 30 }; // 盤面の位置
	int width = 50; // マスの横幅
	int height = 50; // マスの縦幅
	/*
	 * 石を反転させる数 添え字は8方位のうちの対応したいずれかである
	 *
	 * changeNum[i] > 0 その数だけ反転させる changeNum[i] == 0 反転する可能性がある changeNum[i] < 0
	 * 反転しない
	 */
	int[] changeNum = new int[8];
	/*
	 * 石の配置 0：何もない 1：黒 2：白
	 */
	int[][] board = new int[boardSize][boardSize];
	boolean turn = true; //
	int[] diskCounts = { 2, 2 }; // 石の数
	ArrayList<Integer> checkPlace = new ArrayList<Integer>(); // 置けるかどうか確認する場所

	/**
	 * 石の配置の初期化
	 */
	public void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 0;
			}
		}
		int cx = (int) (boardSize / 2) - 1;
		int cy = (int) (boardSize / 2) - 1;

		board[cx][cy] = 2;
		board[cx][cy + 1] = 1;
		board[cx + 1][cy] = 1;
		board[cx + 1][cy + 1] = 2;
	}

	/**
	 * checkPlace の初期化 盤面の初期状態の石の周り12か所すべてを追加
	 */
	public void initCheckPlace() {
		checkPlace = new ArrayList<Integer>();
		int cx = (int) (boardSize / 2) - 1;
		int cy = (int) (boardSize / 2) - 1;

		checkPlace.add((cy - 1) * boardSize + (cx - 1));
		checkPlace.add((cy - 1) * boardSize + cx);
		checkPlace.add((cy - 1) * boardSize + (cx + 1));
		checkPlace.add((cy - 1) * boardSize + (cx + 2));
		checkPlace.add(cy * boardSize + (cx - 1));
		checkPlace.add(cy * boardSize + (cx + 1));
		checkPlace.add((cy + 1) * boardSize + (cx - 1));
		checkPlace.add((cy + 1) * boardSize + (cx + 1));
		checkPlace.add((cy + 2) * boardSize + (cx - 1));
		checkPlace.add((cy + 2) * boardSize + cx);
		checkPlace.add((cy + 2) * boardSize + (cx + 1));
		checkPlace.add((cy + 2) * boardSize + (cx + 2));
	}

	public void updateCheckPlace(int x, int y) {
		if (x - 1 >= 0 && y - 1 >= 0 && x - 1 < boardSize && y - 1 < boardSize && board[x - 1][y - 1] == 0) {
			checkPlace.add((y - 1) * boardSize + (x - 1));
		}
		if (x - 1 >= 0 && y >= 0 && x - 1 < boardSize && y < boardSize && board[x - 1][y] == 0) {
			checkPlace.add(y * boardSize + (x - 1));
		}
		if (x - 1 >= 0 && y + 1 >= 0 && x - 1 < boardSize && y + 1 < boardSize && board[x - 1][y + 1] == 0) {
			checkPlace.add((y + 1) * boardSize + (x - 1));
		}
		if (x >= 0 && y - 1 >= 0 && x < boardSize && y - 1 < boardSize && board[x][y - 1] == 0) {
			checkPlace.add((y - 1) * boardSize + (x));
		}
		if (x >= 0 && y + 1 >= 0 && x < boardSize && y + 1 < boardSize && board[x][y + 1] == 0) {
			checkPlace.add((y + 1) * boardSize + (x));
		}
		if (x + 1 >= 0 && y - 1 >= 0 && x + 1 < boardSize && y - 1 < boardSize && board[x + 1][y - 1] == 0) {
			checkPlace.add((y - 1) * boardSize + (x + 1));
		}
		if (x + 1 >= 0 && y >= 0 && x + 1 < boardSize && y < boardSize && board[x + 1][y] == 0) {
			checkPlace.add(y * boardSize + (x + 1));
		}
		if (x + 1 >= 0 && y + 1 >= 0 && x + 1 < boardSize && y + 1 < boardSize && board[x + 1][y + 1] == 0) {
			checkPlace.add((y + 1) * boardSize + (x + 1));
		}
		if (x >= 0 && y >= 0 && x < boardSize && y < boardSize && board[x][y] != 0) {
			checkPlace.remove((Integer) (y * boardSize + x));
		}
	}

	/**
	 * 石の配置を描画する
	 *
	 * @param g
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
	 * その位置に石が置けるか判定 置ける場合はtrue、それ以外はfalseを返す
	 *
	 * @param i
	 *                置きたいマスのx軸方向の位置
	 * @param j
	 *                置きたいマスのy軸方向の位置
	 * @return place 置けるかどうか
	 */
	public boolean enablePlace(int i, int j, boolean turn) {
		boolean place = false;
		int disk = turn ? 1 : 2;
		int diskR = turn ? 2 : 1;
		Arrays.fill(changeNum, 0);
		if (i >= 0 && j >= 0 && i < boardSize && j < boardSize && board[i][j] == 0)
			for (int k = 1; k < boardSize; k++) {
				if (i - k >= 0 && j - k >= 0 && changeNum[0] == 0) { // 左上
					if (board[i - k][j - k] == 0) { // 石が置かれていないとき
						changeNum[0] = -1;
					} else if (board[i - k][j - k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[0] = -1;
						} else {
							place = true;
							changeNum[0] = k - 1;
						}
					} else if (board[i][j - k] == diskR) { // 相手の石と同じ色のとき
						if (i - k == 0 || j - k == 0) {
							changeNum[0] = -1;
						}
					}
				}
				if (j - k >= 0 && changeNum[1] == 0) { // 上
					if (board[i][j - k] == 0) { // 石が置かれていないとき
						changeNum[1] = -1;
					} else if (board[i][j - k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[1] = -1;
						} else {
							place = true;
							changeNum[1] = k - 1;
						}
					} else if (board[i][j - k] == diskR) { // 相手の石と同じ色のとき
						if (j - k == 0) {
							changeNum[1] = -1;
						}
					}
				}
				if (i + k < boardSize && j - k >= 0 && changeNum[2] == 0) { // 右上
					if (board[i + k][j - k] == 0) { // 石が置かれていないとき
						changeNum[2] = -1;
					} else if (board[i + k][j - k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[2] = -1;
						} else {
							place = true;
							changeNum[2] = k - 1;
						}
					} else if (board[i + k][j - k] == diskR) { // 相手の石と同じ色のとき
						if (i + k == boardSize || j - k == 0) {
							changeNum[2] = -1;
						}
					}
				}
				if (i + k < boardSize && changeNum[3] == 0) { // 右
					if (board[i + k][j] == 0) { // 石が置かれていないとき
						changeNum[3] = -1;
					} else if (board[i + k][j] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[3] = -1;
						} else {
							place = true;
							changeNum[3] = k - 1;
						}
					} else if (board[i + k][j] == diskR) { // 相手の石と同じ色のとき
						if (i + k == boardSize) {
							changeNum[3] = -1;
						}
					}
				}
				if (i + k < boardSize && j + k < boardSize && changeNum[4] == 0) { // 右下
					if (board[i + k][j + k] == 0) { // 石が置かれていないとき
						changeNum[4] = -1;
					} else if (board[i + k][j + k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[4] = -1;
						} else {
							place = true;
							changeNum[4] = k - 1;
						}
					} else if (board[i + k][j + k] == diskR) { // 相手の石と同じ色のとき
						if (i + k == boardSize || j + k == boardSize) {
							changeNum[4] = -1;
						}
					}
				}
				if (j + k < boardSize && changeNum[5] == 0) { // 下
					if (board[i][j + k] == 0) { // 石が置かれていないとき
						changeNum[5] = -1;
					} else if (board[i][j + k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[5] = -1;
						} else {
							place = true;
							changeNum[5] = k - 1;
						}
					} else if (board[i][j + k] == diskR) { // 相手の石と同じ色のとき
						if (j + k == boardSize) {
							changeNum[5] = -1;
						}
					}
				}
				if (i - k >= 0 && j + k < boardSize && changeNum[6] == 0) { // 左下
					if (board[i - k][j + k] == 0) { // 石が置かれていないとき
						changeNum[6] = -1;
					} else if (board[i - k][j + k] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[6] = -1;
						} else {
							place = true;
							changeNum[6] = k - 1;
						}
					} else if (board[i - k][j + k] == diskR) { // 相手の石と同じ色のとき
						if (i - k == 0 || j + k == boardSize) {
							changeNum[6] = -1;
						}
					}
				}
				if (i - k >= 0 && changeNum[7] == 0) { // 左
					if (board[i - k][j] == 0) { // 石が置かれていないとき
						changeNum[7] = -1;
					} else if (board[i - k][j] == disk) { // 自分の石と同じ色のとき
						if (k == 1) {
							changeNum[7] = -1;
						} else {
							place = true;
							changeNum[7] = k - 1;
						}
					} else if (board[i - k][j] == diskR) { // 相手の石と同じ色のとき
						if (i - k == 0) {
							changeNum[7] = -1;
						}
					}
				}

			}

		return place;
	}

	/**
	 * 石を置けるかどうか判定
	 *
	 * @param turn
	 *                if(turn==true) 黒が置けるかを判定 if(turn==false) 白が置けるかを判定
	 * @return place if(place==true) 石が置ける if(place==false) 石が置けない
	 *
	 */
	public boolean enablePlace(boolean turn) {
		boolean place = false;
		for (int n : checkPlace) {
			int i = n % boardSize;
			int j = n / boardSize;
			place |= enablePlace(i, j, turn);
		}
		return place;
	}

	/**
	 * 石を反転させる
	 *
	 * @param i
	 * @param j
	 */
	public void reverseDisks(int i, int j) {
		int myDisk = turn ? 1 : 2;

		for (int k = 0; k < boardSize; k++) {
			if (k <= changeNum[0]) { // 左上
				board[i - k][j - k] = myDisk;
			}
			if (k <= changeNum[1]) { // 上
				board[i][j - k] = myDisk;
			}
			if (k <= changeNum[2]) { // 右上
				board[i + k][j - k] = myDisk;
			}
			if (k <= changeNum[3]) { // 右
				board[i + k][j] = myDisk;
			}
			if (k <= changeNum[4]) { // 右下
				board[i + k][j + k] = myDisk;
			}
			if (k <= changeNum[5]) { // 下
				board[i][j + k] = myDisk;
			}
			if (k <= changeNum[6]) { // 左下
				board[i - k][j + k] = myDisk;
			}
			if (k <= changeNum[7]) { // 左
				board[i - k][j] = myDisk;
			}
		}
	}

	/**
	 * 両者の石の数を数える
	 */
	public void countDisk() {
		diskCounts[0] = 0;
		diskCounts[1] = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] == 1)
					diskCounts[0]++;
				else if (board[i][j] == 2)
					diskCounts[1]++;
			}
		}
	}

	/**
	 * 勝敗を示すテキストを取得する
	 */
	public String getWinner() {
		String winner = "";
		countDisk();
		if (diskCounts[0] > diskCounts[1]) {
			winner = "黒の勝ちです";
		} else if (diskCounts[0] < diskCounts[1]) {
			winner = "白の勝ちです";
		} else {
			winner = "引き分けです";
		}
		return winner;
	}
}
