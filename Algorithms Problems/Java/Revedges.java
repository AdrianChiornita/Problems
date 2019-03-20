import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Revedges {
	
	static class Task {
		public static final String INPUT_FILE = "revedges.in";
		public static final String OUTPUT_FILE = "revedges.out";
		
		/** numarul de noduri, numarul de muchii si
		 *  numarul de query-uri
		 *  **/
		int N,M,Q;
		
		/** matrice de adiacenta 
		 *  pune 0 daca e drum intre X si Y
		 *  pune 1 pe muchia de intoarcere
		 *  pentru numararea muchiilor ce 
		 *  trebuie schimbate pentru a avea 
		 *  drum de la X la Y 
		 *  pune minint acolo unde nu exista 
		 *  drum intre X si Y
		 *  **/
		int[][] adj;
		
		/** dinamica folosita pentru aplicarea
		 *  algoritmului Floyd - Warshall pe 
		 *  matricea de adiacenta construita
		 *  dp[X][Y] va aduna drumul minim de
		 *  la X la Y cu semnificatia a cate
		 *  muchii trebuie minim inversate 
		 *  ca de la X sa se ajunga la Y
		 *  (adj[i][j] este 0 daca exista muchie intre
		 *  i si j, adj[i][j] este 1 daca exista muchie
		 *  ce trebuie intoarsa intre i si j) astfel
		 *  putandu-se aplica Floyd - Warshall pentru 
		 *  gasirea raspunsului la un query (X, Y)
		 *  **/
		int[][] dp;
		
		/** fq[i], sq[i] - pereche ce reprezinta un query
		 *  **/
		int[] fq;
		int[] sq;
		
		
		/** citirea informatiei din fisier si initializarea dinamicii
		 *  **/
		private void readInput() {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));
				
				String line = buf.readLine();
				int index = line.indexOf(' ');
				
				N = Integer.parseInt(line.substring(0, index));
				
				line = line.substring(index + 1);
				index = line.indexOf(' ');
				M =	Integer.parseInt(line.substring(0, index));
				Q =	Integer.parseInt(line.substring(index + 1));
				
				adj = new int[N + 1][N + 1];
				dp = new int[N + 1][N + 1];
				
				for (int i = 1; i <= N; ++i) {
					for (int j = 1; j <= N; ++j) {
						adj[i][j] = Integer.MIN_VALUE;
					}
				}
				
				for (int i = 1; i <= M; ++i) {
					line = buf.readLine();
					index = line.indexOf(' ');
					int x = Integer.parseInt(line.substring(0, index)); 
					int y = Integer.parseInt(line.substring(index + 1));
					
					adj[x][y] = 0;
					if (adj[y][x] != 0)
						adj[y][x] = 1;
				}
				
				for (int i = 1; i <= N; i++) {
					for (int j = 1; j <= N; j++) {
						if (adj[i][j] != Integer.MIN_VALUE) {
							dp[i][j] = adj[i][j];
						} else {
							dp[i][j] = M + 1;
						}
					}
					dp[i][i] = 0;
				}
				
				fq = new int[Q + 1];
				sq = new int[Q + 1];
				
				for (int i = 1; i <= Q; ++i) {
					line = buf.readLine();
					index = line.indexOf(' ');
					fq[i] = Integer.parseInt(line.substring(0, index)); 
					sq[i] = Integer.parseInt(line.substring(index + 1));
				}
				
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** scrierea rezultatului in fisier 
		 *  **/
		private void writeOutput(int[] result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				for (int i = 1; i <= Q; ++i) {
					buf.write(result[i] + "\n");
				}
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		
		/** folosirea algoritmului  Floyd - Warshall
		 *  pentru a raspunde la orice query
		 *  si gasirea rezultatelor pentru cele 
		 *  cerute
		 *  **/
		private int[] getResult() {
			
			// vector de raspunsuri
			int[] result = new int[Q + 1];
			
			// Floyd - Warshall pentru 
			// matricea de adiacenta creata
			for (int k = 1; k <= N; ++k) {
				for (int i = 1; i <= N; ++i) {
					for (int j = 1; j <= N; ++j) {
						dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j]);
					}
				}
			}
			
			// adaugarea raspunsurilor cerute
			// dupa ce le aflam pe toate
			for (int i = 1; i <= Q; ++i) {
				if (dp[fq[i]][sq[i]] == M + 1) {
					result[i] = -1;
				} else {
					result[i] = dp[fq[i]][sq[i]];
				}
			}
			
			return result;
		}

		/** rezolvarea problemei REVEDGES
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