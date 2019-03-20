import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;


public class Frati {

	static class Task {
		public static final String INPUT_FILE = "frati.in";
		public static final String OUTPUT_FILE = "frati.out";

		/** obiectul va fi folosit
		 *  pentru a retine perechi de tipul
		 *  (nr. jocuri ; nr. benzi desenate) **/
		class Pair {
			int a;
			int b;

			Pair(int a, int b) {
				this.a = a;
				this.b = b;
			}

			Pair() {
				this(0,0);
			}

			public String toString() {
				return "(" + a +  "; " + b + ")";
			}
		}

		/** numarul de concursuri **/
		int N;

		/** vectorul de perechi reprezentand numarul de
		 *  premiul fiecarui concurs **/
		Pair[] competitions;

		/** citirea eficienta a datelor din fisierul de
		 *  INPUT - N, competitions **/
		private void readInput() {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));

				N = Integer.parseInt(buf.readLine());

				competitions = new Pair[N];

				for (int i = 0; i < N; ++i) {
					String line = buf.readLine();
					int index = line.indexOf(' ');
					competitions[i] = new Pair(Integer.parseInt(line.substring(0, index)),
												Integer.parseInt(line.substring(index + 1)));
				}


				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** scrierea eficienta a datelor in fisierul de
		 *  OUTPUT - perechea formata din obiectele preferate
		 *  stranse de Jon si Sam **/
		private void writeOutput(Pair result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				buf.write(result.a + " " +  result.b + "\n");
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** functia care gaseste perechea formata
		 *  din obiectele preferate stranse de Jon
		 *  si Sam folosind metoda Greedy **/
		private Pair getResult() {
			Pair result = new Pair();

			// obiectele stranse de Sam si Jon
			Pair Sam = new Pair();
			Pair Jon = new Pair();

			// sortare descrescatoare dupa suma (fiecare trebuie sa
			// stranga cat mai multe obiecte pentru a satisface
			// optimul global (daca sumele a doua perechi sunt egale
			// se sorteaza descrescator dupa primul element din pereche)
			Arrays.sort(competitions, new Comparator<Pair>() {

				@Override
				public int compare(Pair a, Pair b) {
					if (b.a + b.b == a.a + a.b) {
						return b.a - a.a;
					}

					return (b.a + b.b) - (a.a + a.b);
				}

			});

			// indice de trecere de la o secventa la alta
			int i = 0;

			// indici stanga, dreapta pentru o secventa de
			// suma egala
			int st, dr;

			// 1 - alege Jon; 2 - alege Sam
			int turn = 1;

			// bucla pana cand se termina toate concursurile
			while (i < N) {
				// secventa de un element
				st = i;
				dr = i;

				// suma elementului din secventa
				int sum = competitions[st].a + competitions[st].b;

				// marim secventa in caz de sume egale pana cand nu se mai respecta
				// acest caz
				while (dr + 1 < N && competitions[dr + 1].a + competitions[dr + 1].b == sum) {
					dr++;
				}

				// trecem index-ul peste secventa
				i = dr + 1;

				// e timpul ca fratii sa aleaga din secventa
				// din capatul din stanga alege Jon (pentru ca
				// vrea cat mia multe jocuri), iar din dreapta
				// Sam (pentru ca vrea ca mai multe benzi decat
				// jocuri) pana la epuizarea secventei
				while (st <= dr) {
					if (turn == 1) {
						Jon.a += competitions[st].a;
						Jon.b += competitions[st].b;
						turn = 2;
						st++;

					} else {
						Sam.a += competitions[dr].a;
						Sam.b += competitions[dr].b;
						turn = 1;
						dr--;
					}
				}
			}

			result.a = Jon.a;
			result.b = Sam.b;

			// se intoarce perechea formata
			// din obiectele preferate stranse de Jon
			// si Sam
			return result;
		}

		/** rezolvarea problemei Frati **/
		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}


	public static void main(String[] args) {
		new Task().solve();
	}
}
