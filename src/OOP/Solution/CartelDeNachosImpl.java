package OOP.Solution;

import OOP.Provided.CartelDeNachos;
import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.util.*;
import java.util.stream.Collectors;

public class CartelDeNachosImpl implements CartelDeNachos {
    private final HashMap<Integer, Profesor> profesors;
    private final HashMap<Integer, CasaDeBurrito> casas;

    public CartelDeNachosImpl() {
        profesors = new HashMap<>();
        casas = new HashMap<>();
    }

    @Override
    public Profesor joinCartel(int id, String name) throws Profesor.ProfesorAlreadyInSystemException {
        if (profesors.containsKey(id)) {
            throw new Profesor.ProfesorAlreadyInSystemException();
        }
        Profesor new_pro = new ProfesorImpl(id, name);
        profesors.put(id, new_pro);
        return new_pro;
    }

    @Override
    public CasaDeBurrito addCasaDeBurrito(int id, String name, int dist, Set<String> menu) throws CasaDeBurrito.CasaDeBurritoAlreadyInSystemException {
        if (casas.containsKey(id)) {
            throw new CasaDeBurrito.CasaDeBurritoAlreadyInSystemException();
        }
        CasaDeBurrito new_casa = new CasaDeBurritoImpl(id, name, dist, menu);
        casas.put(id, new_casa);
        return new_casa;
    }

    @Override
    public Collection<Profesor> registeredProfesores() {
        return new HashSet<Profesor>(profesors.values());
    }

    @Override
    public Collection<CasaDeBurrito> registeredCasasDeBurrito() {
        return new HashSet<CasaDeBurrito>(casas.values());
    }

    @Override
    public Profesor getProfesor(int id) throws Profesor.ProfesorNotInSystemException {
        if (profesors.containsKey(id)) {
            return profesors.get(id);
        } else {
            throw new Profesor.ProfesorNotInSystemException();
        }
    }

    @Override
    public CasaDeBurrito getCasaDeBurrito(int id) throws CasaDeBurrito.CasaDeBurritoNotInSystemException {
        if (casas.containsKey(id)) {
            return casas.get(id);
        } else {
            throw new CasaDeBurrito.CasaDeBurritoNotInSystemException();
        }
    }

