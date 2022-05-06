package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;
import OOP.Solution.CompareByRating;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfesorImpl implements Profesor {
    private final int id;
    private final String name;
    private final HashSet<CasaDeBurrito> favorites;
    private final Set<Profesor> friends;

    public ProfesorImpl(int id, String name){
        this.id = id;
        this.name = name;
        favorites = new HashSet<>();
        friends = new HashSet<>();
    }
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Profesor favorite(CasaDeBurrito c) throws UnratedFavoriteCasaDeBurritoException {
        if (c.isRatedBy(this)){
            favorites.add(c.copy()); //todo changes on this object should not effect no the real restaurant.
            return this;
        } else {
            throw new UnratedFavoriteCasaDeBurritoException();
        }
    }

    @Override
    public Collection<CasaDeBurrito> favorites() {
        return new HashSet<CasaDeBurrito>(favorites);
    }

    /**
     *
     * @return - List of all Favorite Casas id sorted.
     */
    public LinkedList<Integer> favoritesIds(){
        LinkedList<Integer> res = new LinkedList<>();
        for(CasaDeBurrito casa: favorites){
            res.add(casa.getId());
        }
        res.sort(Integer::compareTo);
        return res;
    }
    @Override
    public Profesor addFriend(Profesor p) throws SameProfesorException, ConnectionAlreadyExistsException {
        if (friends.contains(p)){
            throw new ConnectionAlreadyExistsException();
        }
        if (this.compareTo(p) == 0){
            throw new SameProfesorException();
        }
        friends.add(p);
        return this;
    }

    @Override
    public Set<Profesor> getFriends() {
        return new HashSet<>(friends);
    }

    @Override
    public Set<Profesor> filteredFriends(Predicate<Profesor> p) {
        Set<Profesor> cloned_set = new HashSet<>(friends);
        return cloned_set.stream()
                .filter(p)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<CasaDeBurrito> filterAndSortFavorites(Comparator<CasaDeBurrito> comp, Predicate<CasaDeBurrito> p) {
        Set<CasaDeBurrito> cloned_set = new HashSet<>(favorites);
        return cloned_set.stream()
                .filter(p)
                .sorted(comp).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(int rLimit) {
        Comparator<CasaDeBurrito> comp = new CompareByRating();
        return filterAndSortFavorites(comp,(CasaDeBurrito c1)->c1.averageRating() >= (double)rLimit);
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(int dLimit) {
        Comparator<CasaDeBurrito> comp = new CompareByDistance();
        return filterAndSortFavorites(comp,(CasaDeBurrito c1)->c1.distance() <= dLimit);
    }
    /***
     *
     * @param o - other restaurant
     * @return true if their id is equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profesor other)) {
            return false;
        }
        return id == other.getId();
    }

    /***
     * Used for comparing Casa by id.
     * @param p the object to be compared.
     * @return 0 - if they have identical id's.
     *         else - the Subtraction between the current Casa and the other Casa.
     */
    @Override
    public int compareTo(Profesor p) {
        return id - p.getId();
    }

    @Override
    public String toString(){
        StringBuilder favorites_string = new StringBuilder();
        for (CasaDeBurrito c : favorites) {
            favorites_string.append(c.getName()).append(", ");
        }
        if (favorites_string.length() > 0) {
            favorites_string.deleteCharAt(favorites_string.lastIndexOf(" ")).deleteCharAt(favorites_string.lastIndexOf(","));
            favorites_string.append(".");
        }
        return String.format(
                "Profesor: %s." + System.lineSeparator() +
                        "Id: %d." + System.lineSeparator() +
                        "Favorites: %s"
                , name, id, favorites_string);
    }

}

