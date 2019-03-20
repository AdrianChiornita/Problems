import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Minlexbfs {

	static class Task {
		public static final String INPUT_FILE = "minlexbfs.in";
		public static final String OUTPUT_FILE = "minlexbfs.out";
		
		/** numarul de noduri si numarul de muchii 
		 *  **/
		int N,M;
		
		/** listele de adiacenta ale nodurilor grafului neorientat 
		 *  **/
		ArrayList<Integer> adj[];
		
		/** citirea informatiei din fisier 
		 *  **/
		@SuppressWarnings("unchecked")
		private void readInput() {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));
				
				String line = buf.readLine();
				int index = line.indexOf(' ');
				
				N = Integer.parseInt(line.substring(0, index));
				M =	Integer.parseInt(line.substring(index + 1));
				
				adj = new ArrayList[N + 1];
				for (int i = 1; i <= N; ++i)
					adj[i] = new ArrayList<>();
				
				for (int i = 0; i < M; ++i) {
					line = buf.readLine();
					index = line.indexOf(' ');
					int x = Integer.parseInt(line.substring(0, index)); 
					int y = Integer.parseInt(line.substring(index + 1));
					adj[x].add(y);
					adj[y].add(x);
				}
					
				
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** scrierea rezultatului in fisier 
		 *  **/
		private void writeOutput(ArrayList<Integer> result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				for (int nod : result) {
					buf.write(nod + " ");
				}
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** algoritmul care gaseste parcurgerea minima lexicografica a grafului 
		 *  **/
		private ArrayList<Integer> getResult() {
			
			// sortarea listei de adiacenta a fiecarui nod pentru ca
			// la pasul de alegere a noului nod din parcurgere (adiacent
			// celui curent) sa se aleaga minimul lexicografic
			for (int i = 1; i <= N; ++i) {
				Collections.sort(adj[i]);
			}
		
			// parcurgerea
			ArrayList<Integer> result = new ArrayList<>();
			
			// parcurgere BFS in lista sortata
			
			//coada de noduri
			LinkedList<Integer> Q = new LinkedList<>();
			
			// vectorul care verifica sa nu alegem de 2
			// ori acelasi nod
			boolean[] visited = new boolean[N + 1];
			
			
			// pornire BFS de la minimul lexicografic
			// global al nodurilor
			Q.add(1);
			visited[1] = true;
			
			while (!Q.isEmpty()) {
				int nod = Q.poll();
				
				// adaugare in vectorul de parcurgere
				result.add(nod);
				
				for (int i: adj[nod]) {
					if (!visited[i]) {
						Q.add(i);
						visited[i] = true;
					}
				}
			}
			
			// gasirea parcurgerii minime lexicografic (in latime)
			return result;
		}

		/** rezolvarea problemei MINLEX BFS 
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