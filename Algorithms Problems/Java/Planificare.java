import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Planificare {
	
	static class Task {
		public static final String INPUT_FILE = "planificare.in";
		public static final String OUTPUT_FILE = "planificare.out";
		
		/** obiect returnat de getResult() 
		 *  si primit de writeOutput()
		 *  - perechea formata de pierderea
		 *    totala minima si numarul de 
		 *    concursuri **/
		class Pair {
			long a;
			long b;
			
			Pair(long a, long b) {
				this.a = a;
				this.b = b;
			}
			
			Pair() {
				this(0,0);
			}
		}
		
		/** P - numarul de probe
		 *  D - durata unui concurs
		 *  B - durata unei pauze **/
		int P,D,B;
		
		/** vectorul de probe **/
		int[] tasks;
		
		/** vector ce retine pe o pozitie i 
		 *  pierderea minima pentru 1...i probe 
		 *  (folosirea probelor de la 1 la i)**/
		long[] dp;
		
		/** vector ce retine pe o pozitie i suma 
		 *  duratelor probelor de la 1 la i**/
		int[] sum;
		
		/** vector ce retine pe pozitia i ultima
		 *  proba a concursului anterior celui 
		 *  curent **/
		int[] ant;
		
		
		/** citirea eficienta a datelor din fisierul de 
		 *  INPUT - P, D, B, tasks**/
		private void readInput() {
			try {
				
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));
				String[] split = buf.readLine().split(" ");
				P = Integer.parseInt(split[0]);
				D = Integer.parseInt(split[1]);
				B = Integer.parseInt(split[2]);
				
				tasks = new int[P + 1];
				
				for (int i = 1; i <= P; ++i) {
					tasks[i] = Integer.parseInt(buf.readLine());
				}
				
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** scrierea eficienta a datelor in fisierul de 
		 *  OUTPUT - pereche de pierderea totala minima 
		 *  si numarul de concursuri **/
		private void writeOutput(Pair result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				
				buf.write(result.a + " " +  result.b + "\n");
				
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** functia care gaseste perechea cautata
		 *  folosind tehnica programarii dinamice **/
		private Pair getResult() {
			
			// initializarea vectorilor folositi
			// pentru memorizare
			dp = new long[P + 1];
			sum = new int[P + 1];
			ant = new int[P + 1];
			
			// pentru fiecare proba
			for (int i = 1; i <= P; ++i) {
				
				// precalcularea unor sume partiale :
				// pozitia i - suma duratelor probelor 
				//             de la 1 la i
				sum[i] = sum[i - 1] + tasks[i];
				
				// vom avea nevoie de un minim pentru
				// a construi pierderea minima daca 
				// construim un concurs cu probe 
				// de la j la i
				long min = Long.MAX_VALUE;
				
				// luam toate modurile de a forma un 
				// concurs avand o secventa de probe 
				// ce se termina cu proba i
				// (i), (i-1,i) , ... ,(1,...,i)
				for (int j = i; j >= 1; --j) {
					
					// calcularea costului incadrarii probelor
					//                 i ... j intr-un concurs
					int cost = sum[i] - sum[j - 1] + (i - j) * B;
					
					// calcularea pierderii crearii unui astfel de 
					// concurs prin adaugarea pierderii realizate 
					// de acest concurs ce are probe de la j la i
					// la pierderea totala minima pana la proba j
					// (pentru i  = P nu se adauga piederea realizata
					// de acest concurs)
					long cube  = dp[j - 1] + (i == P ? 0 : 1) 
								* (D - cost) * (D - cost) * (D - cost);
						
					// daca se poate face un astfel de concurs
					// adica probele incluzand pauzele se
					// incadreaza in durata unui concurs
					// atunci se gaseste pierderea minima
					// si se pastreaza ultima proba din
					// concursul anterior pentru calculul
					// numarului de concursuri
					if (cost <= D && min > cube) {
						min = cube;
						ant[i] = j - 1;
					}
				}
				
				// pierderea minima pentru incadrarea 
				// a i probe
				dp[i] = min;
			}
			
			// calcularea numarului de concursuri
			int k  = P;
			int N = 0;
			
			// de la ultima proba p se duce la ultima
			// proba a concursului anterior si tot asa
			// crescand un contor si afland numarul de 
			// concursuri
			while (k > 0) {
				k = ant[k];
				N++;
			}
			
			return new Pair(dp[P], N);
		}
		
		/** functie care rezolva problema planificare **/
		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
