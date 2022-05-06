package OOP.Tests;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.CartelDeNachos;
import OOP.Provided.CartelDeNachos.ImpossibleConnectionException;
import OOP.Provided.Profesor;
import OOP.Provided.Profesor.*;
import OOP.Provided.CasaDeBurrito.*;
import OOP.Solution.CartelDeNachosImpl;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;


public class Example {
    @Test
    public void ExampleTest() {
        CartelDeNachos network = new CartelDeNachosImpl();
        Profesor s1 = null, s2 = null, s3 = null, s4 = null, s5 = null;
        try {
            s1 = network.joinCartel(100, "Anne");
            s2 = network.joinCartel(300, "Ben");
            s3 = network.joinCartel(400, "Michael");
            s4 = network.joinCartel(500, "Tair");
            s5 = network.joinCartel(600, "Aviv");
        } catch (ProfesorAlreadyInSystemException e) {
            fail();
        }

        Set<String> menu1 = new HashSet<>(), menu2 = new HashSet<>();
        menu1.add("Hamburger");
        menu1.add("Fries");
        menu2.add("Steak");
        menu2.add("Fries");
        menu2.add("Orange Juice");
        CasaDeBurrito r1 = null, r2 = null, r3 = null, r4 = null, r5 = null, r6 = null;
        try {
            r1 = network.addCasaDeBurrito(10, "BBB", 12, menu1);
            r2 = network.addCasaDeBurrito(12, "Bob's place", 1, menu2);
            r3 = network.addCasaDeBurrito(14, "Ben's hut", 1, menu1);
            r4 = network.addCasaDeBurrito(15, "rene", 3, menu1);
            r5 = network.addCasaDeBurrito(16, "simcha", 23, menu1);
            r6 = network.addCasaDeBurrito(17, "ruben", 1, menu1);
        } catch (CasaDeBurrito.CasaDeBurritoAlreadyInSystemException e) {
            fail();
        }

        try {
            r1.rate(s1, 4)
                    .rate(s2, 5)
                            .rate(s3,4).rate(s4,1);


            r2.rate(s1, 2)
                    .rate(s1, 3)
                    .rate(s2, 4).rate(s5,2);
            r3.rate(s2, 4).rate(s5,1);
            r4.rate(s1,2).rate(s5,4);
            r5.rate(s1,2).rate(s4, 2).rate(s2,3);
        } catch (RateRangeException e) {
            fail();
        }

        assertEquals(4, r1.numberOfRates(), 0);
        assertEquals(3.0, r2.averageRating(), 0);
        assertEquals(0, r6.averageRating(),0);

        try {
            s1.favorite(r1)
                    .favorite(r2).favorite(r4);
            s2.favorite(r2)
                    .favorite(r3).favorite(r1).favorite(r5);
        } catch (UnratedFavoriteCasaDeBurritoException e) {
            System.out.println(e.toString() + " As expected.");
        }

        try {
            CasaDeBurrito r1Favorites = network.getCasaDeBurrito(10);
            CasaDeBurrito r2Favorites = network.getCasaDeBurrito(12);
            Predicate<CasaDeBurrito> p1 = r -> Objects.equals(r, r1Favorites);
            Predicate<CasaDeBurrito> p2 = r -> Objects.equals(r, r2Favorites);
            Collection<CasaDeBurrito> s1Favorites = s1.favorites();
            assertEquals(3, s1Favorites.size());
            assertTrue(s1Favorites.stream().anyMatch(p1) && s1Favorites.stream().anyMatch(p2));
            Collection<CasaDeBurrito> s2Favorites = s2.favorites();
            //assertTrue(s2Favorites.stream().noneMatch(p1) && s2Favorites.stream().anyMatch(p2));

        } catch (CasaDeBurrito.CasaDeBurritoNotInSystemException e) {
            fail();
        }

        Iterator<CasaDeBurrito> s1RateIterator = s1.favoritesByRating(1).iterator();
        double avrg_r1 = r1.averageRating();
        double avrg_r2 = r2.averageRating();
        double avrg_r3 = r3.averageRating();
        double avrg_r4 = r4.averageRating();
        Iterator<CasaDeBurrito> s2DistIterator = s2.favoritesByDist(20).iterator();

        assertEquals(s1RateIterator.next(), r1);
        assertEquals(s1RateIterator.next(), r2);
        assertEquals(s2DistIterator.next(), r2);
        assertEquals(s2DistIterator.next(), r3);

        try {
            network.addConnection(s1, s2);
            network.addConnection(s1,s5);
            network.addConnection(s2, s3);
            network.addConnection(s5, s4);
        } catch (ConnectionAlreadyExistsException | ProfesorNotInSystemException | SameProfesorException e) {
            fail();
        }

        try {
            assertFalse(network.getRecommendation(s1, r6, 1));
            assertTrue(network.getRecommendation(s1, r1, 0));
            assertFalse(network.getRecommendation(s1, r3, 0));
            List<Integer> res = network.getMostPopularRestaurantsIds();
            assertFalse(network.getRecommendation(s1, r3, 0));
            System.out.println(r1.toString());
            System.out.println(network.toString());
        } catch (ProfesorNotInSystemException | CasaDeBurritoNotInSystemException | ImpossibleConnectionException e) {
            fail();
        }
    }

    @Test
    public void Exapmle2(){

    }
}