import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Disjcnt {
	
	static class Task {
		public static final String INPUT_FILE = "disjcnt.in";
		public static final String OUTPUT_FILE = "disjcnt.out";
				
		/** pereche folosita ca si muchie intre 2 noduri
		 *  **/
		public class Pair<F, S> {
		    public F first;
		    public S second;

		    public Pair(F first, S second) {
		        this.first = first;
		        this.second = second;
		    }
		}
		
		/** numarul de noduri si numarul de muchii
		 *  **/
		int N,M;
		
		/** listele de adiacenta ale nodurilor grafului neorientat 
		 *  **/
		ArrayList<Integer> adj[];
		
		/** lista tuturor puntilor (muchiilor critice)
		 *  **/
		ArrayList<Pair<Integer, Integer>> bridges;
		
		/** vectori folositi in gasirea puntilor
		 *  idx[u] -> timpul de descoperire / ordinea lui u
		 *  low[u] -> min[ {idx[u]} U {idx[v] | (u, v) este o muchie înapoi} U 
		 *                 {low[vi] | vi copil al lui u în arborele de adâncime}]
		 *  **/
		int[] idx, low;
		
		/** Numarul de perechi de noduri (X, Y) pentru care exista cel putin 
		 *  un mod de a alege 2 drumuri intre nodul X si nodul Y care sa nu 
		 *  aiba muchii comune.
		 *  **/
		long result;
		
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
		private void writeOutput(long result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				buf.write(result + " ");
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** BFS - parcurgere care va numara cate noduri fac parte dintr-o componenta conexa
		 *  dupa ce s-au eliminat toate puntile.
		 *  fie aceste noduri vizitate in numar de X -> atunci combinari de X luate cate 2 = X(X-1)/2
		 *  deci in acea componenta exita X(X-1)/2 perechi de noduri care satisfac conditia ceruta.
		 *  acest numar se adauga la raspuns si vom face acelasi algoritm pentru fiecare componenta
		 *  conexa a grafului modificat.
		 *  **/
		private void BFS(int start, boolean[] visited) {
			
			LinkedList<Integer> Q = new LinkedList<>();
			int n = 1;
			
			Q.add(start);
			visited[start] = true;
			
			while (!Q.isEmpty()) {
				int nod = Q.poll();
				
				for (int i: adj[nod]) {
					if (!visited[i]) {
						Q.add(i);
						visited[i] = true;
						n++;
					}
				}
			}
			result += n * (n - 1) / 2;
		}
		
		/** parcurgere in adancime modificata (algoritmul lui Tarjan) 
		 *  pentru descoperirea muchiilor critice/ puntilor
		 *  
		 *  dupa aplicarea acestei functii putem gasi un graf modificat 
		 *  prin eliminarea puntilor din cel initial, care se va folosi
		 *  pentru numararea perechilor de noduri (X, Y) pentru care exista 
		 *  cel putin un mod de a alege 2 drumuri intre nodul X si nodul Y 
		 *  care sa nu aiba muchii comune (vor ramene conectate doar acele 
		 *  noduri care satisfac conditia)
		 *  **/
		private void findbridges(int start, int predecesor, int h) {
			idx[start] = h;
			low[start] = h;
			
			for (int nod : adj[start]) {
				if (nod != predecesor) {
					if (idx[nod] == 0) {
						findbridges(nod, start, h + 1);
						
						low[start] = Math.min(low[start] , low[nod]);
						if (low[nod] > idx[start]) 
							bridges.add(new Pair<Integer, Integer>(start, nod));
					}else {
						low[start] = Math.min( low[start] , idx[nod]);
					}
				}
			}
		}
		
		/** gasirea rezultatului
		 *  **/
		private long getResult() {
			
			// initializarea vectorilor folositi in algoritmul lui Tarjan
			idx = new int[N + 1];
			low = new int[N + 1];
			
			// initializarea listei de punti
			bridges = new ArrayList<>();
			
			// gasirea tuturor puntilor din graf
			for (int i  = 1; i <= N; ++i) 
				if (idx[i] == 0)
					findbridges(i, 0, 1);
		
			// transformarea grafului prin eliminarea puntilor
			for (Pair<Integer, Integer> e : bridges) {
				adj[e.first].remove(adj[e.first].indexOf(e.second));
				adj[e.second].remove(adj[e.second].indexOf(e.first));
			}
			
			// vector care tine minte care noduri au fost vizitate 
			// prin BFS in graful transformat, numaram cate am vizitat
			// o singura data cu o parcurgere si folosind formula de 
			// la combinari adaugam la rezultat, apoi reluam un BFS
			// din ultimul nod nevizitat
			boolean[] visited = new boolean[N + 1];
			
			for (int i  = 1; i <= N; ++i) 
				if (!visited[i]) 
					BFS (i, visited);
			
			// rezultatul dupa vizitarea tuturor nodurilor
			return result;
		}
		
		/** rezolvarea problemei DISJCNT
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