    @Override
    public CartelDeNachos addConnection(Profesor p1, Profesor p2) throws Profesor.ProfesorNotInSystemException, Profesor.ConnectionAlreadyExistsException, Profesor.SameProfesorException {
        if (p1.equals(p2)) {
            throw new Profesor.SameProfesorException();
        }
        if (!profesors.containsKey(p1.getId()) || !profesors.containsKey(p2.getId())){
            throw new Profesor.ProfesorNotInSystemException();
        }
        p1.addFriend(p2);
        p2.addFriend(p1);
        return this;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(Profesor p) throws Profesor.ProfesorNotInSystemException {
        if (!profesors.containsKey(p.getId())) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        Set<CasaDeBurrito> res = new HashSet<>();
        Set<Profesor> sorted_friends = new HashSet<>();
        sorted_friends = p.getFriends().stream().sorted(Comparator.comparingInt(Profesor::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
        for (Profesor pro : sorted_friends) {
            res.addAll(pro.favoritesByRating(Integer.MIN_VALUE));
        }
        return res;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(Profesor p) throws Profesor.ProfesorNotInSystemException {
        if (!profesors.containsKey(p.getId())) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        Set<CasaDeBurrito> res = new HashSet<>();
        Set<Profesor> sorted_friends = new HashSet<>();
        sorted_friends = p.getFriends().stream().sorted(Comparator.comparingInt(Profesor::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
        for (Profesor pro : sorted_friends) {
            res.addAll(pro.favoritesByDist(Integer.MAX_VALUE));
        }
        return res;
    }

    @Override
    public boolean getRecommendation(Profesor p, CasaDeBurrito c, int t) throws Profesor.ProfesorNotInSystemException, CasaDeBurrito.CasaDeBurritoNotInSystemException, ImpossibleConnectionException {
        if (!profesors.containsKey(p.getId())) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        if (!casas.containsKey(c.getId())) {
            throw new CasaDeBurrito.CasaDeBurritoNotInSystemException();
        }
        if (t < 0) {
            throw new ImpossibleConnectionException();
        }
        HashMap<Profesor, Integer> d = BFS(createGraph(), p);
        for (Profesor prof : profesors.values()) {
            if (c.isRatedBy(prof) && d.get(prof) <= t) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> getMostPopularRestaurantsIds() {
        HashMap<Integer, Integer> casas_rating = new HashMap<>();
        for (Integer casaID : casas.keySet()) {
            casas_rating.put(casaID, 0);
        }
        for (Profesor pro : profesors.values()) {
            for (Profesor friend : pro.getFriends()) {
                for (CasaDeBurrito casa : friend.favorites()) {
                    casas_rating.put(casa.getId(), casas_rating.get(casa.getId()) + 1);
                }
            }
        }
        Integer max_rate = Collections.max(casas_rating.values());
        TreeMap<Integer, Integer> sorted
                = new TreeMap<>(casas_rating);
        List<Integer> res = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry: sorted.entrySet()){
            if (Objects.equals(entry.getValue(), max_rate)){
                res.add(entry.getKey());
            }
        }
        return res;
    }

    /**
     * Create an Indirect graph representing all connection of all professors joined the cartel
     *
     * @return - An Indirect graph represented by Map <Professor -> List<Professor>>.
     */
    public HashMap<Profesor, Set<Profesor>> createGraph() {
        HashMap<Profesor, Set<Profesor>> res = new HashMap<>();
        for (Profesor p : profesors.values()) {
            res.put(p, p.getFriends());
        }
        return res;
    }


    /**
     * Run BFS Algorithm on the graph and return it's Dist Function
     * containing for each Professor it's Shortest distance from Professor "p" on the graph.
     * @param graph -the Graph of all Proffesor
     * @param p - The professor we want to start the bfs algo on.
     * @return - D(p):Professor -> Int (The distance function of BFS).
     */
    public HashMap<Profesor, Integer> BFS(HashMap<Profesor, Set<Profesor>> graph, Profesor p) {
        HashMap<Profesor, Boolean> visited = new HashMap<>();
        HashMap<Profesor, Integer> dist = new HashMap<>();
        for (Profesor pro : profesors.values()) {
            visited.put(pro, false);
            dist.put(pro, Integer.MAX_VALUE);
        }
        LinkedList<Profesor> queue = new LinkedList<>();
        visited.put(p, true);
        dist.put(p, 0);
        queue.add(p);
        while (queue.size() != 0) {
            p = queue.poll();
            for (Profesor i : graph.get(p)) {
                if (!visited.get(i)) {
                    visited.put(i, true);
                    dist.put(i, Integer.min(dist.get(p) + 1, dist.get(i)));
                    queue.add(i);
                }
            }
        }
        return dist;
    }

    @Override
    public String toString(){
        StringBuilder profesors_string = new StringBuilder();
        StringBuilder casas_string = new StringBuilder();
        buildToString(profesors_string, profesors.keySet());
        buildToString(casas_string, casas.keySet());
        StringBuilder res = new StringBuilder(String.format(
                "Registered profesores: %s" + System.lineSeparator() +
                        "Registered casas de burrito: %s" + System.lineSeparator() +
                        "Profesores:" + System.lineSeparator()
                , profesors_string, casas_string));
        for (Integer prof: profesors.keySet().stream().sorted(Integer::compareTo).collect(Collectors.toCollection(LinkedHashSet::new))){
            ProfesorImpl profesor = (ProfesorImpl)profesors.get(prof);
            res.append(profesor.getId()).append(" -> ").append(profesor.favoritesIds()).append(".\n\r");
        }
        res.append("End profesores.");
        return new String(res);
    }

    private void buildToString(StringBuilder casas_string, Set<Integer> integers) {
        for (Integer casaID : integers.stream().sorted(Integer::compareTo).collect(Collectors.toCollection(LinkedHashSet::new))) {
            casas_string.append(casaID.toString()).append(", ");
        }
        if (casas_string.length() > 0) {
            casas_string.deleteCharAt(casas_string.lastIndexOf(" ")).deleteCharAt(casas_string.lastIndexOf(","));
            casas_string.append(".");
        }
    }


}
