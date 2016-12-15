import java.util.*;

public class DocVector extends Vector {
  // docID: {term, tf}*
  String docID;
  public DocVector(String docID) { this.docID = docID; }
  public DocVector(String docID, ArrayList<String> terms) {
    this.docID = docID;
    for (String i: terms) {
      if (v.containsKey(i)) {
        v.put(i, v.get(i) + 1.0);
      } else v.put(i, 1.0);
    }
  }
  public void update(String term, double tf) { v.put(term, tf); }

  public Vector normalise(HashMap<String, TermVector> invIndexs, int N) {
    Vector res = new Vector();
    for (String term: v.keySet()) {
      double tfIdf = v.get(term);
      if (invIndexs.containsKey(term)) tfIdf *= invIndexs.get(term).getIDF();
      else tfIdf *= Backend.calcIDF(0, N);
      res.v.put(term, tfIdf);
    }
    return res;
  }
}
