import java.util.*;
import java.io.*;

public class MySearchEngine {

  public static void createIndex(String[] args) {
    String collectionDir = args[1];
    String indexDir = args[2];
    String stopWordsPath = args[3];
    Backend b = new Backend(collectionDir, indexDir, stopWordsPath);
    b.init();
    b.write();
  }

  public static void searchDoc(String[] args) {
    int idx = 1;
    String indexDir = args[idx++];
    int num = Integer.parseInt(args[idx++]);
    boolean isCheck = false;
    if (args[idx].equals("-c")) {
      isCheck = true;
      idx++;
    }

    ArrayList<String> kw = new ArrayList<>();
    for (int i=idx; i<args.length; i++) kw.add(args[i]);
    Backend b = new Backend(indexDir);
    Controller c;
    if (isCheck) c = new Controller(b, "report/normal word");
    else c = new Controller(b);
    ArrayList<String> res = c.search(num, kw);
    System.out.printf("-----------------Find %d results:\n", res.size());
    for (String i: res) System.out.println(i);
  }

  public static void genStopwords(String[] args) {
    String collectionDir = args[1];
    String indexDir = args[2];
    String writePath = args[3];
    Backend b = new Backend(collectionDir, indexDir, null);
    b.init();
    b.genStopwords(writePath);
  }

  public static void statistic(String[] args) {
    String collectionDir = args[1];
    HashMap<String, String> docs = new HashMap<>();
    Backend.readAllDocs(new File(collectionDir), docs);
    ArrayList<String> docContents = new ArrayList<>();
    for (String i: docs.values()) docContents.add(i);
    new Parser().workflow(String.join("\n", docContents), false, true, null);
  }

  public static void main(String[] args) {
    if (args[0].equals("index")) {
      createIndex(args);
    } else if (args[0].equals("search")) {
      searchDoc(args);
    } else if (args[0].equals("genstop")) {
      genStopwords(args);
    } else if (args[0].equals("statistic")) {
      statistic(args);
    }
    else System.out.println("Not valid command");
  }
}
