package OOP.Solution;

import OOP.Provided.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represent a restaurant named "Casa de Burrito".
 * each restaurant (instance) of this class identified by id, a more specific name, distance from technion,
 * and it's menu.
 *
 * it can be rated by professors and get the avg rate.
 *
 * enjoy!
 */
public class CasaDeBurritoImpl implements CasaDeBurrito, Cloneable {
    private final int id;
    private final String name;
    private final int dist;
    private final Set<String> menu;
    public Map<Profesor, Integer> rates;


    public CasaDeBurritoImpl(int id, String name, int dist, Set<String> menu) {
        this.id = id;
        this.name = name;
        this.dist = dist;
        this.menu = menu;
        rates = new HashMap<>();
    }

    /***
     * @return the restaurant id.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /***
     *
     * @return the specific name of the restaurant
     */
    @Override
    public String getName() {
        return this.name;
    }

    /***
     *
     * @return the distance from technion.
     */
    @Override
    public int distance() {
        return this.dist;
    }

    /***
     *
     * @param p - a profesor
     * @return true if this professor already rated this restaurant.
     */
    @Override
    public boolean isRatedBy(Profesor p) {
        return rates.containsKey(p);
    }

    /***
     *
     * @param p - the profesor rating the CasaDeBurrito
     * @param r - the rating
     * @return the restaurant Object for concatenating more ops.
     * @throws RateRangeException if the rate is not in [0,5]
     */
    @Override
    public CasaDeBurrito rate(Profesor p, int r) throws RateRangeException {
        if (r < 0 || r > 5) {
            throw new RateRangeException();
        }
        rates.put(p, r);
        return this;
    }

    /***
     *
     * @return the number of rates for this restaurant.
     */
    @Override
    public int numberOfRates() {
        return rates.size();
    }

    /***
     *
     * @return the average rate of all rates so far.
     */
    @Override
    public double averageRating() {
        double res = 0;
        int num_of_rates = rates.size();
        if (num_of_rates == 0) {
            return res;
        }
        for (Integer rate : rates.values()) {
            res += rate;
        }
        res /= num_of_rates;
        return res;
    }

    /***
     *
     * @param o - other restaurant
     * @return true if their id is equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CasaDeBurritoImpl other)) {
            return false;
        }
        return name.equals(other.name);
    }

    @Override
    public String toString(){
        StringBuilder menu_string = new StringBuilder();
        for (String s : menu) {
            menu_string.append(s).append(", ");
        }
        if (menu_string.length() > 0) {
            menu_string.deleteCharAt(menu_string.lastIndexOf(" ")).deleteCharAt(menu_string.lastIndexOf(","));
            menu_string.append(".");
        }
        return String.format(
                "CasaDeBurrito: %s." + System.lineSeparator() +
                "Id: %d." + System.lineSeparator() +
                "Distance: %d." + System.lineSeparator() +
                "Menu: %s"
                , name, id, dist, menu_string);
    }

    /***
     * Used for comparing Casa by id.
     * @param o the object to be compared.
     * @return 0 - if they have identical id's.
     *         else - the Subtraction between the current Casa and the other Casa.
     */
    @Override
    public int compareTo(CasaDeBurrito o) {
        return id - o.getId();
    }

    /**
     * clone the Object deep.
     * @return - cloned object.
     */
    @Override
    public CasaDeBurritoImpl clone() {

        HashSet<String> cloned_set = new HashSet<>(menu);

        CasaDeBurritoImpl res =  new CasaDeBurritoImpl(id, name, dist, cloned_set);
        res.rates.putAll(rates);
        return res;
    }

    public CasaDeBurritoImpl copy(){
        return this.clone();
    }
}
