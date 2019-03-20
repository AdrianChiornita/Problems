import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;


/*** M-am prins ca conteaza cu ce configuratie ajung pe o pozite
 *  si am incercat sa fac un Map care sa imi rezolve acest lucru
 *  dar am intampinat cateva probleme asa ca voi face upload la
 *  ultima varianta.
 * */
public class Rtd {

	static class Task {
		public static final String INPUT_FILE = "rtd.in";
		public static final String OUTPUT_FILE = "rtd.out";

		/** Starea cubului cand poate ajunge pe o
		 *  pozitie (x y) cu o configuratie order
		 *  adunand costul cost
		 *  **/
		class Node {
			int x;
			int y;
			int[] order;
			int cost;

			Node(int x, int y, int[] order, int cost) {
				this.x = x;
				this.y = y;
				this.order = new int[7];

				for (int i = 1; i <= 6; ++i) {
					this.order[i] = order[i];
				}

				this.cost = cost;
			}
		}

		/** dimensiunea matricei (N x M)
		 *  pozitia de start (Sx, Sy)
		 *  pozitIa de final (Fx, Fy)
		 *  numarul de casute blocate K
		 *  **/
		int N, M, Sx, Sy, Fx, Fy, K;

		/** valorile de pe cub
		 *  **/
		int[] cube = new int[7];

		/** labirintul
		 *  **/
		int[][] maze;

		/** directiile de deplasare v ^ > <
		 *  **/
		int[] dx = {1, -1, 0, 0};
		int[] dy = {0, 0, 1, -1};

		/** citirea informatiei din fisier
		 *  **/
		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
				N = sc.nextInt();
				M = sc.nextInt();
				Sx = sc.nextInt();
				Sy = sc.nextInt();
				Fx = sc.nextInt();
				Fy = sc.nextInt();
				K = sc.nextInt();

				maze = new int[N + 1][M + 1];

				for (int i = 1; i <= 6; ++i) {
					cube[i] = sc.nextInt();
				}

				for (int i = 0; i < K; ++i) {
					int x = sc.nextInt();
					int y = sc.nextInt();
					maze[x][y] = -1;
				}

				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** scrierea rezultatului in fisier
		 *  **/
		private void writeOutput(int result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				buf.write(result + " ");
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** verificarea daca o pozitie e valida
		 *  **/
		private boolean check (int x, int y) {
			return (x > 0) && (y > 0) && (x <= N) && (y <= M) && (maze[x][y] != -1);
		}

		/** gasirea permutarii dupa aplicarea
		 *  miscarii move
		 *  **/
		private int[] move (int move, int[] order) {
			int[] new_order = new int[7];
			switch (move) {
				case 0:
					new_order[1] = order[5];
					new_order[2] = order[1];
					new_order[3] = order[3];
					new_order[4] = order[4];
					new_order[5] = order[6];
					new_order[6] = order[2];
					break;
				case 1:
					new_order[1] = order[2];
					new_order[2] = order[6];
					new_order[3] = order[3];
					new_order[4] = order[4];
					new_order[5] = order[1];
					new_order[6] = order[5];
					break;
				case 2:
					new_order[1] = order[3];
					new_order[2] = order[2];
					new_order[3] = order[6];
					new_order[4] = order[1];
					new_order[5] = order[5];
					new_order[6] = order[4];
					break;
				case 3:
					new_order[1] = order[4];
					new_order[2] = order[2];
					new_order[3] = order[1];
					new_order[4] = order[6];
					new_order[5] = order[5];
					new_order[6] = order[3];
					break;
			}
			return new_order;
		}

		/** gasirea drumului minim pana
		 *  de la sursa la destinatie
		 *  folosind algoritmul
		 *  lui Dijkstra
		 *  **/
		private int getResult() {

			int[][] dp = new int[N + 1][M + 1];

			for (int i = 1; i <= N; ++i) {
				for (int j = 1; j <= M; ++j) {
					dp[i][j] = Integer.MAX_VALUE;
				}
			}

			PriorityQueue<Node> Q = new PriorityQueue<>(N * M, new Comparator<Node>() {

				@Override
				public int compare(Node node1, Node node2) {
					return node1.cost - node2.cost;
				}

			});

			int[] order = new int[7];
			for (int i = 1; i <= 6; ++i) {
				order[i] = i;
			}

			Map<Node,Integer> map = new HashMap<>();

			dp[Sx][Sy] = cube[1];
			Q.add(new Node(Sx, Sy, order, cube[1]));

			while(!Q.isEmpty()) {
				Node node = Q.poll();

				for (int k = 0; k < 4; ++k) {
					int x = node.x + dx[k];
					int y = node.y + dy[k];

					if (check(x, y)) {
						int[] new_order = move(k, node.order);

						if (node.cost + cube[new_order[1]] < dp[x][y]) {

							dp[x][y] = node.cost + cube[new_order[1]];
							Q.add(new Node(x, y, new_order,dp[x][y]));
						}
					}
				}
			}

			return dp[Fx][Fy];
		}

		/** rezolvarea problemei RTD
		 *  **/
		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
