import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Tester {

	public static void main(String[] args) {
		int n = 2;
		TreeMap<String, Float> ngrams = new TreeMap<String, Float>();
		// TODO Auto-generated method stub

		String text = "o be, or not to be: that is the question:\r\n" + 
				"Whether ’tis nobler in the mind to suffer\r\n" + 
				"The slings and arrows of outrageous fortune,\r\n" + 
				"Or to take arms against a sea of troubles,\r\n" + 
				"And by opposing end them? To die: to sleep;\r\n" + 
				"No more; and by a sleep to say we end\r\n" + 
				"The heart-ache and the thousand natural shocks\r\n" + 
				"That flesh is heir to, ’tis a consummation\r\n" + 
				"Devoutly to be wish’d. To die, to sleep;\r\n" + 
				"To sleep: perchance to dream: ay, there’s the rub;\r\n" + 
				"For in that sleep of death what dreams may come\r\n" + 
				"When we have shuffled off this mortal coil,\r\n" + 
				"Must give us pause: there’s the respect\r\n" + 
				"That makes calamity of so long life;\r\n" + 
				"For who would bear the whips and scorns of time,\r\n" + 
				"The oppressor’s wrong, the proud man’s contumely,\r\n" + 
				"The pangs of despised love, the law’s delay,\r\n" + 
				"The insolence of office and the spurns\r\n" + 
				"That patient merit of the unworthy takes,\r\n" + 
				"When he himself might his quietus make\r\n" + 
				"With a bare bodkin? who would fardels bear,\r\n" + 
				"To grunt and sweat under a weary life,\r\n" + 
				"But that the dread of something after death,\r\n" + 
				"The undiscover’d country from whose bourn\r\n" + 
				"No traveller returns, puzzles the will\r\n" + 
				"And makes us rather bear those ills we have\r\n" + 
				"Than fly to others that we know not of?\r\n" + 
				"Thus conscience does make cowards of us all;\r\n" + 
				"And thus the native hue of resolution\r\n" + 
				"Is sicklied o’er with the pale cast of thought,\r\n" + 
				"And enterprises of great pith and moment\r\n" + 
				"With this regard their currents turn awry,\r\n" + 
				"And lose the name of action.–Soft you now!\r\n" + 
				"The fair Ophelia! Nymph, in thy orisons\r\n" + 
				"Be all my sins remember’d.";
		String[] words = text.split(" ");
		
		for (String word : words) {
			if (n < word.length()) {
				char[] chars = word.toCharArray();
				StringBuilder ngram = new StringBuilder();
				/*
				 * abcdef l:6 2 5 3 4 4 3 5 2 6 1
				 */
				// Generate ngrams for each word.
				for (int i = 0; i < (chars.length - n) + 2; i++) {
					if (i == 0) {
						ngram.append(' ');
					} else {
						ngram.append(chars[i - 1]);
					}
					for (int j = 1; j < n - 1; j++) {
						ngram.append(chars[i + j]);
					}
					if (i + n > chars.length) {
						ngram.append(' ');
					}
					// Add ngrams to tree.
					String finalNgram = ngram.toString();
					if (ngrams.containsKey(finalNgram)) {
						ngrams.put(finalNgram, ngrams.get(finalNgram) + 1);
					} else {
						ngrams.put(finalNgram, (float) 1);
					}
				}
			} else {
				continue;
			}
		}
		Set <Map.Entry<String, Float>> set = ngrams.entrySet();
		for(Map.Entry<String, Float> me : set) {
			System.out.println(me.getKey() + " : " + me.getValue());
		}
	}
}
