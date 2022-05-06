package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import java.util.Comparator;


/**
 * This class is a Comparator class used for sorting list of CasaDeBurrito
 * Mainly based on Distance from technion (Ascending order) and if the distance are equal then
 * is will compare based on the average rating (Descending order).
 * And finally if the average rating are also equal then it will compare
 * based on their id (Ascending order).
 */
public class CompareByDistance implements Comparator<CasaDeBurrito> {
    @Override
    public int compare(CasaDeBurrito c1, CasaDeBurrito c2) {
        assert c1 instanceof CasaDeBurritoImpl;
        assert c2 instanceof CasaDeBurritoImpl;
        CasaDeBurritoImpl casa1 = (CasaDeBurritoImpl) c1;
        CasaDeBurritoImpl casa2 = (CasaDeBurritoImpl) c2;
        if (casa1.distance() == casa2.distance()){
            if (casa1.averageRating() == casa2.averageRating()){
                return casa1.getId() - casa2.getId();
            } else {
                double res =  (casa2.averageRating() - casa1.averageRating());
                return res > 0 ? 1 : -1;
            }
        } else {
            return casa1.distance() - casa2.distance();
        }
    }
}
