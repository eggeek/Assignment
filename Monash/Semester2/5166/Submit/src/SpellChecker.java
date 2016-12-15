import java.util.*;
import java.io.*;
import java.lang.Math;

public class SpellChecker {
  public HashSet<String> dict;
  public static double INF = 1e9;
  public static double EPS = 1e-4;

  public double cost(int pos, int len) {
    return Math.log((double)(len+2) / (double)(pos+1));
  }

  /**
   * return distance from wrong to correct
   */
  public double dist(String w, String c) {
    int n = c.length(), m = w.length();
    double[][] v = new double[n+1][m+1];
    for (int i=0; i<=n; i++)
      for (int j=0; j<=m; j++) v[i][j] = INF;
    v[0][0] = 0.0;
    for (int i=0; i<=n; i++) {
      for (int j=0; j<=m; j++) if (Math.abs(v[i][j] - INF) > EPS) {
        if (i < n && j < m && c.charAt(i) == w.charAt(j)) { // match without cost
          v[i+1][j+1] = Math.min(v[i+1][j+1], v[i][j]);
        }
        if (i < n && j < m && c.charAt(i) != w.charAt(j)) { // replace and match
          v[i+1][j+1] = Math.min(v[i+1][j+1], v[i][j] + cost(j, m));
        }
        // swap and match
        if (i+1 < n && j+1 < m &&
            c.charAt(i+1) == w.charAt(j) &&
            c.charAt(i) == w.charAt(j+1)) {
          v[i+2][j+2] = Math.min(v[i+2][j+2], v[i][j] + cost(j, m) / 2.0);
        }
        // delete j
        if (j+1 <= m) v[i][j+1] = Math.min(v[i][j+1], v[i][j] + cost(j, m));
        // add before j
        if (i+1 <= n) v[i+1][j] = Math.min(v[i+1][j], v[i][j] + cost(j, m));
      }
    }
    return v[n][m];
  }

  public List<AbstractMap.SimpleEntry<String, Double>> search(String w, int top) {
    ArrayList<AbstractMap.SimpleEntry<String, Double>> res = new ArrayList<>();
    for (String i: dict) {
      res.add(new AbstractMap.SimpleEntry<String, Double>(i, dist(w, i)));
    }
    Collections.sort(res, new Comparator<AbstractMap.SimpleEntry<String, Double>>() {
      public int compare(AbstractMap.SimpleEntry<String, Double> lhs,
          AbstractMap.SimpleEntry<String, Double> rhs) {
        if (lhs.getValue() - rhs.getValue() > EPS) return 1;
        else if (Math.abs(lhs.getValue() - rhs.getValue()) <= EPS) return 0;
        else return -1;
      }
    });
    if (res.size() >= top)
      return res.subList(0, top);
    else
      return res;
  }

  public ArrayList<String> correct(ArrayList<String> raw) {
    ArrayList<String> res = new ArrayList<>();
    for (String i: raw) {
      List<AbstractMap.SimpleEntry<String, Double>> list = search(i, 1);
      String c = list.get(0).getKey();
      if (list.size() > 0)
        res.add(c);
      else
        res.add(i);
      if (!c.equals(i)) {
        System.out.printf("SpellChecker: change from `%s` to `%s`, diff: %f\n",
            i, c, list.get(0).getValue());
      }
    }
    return res;
  }

  public SpellChecker() {
    dict = new HashSet<>();
  }

  public SpellChecker(String dictPath) {
    try {
      FileReader fr = new FileReader(new File(dictPath));
      Scanner in = new Scanner(fr);
      ArrayList<String> words = new ArrayList<>();
      while (in.hasNextLine()) {
        words.add(in.nextLine().toLowerCase());
      }
      in.close();
      dict = new HashSet<>(Parser.cleanUp(words));
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    SpellChecker c = new SpellChecker() {
      public double cost(int len, int pos) { return 1.0; }
    };
    double x = c.dist("hckre", "ochre");
    System.out.println(x);
  }
}
