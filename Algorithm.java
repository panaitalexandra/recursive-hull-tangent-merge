import java.util.*;

public class Algorithm {
    private ArrayList<Point> allPoints;

    public Algorithm(ArrayList<Point> p) {
        this.allPoints = new ArrayList<>(p);
    }

    public void setPoints(ArrayList<Point> p) {
        this.allPoints = new ArrayList<>(p);
    }

    public Object[] solve() {
        if (allPoints.size() < 3) return allPoints.toArray();
        Collections.sort(allPoints); // Ordonare după X
        List<Point> result = divideEtImpera(allPoints);
        return result.toArray();
    }

    private List<Point> divideEtImpera(List<Point> s) {
        if (s.size() <= 5) return grahamScan(new ArrayList<>(s));

        int mid = s.size() / 2; // Împărțire după mediana absciselor
        List<Point> s1 = divideEtImpera(s.subList(0, mid));
        List<Point> s2 = divideEtImpera(s.subList(mid, s.size()));

        return mergeHulls(s1, s2);
    }

    private List<Point> mergeHulls(List<Point> ch1, List<Point> ch2) {
        int n1 = ch1.size(), n2 = ch2.size();

        // Găsim A1 (max X în ch1) și B1 (min X în ch2)
        int aIdx = 0;
        for (int i = 1; i < n1; i++) if (ch1.get(i).getX() > ch1.get(aIdx).getX()) aIdx = i;
        int bIdx = 0;
        for (int i = 1; i < n2; i++) if (ch2.get(i).getX() < ch2.get(bIdx).getX()) bIdx = i;

        // Tangenta Superioară
        int upperA = aIdx, upperB = bIdx;
        boolean done = false;
        while (!done) {
            done = true;
            // Mergem anti-orar pe CH1 până când linia e tangentă
            while (calcDet(ch2.get(upperB), ch1.get(upperA), ch1.get((upperA + n1 - 1) % n1)) >= 0) {
                upperA = (upperA + n1 - 1) % n1;
                done = false;
            }
            // Mergem orar pe CH2
            while (calcDet(ch1.get(upperA), ch2.get(upperB), ch2.get((upperB + 1) % n2)) <= 0) {
                upperB = (upperB + 1) % n2;
                done = false;
            }
        }

        // Tangenta Inferioară
        int lowerA = aIdx, lowerB = bIdx;
        done = false;
        while (!done) {
            done = true;
            // Mergem orar pe CH1
            while (calcDet(ch2.get(lowerB), ch1.get(lowerA), ch1.get((lowerA + 1) % n1)) <= 0) {
                lowerA = (lowerA + 1) % n1;
                done = false;
            }
            // Mergem anti-orar pe CH2
            while (calcDet(ch1.get(lowerA), ch2.get(lowerB), ch2.get((lowerB + n2 - 1) % n2)) >= 0) {
                lowerB = (lowerB + n2 - 1) % n2;
                done = false;
            }
        }

        List<Point> merged = new ArrayList<>();
        // Adăugăm punctele orar între tangente
        int curr = upperA;
        merged.add(ch1.get(curr));
        while (curr != lowerA) {
            curr = (curr + 1) % n1;
            merged.add(ch1.get(curr));
        }
        curr = lowerB;
        merged.add(ch2.get(curr));
        while (curr != upperB) {
            curr = (curr + 1) % n2;
            merged.add(ch2.get(curr));
        }
        return merged;
    }

    public double calcDet(Point p1, Point p2, Point p3) {
        return (double)(p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) -
                (double)(p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
    }

    private List<Point> grahamScan(ArrayList<Point> pts) {
        if (pts.size() < 3) return pts;
        Point pivot = pts.stream().min(Comparator.comparingInt(Point::getY).thenComparingInt(Point::getX)).get();
        pts.sort((p1, p2) -> {
            double d = calcDet(pivot, p1, p2);
            if (d == 0) return Double.compare(distSq(pivot, p1), distSq(pivot, p2));
            return d > 0 ? -1 : 1;
        });

        List<Point> hull = new ArrayList<>();
        for (Point p : pts) {
            while (hull.size() >= 2 && calcDet(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0)
                hull.remove(hull.size() - 1);
            hull.add(p);
        }
        // Inversăm pentru a asigura ordinea ORARĂ cerută de algoritmul tău
        Collections.reverse(hull);
        return hull;
    }

    private double distSq(Point p1, Point p2) {
        return Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2);
    }
}