package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import java.util.Comparator;
import java.util.Objects;


/**
 * This class is a Comparator class used for sorting list of CasaDeBurrito
 * Mainly based on average rating (Descending order) and if the rating are equal then
 * is will compare based on distance from technion (Ascending order).
 * And finally if the distance are also equal then it will compare
 * based on their id (Ascending order).
 */
public class CompareByRating implements Comparator<CasaDeBurrito> {

    @Override
    public int compare(CasaDeBurrito c1, CasaDeBurrito c2) {
        assert c1 instanceof CasaDeBurritoImpl;
        assert c2 instanceof CasaDeBurritoImpl;
        CasaDeBurritoImpl casa1 = (CasaDeBurritoImpl) c1;
        CasaDeBurritoImpl casa2 = (CasaDeBurritoImpl) c2;
        if (Objects.equals(casa1.averageRating(), casa2.averageRating())) {
            if (casa1.distance() == casa2.distance()) {
                return casa1.getId() - casa2.getId();
            } else {
                return casa1.distance() - casa2.distance();
            }
        } else {
            double res = casa2.averageRating() - casa1.averageRating();
            return res > 0 ? 1 : -1;
        }
    }


}
