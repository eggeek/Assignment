import java.io.*;
import java.util.*;
import java.lang.Math;

public class Backend {
  // key: docID, value: content
  public HashMap<String, TermVector> invIndexs;
  public HashMap<String, DocVector> docVecs;
  public HashSet<String> terms;
  public HashSet<String> stopWords;
  public String collectionDir;
  public String indexPath;
  public String stopWordsPath;
  public double EPS = 1e-4;

  public static double calcIDF(int df, int N) {
    return Math.log((double)(N + 0.1) / (double)(df + 0.1));
  }

  public Backend(String indexDir) {
    this.indexPath = indexDir.replaceAll("/$", "") + "/index.txt";
    try {
      FileReader fr = new FileReader(new File(indexPath));
      Scanner in = new Scanner(fr);
      invIndexs = new HashMap<>();
      docVecs = new HashMap<>();
      terms = new HashSet<>();
      while (in.hasNextLine()) {
        TermVector tv = new TermVector(in.nextLine());
        invIndexs.put(tv.term, tv);
        for (String docID: tv.v.keySet()) {
          if (!docVecs.containsKey(docID)) docVecs.put(docID, new DocVector(docID));
          docVecs.get(docID).update(tv.term, tv.v.get(docID));
        }
      }
      in.close();
      System.out.printf("Finish indexPath(%s), term num: %d, doc num: %d\n",
          indexPath, invIndexs.size(), docVecs.size());
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public Backend(String collectionDir, String indexDir, String stopWordsPath) {
    this.collectionDir = collectionDir;
    this.indexPath = indexDir.replaceAll("/$", "") + "/index.txt";
    this.stopWordsPath = stopWordsPath;
    invIndexs = new HashMap<>();
    docVecs = new HashMap<>();
    terms = new HashSet<>();
    stopWords = new HashSet<>();
  }

  public double getTF(String term, String docID) {
    if (invIndexs.containsKey(term)) {
      return invIndexs.get(term).getTF(docID);
    } else return 0.0;
  }

  public static String readFileContent(File f) {
    ArrayList<String> res = new ArrayList<>();
    try {
      Scanner in = new Scanner(new FileReader(f));
      while (in.hasNextLine()) res.add(in.nextLine());
      in.close();
      return String.join("\n", res);
    } catch(Exception e){
      e.printStackTrace();
    }
    return "";
  }

  public static void readAllDocs(File folder, HashMap<String, String> docs) {
    for (File i: folder.listFiles()) {
      if (i.isFile()) {
        docs.put(i.getName().replaceAll(",", ""), readFileContent(i));
      } else if (i.isDirectory()) {
        readAllDocs(i, docs);
      }
    }
  }

  public void parseStemmedTerm(HashMap<String, String> docs,
      HashMap<String, ArrayList<String>> rawDocTerms) {
    for (String i: docs.keySet()) {
      System.out.printf("Backend: parsing doc(%s)\n", i);
      ArrayList<String> stemmed = new Parser().workflow(docs.get(i));
      if (!rawDocTerms.containsKey(i))
        rawDocTerms.put(i, new ArrayList<>());
      for (String j: stemmed) {
        if (stopWords.contains(j)) continue;
        rawDocTerms.get(i).add(j);
        terms.add(j);
      }
    }
  }

  public void createInvIndex(HashMap<String, ArrayList<String>> rawDocTerms) {
    for (String docID: rawDocTerms.keySet()) {
      docVecs.put(docID, new DocVector(docID, rawDocTerms.get(docID)));
    }
    for (String i: terms) {
      invIndexs.put(i, new TermVector(docVecs.size(), i, docVecs.values()));
    }
  }

  public void readStopWords(File f) {
    try {
      Scanner in = new Scanner(new FileReader(f));
      while (in.hasNextLine()) {
        stopWords.add(in.nextLine());
      }
      in.close();
    } catch(FileNotFoundException e){
      try {
        f.createNewFile();
      } catch(Exception e1){
        e1.printStackTrace();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public List<Map.Entry<String, Double>> retrieval(int num, ArrayList<String> queryTerms) {
    Vector query = new DocVector("__query", queryTerms).normalise(invIndexs, docVecs.size());
    HashMap<String, Double> rating = new HashMap<>();
    for (DocVector i: docVecs.values()) {
      Vector d = i.normalise(invIndexs, docVecs.size());
      double sim = Vector.smilarity(query, d);
      if (Math.abs(sim) > EPS)
        rating.put(i.docID, sim);
    }
    ArrayList<Map.Entry<String, Double>> res = new ArrayList<>(rating.entrySet());
    Collections.sort(res, new Comparator<Map.Entry<String, Double>>() {
      public int compare(Map.Entry<String, Double> lhs, Map.Entry<String, Double> rhs) {
        if (lhs.getValue() - rhs.getValue() > EPS) return -1;
        else if (Math.abs(lhs.getValue() - rhs.getValue()) <= EPS) return 0;
        else return 1;
      }
    });
    return res.subList(0, Math.min(num, res.size()));
  }

  public void genStopwords(String path) {
    stopWords.clear();
    for (TermVector i: invIndexs.values()) {
      if (i.getIDF() <= EPS) stopWords.add(i.term);
    }
    System.out.printf("Find %d stop words, write to file: %s\n", stopWords.size(), path);
    try {
      PrintWriter pw = new PrintWriter(path);
      File f = new File(path);
      if (f.getParentFile() != null) {
        f.getParentFile().mkdirs();
      }
      for (String i: stopWords) pw.println(i);
      pw.close();
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  public void init() {
    long start = System.currentTimeMillis();
    if (collectionDir == null) {
      System.out.println("Init failed, collection directory is null.");
      return;
    }
    if (stopWordsPath == null) {
      System.out.println("Warning: stopwords directory is null.");
    }
    System.out.println("Reading file...");
    HashMap<String, String> docs = new HashMap<>();
    HashMap<String, ArrayList<String>> rawDocTerms = new HashMap<>();
    readAllDocs(new File(collectionDir), docs);
    if (stopWordsPath != null)
      readStopWords(new File(stopWordsPath));
    System.out.println("Parsing terms...");
    terms.clear();
    parseStemmedTerm(docs, rawDocTerms);
    System.out.println("Creating index...");
    createInvIndex(rawDocTerms);
    long curr = System.currentTimeMillis();
    System.out.printf("Finish, Term num: %d, doc num: %d, cost time: %.3fs\n",
        terms.size(), docVecs.size(), (double)(curr - start) / 1000.0);
  }

  public void write() {
    ArrayList<String> content = new ArrayList<>();
    ArrayList<TermVector> outs = new ArrayList<>(invIndexs.values());
    Collections.sort(outs, new Comparator<TermVector>() {
      public int compare(TermVector x, TermVector y) {
        if (x.getIDF()- y.getIDF() > EPS) return -1;
        else if (Math.abs(x.getIDF()- y.getIDF()) <= EPS) return 0;
        else return 1;
      }
    });
    for (TermVector i: outs) {
      content.add(i.toString());
    }
    try {
      File f = new File(indexPath);
      f.getParentFile().mkdirs();
      PrintWriter out = new PrintWriter(indexPath);
      for (String i: content) out.println(i);
      out.close();
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
