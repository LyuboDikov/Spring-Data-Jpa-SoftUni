import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {


        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("softuni");

        EntityManager entityManager = factory.createEntityManager();

        Engine engine = new Engine(entityManager);
        engine.run();

        factory.close();
    }
}
