// class for tokenization

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
  ArrayList<String> rules;
  HashMap<String, String> exprs;

  public Parser() {
    rules = new ArrayList<>();
    exprs = new HashMap<>();
    readRegex("regex.txt", rules, exprs);
  }
  public Parser(String regexfile) {
    rules = new ArrayList<>();
    exprs = new HashMap<>();
    readRegex(regexfile, rules, exprs);
  }

  public static ArrayList<String> cleanUp(ArrayList<String> raw) {
    ArrayList<String> res = new ArrayList<>();
    for (String i: raw) {
      i = i.replaceAll("[\\n']", "");
      i = i.replaceAll("^\\s+|\\s+$", "");
      i = i.replaceAll("\\s", "-");
      i = i.toLowerCase();
      if (i.length() > 0) res.add(i);
    }
    return res;
  }

  public static void readRegex(String fp, ArrayList<String> rules, HashMap<String, String> exprs) {
    try {
      FileReader f = new FileReader(fp);
      Scanner in = new Scanner(f);
      while (in.hasNextLine()) {
        String desc = in.nextLine(); // comment line
        String expr = in.nextLine();
        rules.add(desc);
        exprs.put(desc, expr);
      }
      in.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<String> stemTerms(ArrayList<String> raw) {
    raw = cleanUp(raw);
    ArrayList<String> res = new ArrayList<>();
    for (String i: raw) {

      PorterStemmer s = new PorterStemmer();
      s.add(i.toCharArray(), i.length());
      s.stem();
      res.add(s.toString());
    }
    return res;
  }

  public AbstractMap.SimpleEntry<String, String> findMatchRule(String s) {
    for (String desc: rules) {
      String expr = exprs.get(desc);
      Matcher m = Pattern.compile(expr).matcher(s);
      if (m.find()) {
        return new AbstractMap.SimpleEntry<String, String>(desc, expr);
      }
    }
    return null;
  }

  public void revealMatchRule(String s) {
    AbstractMap.SimpleEntry<String, String> res = findMatchRule(s);
    if (res == null) return;
    String desc = res.getKey();
    String expr = res.getValue();
    Matcher m = Pattern.compile(expr).matcher(s);
    if (m.find()) {
      System.out.println("------------------");
      System.out.printf("Desc: %s\nRule: %s\nContent: %s\nMatch: %s\n", desc, expr, s, m.group());
      System.out.println("------------------");
    }
  }

  public static void writeReport(String fn, ArrayList<String> terms, String dir) {
    try {
      System.out.printf("Parser: reporting, fn:%s, dir:%s\n", fn, dir);
      String fp = dir.replace("/$", "") + "/" + fn;
      File f = new File(fp);
      if (f.getParentFile() != null) f.getParentFile().mkdirs();
      PrintWriter p = new PrintWriter(f);
      for (String i: terms) p.println(i);
      p.close();
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  public <T extends Collection<String>> void matchStatistic(T rawTerms) {
    HashMap<String, Integer> statistic = new HashMap<>();
    HashMap<String, ArrayList<String>> report = new HashMap<>();
    for (String i: rawTerms) {
      AbstractMap.SimpleEntry<String, String> x = findMatchRule(i);
      assert(x != null);

      if (!report.containsKey(x.getKey())) report.put(x.getKey(), new ArrayList<>());
      report.get(x.getKey()).add(i);
      if (!statistic.containsKey(x.getKey())) statistic.put(x.getKey(), 1);
      else statistic.put(x.getKey(), statistic.get(x.getKey()) + 1);
    }
    for (String i: report.keySet()) {
      String fn = i.replaceAll("#+", "");
      writeReport(fn.split(":")[0].trim(), report.get(i), "report/");
    }
    int tot = 0;
    for (String key: statistic.keySet()) {
      tot += statistic.get(key);
      System.out.printf("Total terms: %d (%s)\n", statistic.get(key), key);
    }
    System.out.printf("Total term: %d\n", tot);
  }

  public ArrayList<String> regexTokenize(String content, boolean debug) {
    ArrayList<String> res = new ArrayList<>();
    ArrayList<String> reg = new ArrayList<>();
    for (String i: rules) reg.add(exprs.get(i));
    Matcher m = Pattern.compile(String.join("|", reg)).matcher(content);
    while (m.find()) {
      String s = m.group();
      if (debug) revealMatchRule(s);

      if (s.length() > 0) res.add(s);
    }
    return res;
  }

  public ArrayList<String> workflow(String content, boolean debug, boolean statistic,
      SpellChecker c) {
    System.out.println("Parser: executing regexTokenize...");
    ArrayList<String> raw = regexTokenize(content, debug);
    if (statistic) {
      System.out.println("Parser: executing matchStatistic(ArrayList)...");
      matchStatistic(raw);
      System.out.println("Parser: executing matchStatistic(Set)...");
      matchStatistic(new HashSet<String>(raw));
    }
    System.out.println("Parser: executing cleanUp...");
    raw = cleanUp(raw);
    if (c != null)
      raw = c.correct(raw);
    System.out.println("Parser: executing stemTerms...");
    return stemTerms(raw);
  }

  public ArrayList<String> workflow(String content) {
    return workflow(content, false, false, null);
  }

  public ArrayList<String> workflow(String content, SpellChecker c) {
    return workflow(content, false, false, c);
  }
}
