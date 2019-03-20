import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Ursi {

	static class Task {
		public static final String INPUT_FILE = "ursi.in";
		public static final String OUTPUT_FILE = "ursi.out";
		private static final int MOD = 1000000007;
		
		/** mesajul trimis de ursi **/
		String message;
		
		/** dp[n][k] - numarul de posibilatati
		 *  de a imparti secventa 1..n 
		 *  ramanand k subsiruri neterminate **/
		long[][] dp;
		
		/** citirea eficienta a datelor din fisierul de 
		 *  INPUT - message **/
		private void readInput() {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));
				message = buf.readLine();
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** scrierea eficienta a datelor in fisierul de 
		 *  OUTPUT - numarul de posibilitati de impărtire 
		 *  a mesajului în fete zambitoare **/
		private void writeOutput(int result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				buf.write(result + "\n");
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/** functia care gaseste numarul cautat
		 *  folosind tehnica programarii dinamice **/
		private int getResult() {
			
			// incepem indexarea mesajului de la 1
			message = " " + message;
			
			// calculam lungimea acestuia
			int N = message.length() - 1;
			
			// initializarea matricei 
			// folosita pentru memorizare
			dp = new long[N + 1][N + 1];
			
			// o singura posibilitate de a
			// imparti secventa 1 (primul caracter)
			// ramanand 1 subsiruri neterminate
			dp[1][1] = 1;
			
			// pentru fiecare secventa 1,..i din mesaj
			for (int i = 2; i <= N; ++i) {
				
				// pentru fiecare numar de subsiruri neterminate
				for (int j = 0; j < N; ++j) {
					
					// daca se intalneste caracterul '_'
					if (message.charAt(i) == '_') {
						// (se continua un subsir) 
						// dp[i - 1][j] posibilitati care contin j 
						// subsiruri neterminate
						// (impartirea secventei 1,...,i-1  
						// cu j subsiruri neterminate)
						// deci j posibilitati de a continua un subsir
						dp[i][j] = (j * dp[i - 1][j]) % MOD;
					
					} else { // daca se intalneste caracterul '^'
					
						// (cazuri: se deschide un subsir nou
						//          sau se inchide un subsir)
						
						// cand se mai pot deschide subsiruri
						if (j != 0) {
							// (deschiderea unui alt subsir)
							// daca se porneste un subsir de la caracterul actual
							// atunci la dp[i - 1][j - 1] posibilitati
							// (impartirea secventei 1,...,i-1  
							// cu j - 1 subsiruri neterminate)
							// se adauga un subsir nou neterminat
							
							// (inchiderea unui subsir neterminat)
							// dp[i - 1][j + 1] posibilitati care 
							// contin j + 1 subsiruri neterminate
							// (impartirea secventei 1,...,i-1  
							// cu j + 1 subsiruri neterminate)
							// deci j + 1 posibilitati de a inchide un subsir
							dp[i][j] = (dp[i - 1][j - 1] + (j + 1) * dp[i - 1][j + 1]) % MOD;
							
						} else { 
							// cand nu se mai pot deschide subsiruri
							// putem doar sa inchidem
							
							// (inchiderea unui subsir neterminat)
							// dp[i - 1][j + 1] posibilitati care 
							// contin j + 1 subsiruri neterminate
							// (impartirea secventei 1,...,i-1  
							// cu j + 1 subsiruri neterminate)
							// deci j + 1 posibilitati de a inchide un subsir
							dp[i][j] = ((j + 1) * dp[i - 1][j + 1]) % MOD;
						}
					}
				}
			}
			
			// dp[N][0] - numarul de posibilatati
			// de a imparti mesajul fara a avea
			// subsiruri neterminate 
			return (int) dp[N][0];
		}

		/** rezolvarea problemei Ursi **/
		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}

}