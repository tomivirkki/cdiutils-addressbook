package org.vaadin.virkki.cdiutils.addressbook.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class PersonDAOBean {
    private final Logger log = Logger.getLogger(getClass().getName());

    private static Set<Person> personDatabase = createPersonDatabase();

    private static Set<Person> createPersonDatabase() {
        final String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
                "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
                "Lisa", "Marge" };
        final String[] lnames = { "Smith", "Gordon", "Simpson", "Brown",
                "Clavel", "Simons", "Verne", "Scott", "Allison", "Gates",
                "Rowling", "Barks", "Ross", "Schneider", "Tate" };
        final String cities[] = { "Amsterdam", "Berlin", "Helsinki",
                "Hong Kong", "London", "Luxemburg", "New York", "Oslo",
                "Paris", "Rome", "Stockholm", "Tokyo", "Turku" };
        final String streets[] = { "4215 Blandit Av.", "452-8121 Sem Ave",
                "279-4475 Tellus Road", "4062 Libero. Av.", "7081 Pede. Ave",
                "6800 Aliquet St.", "P.O. Box 298, 9401 Mauris St.",
                "161-7279 Augue Ave", "P.O. Box 496, 1390 Sagittis. Rd.",
                "448-8295 Mi Avenue", "6419 Non Av.",
                "659-2538 Elementum Street", "2205 Quis St.",
                "252-5213 Tincidunt St.", "P.O. Box 175, 4049 Adipiscing Rd.",
                "3217 Nam Ave", "P.O. Box 859, 7661 Auctor St.",
                "2873 Nonummy Av.", "7342 Mi, Avenue",
                "539-3914 Dignissim. Rd.", "539-3675 Magna Avenue",
                "Ap #357-5640 Pharetra Avenue", "416-2983 Posuere Rd.",
                "141-1287 Adipiscing Avenue", "Ap #781-3145 Gravida St.",
                "6897 Suscipit Rd.", "8336 Purus Avenue", "2603 Bibendum. Av.",
                "2870 Vestibulum St.", "Ap #722 Aenean Avenue",
                "446-968 Augue Ave", "1141 Ultricies Street",
                "Ap #992-5769 Nunc Street", "6690 Porttitor Avenue",
                "Ap #105-1700 Risus Street",
                "P.O. Box 532, 3225 Lacus. Avenue", "736 Metus Street",
                "414-1417 Fringilla Street", "Ap #183-928 Scelerisque Road",
                "561-9262 Iaculis Avenue" };
        Set<Person> peopleCollection = new HashSet<Person>();
        Random r = new Random(0);

        for (int i = 0; i < 100; i++) {
            Person p = new Person();
            p.setFirstName(fnames[r.nextInt(fnames.length)]);
            p.setLastName(lnames[r.nextInt(lnames.length)]);
            p.setCity(cities[r.nextInt(cities.length)]);
            p.setEmail(p.getFirstName().toLowerCase() + "."
                    + p.getLastName().toLowerCase() + "@vaadin.com");
            p.setPhoneNumber("+358 02 555 " + r.nextInt(10) + r.nextInt(10)
                    + r.nextInt(10) + r.nextInt(10));
            int n = r.nextInt(100000);
            if (n < 10000) {
                n += 10000;
            }
            p.setPostalCode(n);
            p.setStreetAddress(streets[r.nextInt(streets.length)]);
            p.setId(r.nextLong());
            peopleCollection.add(p);
        }
        return peopleCollection;
    }

    public Collection<Person> listPeople() {
        log.info("Fetched a list of all Persons");
        return personDatabase;
    }

    public Collection<String> listCities() {
        final String cities[] = { "Amsterdam", "Berlin", "Helsinki",
                "Hong Kong", "London", "Luxemburg", "New York", "Oslo",
                "Paris", "Rome", "Stockholm", "Tokyo", "Turku" };
        log.info("Fetched a list of city options");
        return Arrays.asList(cities);
    }

    public void update(Person person) {
        personDatabase.remove(person);
        personDatabase.add(person);
        log.info("Person (" + person.getId() + ") was updated.");
    }

    public Person persist(Person person) {
        person.setId(new Random().nextLong());
        personDatabase.add(person);
        log.info("Person (" + person.getId() + ") was persisted.");
        return person;
    }

    public Person createNew() {
        return new Person();
    }

}