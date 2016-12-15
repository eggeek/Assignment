import java.util.*;

public class TermVector extends Vector {
  // term, {docID, tf}*, idf
  public String term;
  public double idf;

  public TermVector(String raw) {
    String[] temp = raw.split(",");
    this.term = temp[0];
    this.idf = Double.parseDouble(temp[temp.length-1]);
    for (int i=1; i+1<temp.length-1; i+=2) {
      String docID = temp[i];
      double tf = Double.parseDouble(temp[i+1]);
      v.put(docID, tf);
    }
  }

  public TermVector (int num, String term, Collection<DocVector> docVecs) {
    this.term = term;
    for (DocVector i: docVecs) {
      if (i.v.containsKey(term)) {
        v.put(i.docID, i.v.get(term));
      }
    }
    idf = Backend.calcIDF(v.size(), num);
  }

  public double getTF(String docID) {
    if (v.containsKey(docID)) return v.get(docID);
    return 0.0;
  }

  public double getIDF() { return idf; }

  @Override
  public String toString() {
    ArrayList<String> res = new ArrayList<>();
    res.add(term);
    for (String i: v.keySet())
      res.add(String.format("%s,%.3f", i, v.get(i)));
    res.add(String.format("%.3f", idf));
    return String.join(",", res);
  }
}


