package stemming;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stemming algorithm by Preslav Nakov.
 *
 * @author Alexander Alexandrov, e-mail: sencko@mail.bg
 * Modified by Yasen Trifonov.
 */
public class Stemmer {
    public Hashtable<String, String> stemmingRules = new Hashtable<>();

    public int STEM_BOUNDARY = 1;

    public static Pattern vocals = Pattern.compile("[^аъоуеияю]*[аъоуеияю]");
    public static Pattern p = Pattern.compile("([а-я]+)\\s==>\\s([а-я]+)\\s([0-9]+)");

    public void loadAllRules() {
        stemmingRules.clear();
	    try {
            loadStemmingRules("stem_rules_context_1.txt");
            loadStemmingRules("stem_rules_context_2.txt");
            loadStemmingRules("stem_rules_context_3.txt");
        } catch (Exception e) {
            System.out.println("Failed to load the stemming rules.");
            e.printStackTrace();
        }
    }

    public void loadStemmingRules(String filename) throws Exception {
        InputStream in = getClass().getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String s = null;
        while ((s = br.readLine()) != null) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                int j = m.groupCount();
                if (j == 3) {
                    if (Integer.parseInt(m.group(3)) > STEM_BOUNDARY) {
                        //System.out.println(m.group(1).toString() + " " + m.group(2).toString());
                        stemmingRules.put(m.group(1), m.group(2));
                    }
                }
            }
        }
        in.close();
        System.out.println(String.format("Successfully loaded %d rules.", stemmingRules.size()));
    }

    public String stem(String word) {
        Matcher m = vocals.matcher(word);
        if (!m.lookingAt()) {
            return word;
        }
        for (int i = m.end() + 1; i < word.length(); i++) {
            String suffix = word.substring(i);
            if ((suffix = (String) stemmingRules.get(suffix)) != null) {
                return word.substring(0, i) + suffix;
            }
        }
        return word;
    }
}
