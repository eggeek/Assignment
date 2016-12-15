import java.io.*;
import java.util.*;

public class Test {

  public static void testIndex() {
    Backend x = new Backend("test/collection", "test/", "test/stopwords.txt");
    x.init();
    x.write();
  }

  public static void testParser(String fn) {
    ArrayList<String> content = new ArrayList<String>();
    System.out.println("--------------------------------");
    try {
      System.out.println("read file: " + fn);
      FileReader f = new FileReader(fn);
      Scanner in = new Scanner(f);
      while (in.hasNextLine()) content.add(in.nextLine());
      in.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    ArrayList<String> tokens = new Parser().regexTokenize(String.join("\n", content), true);
    System.out.println(">>>>>>>>>>>>>>>>>>>Tokenize content:");
    for (String i: tokens) System.out.println(i);
    System.out.println("--------------------------------");
  }

  public static void testSearch(ArrayList<String> args) {
    Backend x = new Backend("test/collection", "test/", "test/stopwords.txt");
    x.init();
    Controller c = new Controller(x);
    System.out.printf("key words: %s\n", String.join(" ", args));
    ArrayList<String> res = c.search(3, args);
    for (String i: res) System.out.println(i);
  }

  public static void testBackendRead(String fp) {
    Backend b = new Backend(fp);
    b.stopWordsPath = "stopwords.txt";
    b.collectionDir = "test/collection";
    b.write();
  }

  public static void testGenStopwords() {
    Backend x = new Backend("test/collection", "test/", "test/stopwords.txt");
    x.init();
    x.write();
    x.genStopwords("stopwords.txt");
  }

  public static void testTermStatistic() {
    HashMap<String, String> docs = new HashMap<>();
    Backend.readAllDocs(new File("test/collection"), docs);
    ArrayList<String> docContents = new ArrayList<>();
    for (String i: docs.values()) docContents.add(i);
    new Parser().workflow(String.join("\n", docContents), false, true, null);
  }

  public static void testSpellChecker(String w, boolean isClassic) {
    long start = System.currentTimeMillis();
    SpellChecker s;
    if (isClassic) {
      s = new SpellChecker("report/normal word") {
        public double cost(int pos, int len) { return 1.0; }
      };
    } else {
      s = new SpellChecker("report/normal word");
    }
    System.out.printf("Search %s\n", w);
    for (AbstractMap.SimpleEntry<String, Double> i: s.search(w, 10)) {
      System.out.printf("Find, %s, %f\n", i.getKey(), i.getValue());
    }
    long end = System.currentTimeMillis();
    System.out.printf("Execute time: %fs\n", (double)(end - start) / 1000.0);
  }

  public static void testSearchWithChecker(ArrayList<String> kw) {
    System.out.println("Original kw:");
    for (String i: kw) System.out.printf("%s,", i);
    System.out.println();
    Backend b = new Backend("myindex");
    Controller c = new Controller(b, "report/normal word");
    for (String i: c.search(10, kw)) System.out.println(i);
  }

  public static void testMysearchEngine(String[] args) {
    MySearchEngine.main(args);
  }

  public static void main(String[] args) {
    for (int i=0; i<args.length; i++) {
      if (args[i].equals("all") || args[i].equals("parser")) {
        testParser("test/rule1.txt");
        testParser("test/rule2.txt");
        testParser("test/rule3.txt");
        testParser("test/rule4.txt");
        testParser("test/rule5.txt");
        testParser("test/rule_address.txt");
        testParser("test/rule_punctuation.txt");
      }
      if (args[i].equals("all") || args[i].equals("statistic")) {
        testTermStatistic();
      }
      if (args[i].equals("all") || args[i].equals("backend")) {
        testIndex();
        testBackendRead("test/");
        testGenStopwords();
        testSearch(new ArrayList<String>(Arrays.asList(args)));
      }
      if (args[i].equals("all") || args[i].equals("checker")) {
        testSpellChecker("computre", false);
        testSpellChecker("computre", true);
        testSpellChecker("hckre", true);
        testSpellChecker("hckre", false);
      }
      if (args[i].equals("all") || args[i].equals("cmd")) {
        testMysearchEngine("index collection myindex stopwords.txt".split(" "));
        testMysearchEngine("search myindex 10 -c computre hckre".split(" "));
        testMysearchEngine("search myindex 10 computre hckre".split(" "));
      }
    }
  }
}
