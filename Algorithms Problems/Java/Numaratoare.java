import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Numaratoare {

	static class Task {
		public static final String INPUT_FILE = "numaratoare.in";
		public static final String OUTPUT_FILE = "numaratoare.out";

		int s,n,index;

		/** citirea eficienta a datelor din fisierul de
		 *  INPUT - s,n,index **/
		private void readInput() {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(INPUT_FILE));
				s = Integer.parseInt(buf.readLine());
				n = Integer.parseInt(buf.readLine());
				index = Integer.parseInt(buf.readLine());
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/** scrierea eficienta a datelor in fisierul de
		 *  OUTPUT - sirul cerut **/
		private void writeOutput(String result) {
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(OUTPUT_FILE));
				buf.write(result);
				buf.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private String getResult() {
			String result = "";
			return result;
		}

		/** rezolvarea problemei Numaratoare **/
		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}

}
