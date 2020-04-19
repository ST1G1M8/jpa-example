package person;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Log4j2
public class Main {

    private static final Faker faker = new Faker();

    private static Person randomPerson(){
        Person person = new Person();
        person.setName(faker.name().name());

        Date date = faker.date().birthday();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        person.setGender(faker.options().option(Person.Gender.class));

        Address address = new Address();
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setCity(faker.address().city());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());
        person.setAddress(address);

        person.setEmail(faker.internet().emailAddress());
        person.setProfession(faker.company().profession());

        return person;

    }


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
        EntityManager em = emf.createEntityManager();

        int n = 100;
        try {
            em.getTransaction().begin();
            for (int i=0;i<n;i++){
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
            em.createQuery("SELECT l FROM Person l ORDER BY l.id", Person.class).getResultList().forEach(log::info);
        } finally {
            em.close();
            emf.close();
        }

    }
}
