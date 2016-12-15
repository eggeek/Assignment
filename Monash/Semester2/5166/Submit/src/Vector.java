import java.util.*;
import java.lang.Math;

public class Vector {
  public HashMap<String, Double> v;
  public Vector() { v = new HashMap<>(); }
  public double len() {
    double res = 0.0;
    for (double i: v.values()) res += i * i;
    return Math.sqrt(res);
  }

  public static double smilarity(Vector a, Vector b) {
    double x = 0.0;
    for (String ka: a.v.keySet()) {
      if (b.v.containsKey(ka)) x += a.v.get(ka) * b.v.get(ka);
    }
    double y = a.len() * b.len();
    return x / (y + 0.1);
  }
}
