package comms;

import cipher.Manager;

public class ServerTest {
	public static void main(String[] args) {
		String text = "dlgcmqkxccxmpxfoggzlcbfpoeiovryhcdipwmlolmgacvpgdgmepbzsqcmzvcuyviyrqejdsggorrvcjyrezmcmiqyjrobrdlgcqyifcmlyvpcxkgxkeszcxxforydyposddlczvmlpcwspsxkkcpswcdsrrimmgycmmxhsoxmdlcobrorqszcvilqxfyjrrmqdepqirdivd"
				.toLowerCase();
		Manager m = new Manager(text, false);
		System.out.println(m.getResult());
	}
}
