import java.util.*;

public class Controller {

  public Backend backend;
  public Parser parser;
  public SpellChecker checker;

  public Controller(Backend backend) {
    this.backend = backend;
    parser = new Parser();
  }

  public Controller(Backend backend, String dictPath) {
    this.backend = backend;
    parser = new Parser();
    checker = new SpellChecker(dictPath);
  }

  public ArrayList<String> genQuery(ArrayList<String> rawQuery) {
    String doc = String.join(" ", rawQuery);
    ArrayList<String> query = parser.workflow(doc, checker);
    return query;
  }

  public ArrayList<String> search(int num, ArrayList<String> rawQuery) {
    ArrayList<String> query = genQuery(rawQuery);
    List<Map.Entry<String, Double>> entries = backend.retrieval(num, query);
    ArrayList<String> res = new ArrayList<>();
    for (Map.Entry<String, Double> i: entries) {
      String row = String.format("%s, %.3f", i.getKey(), i.getValue());
      res.add(row);
    }
    return res;
  }
}
