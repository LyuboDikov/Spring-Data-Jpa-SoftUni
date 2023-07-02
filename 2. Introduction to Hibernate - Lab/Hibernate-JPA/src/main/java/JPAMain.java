import entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school");

        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

//        Student student = new Student("Teo");
//        Student student2 = new Student("Lyubo");
//        Student student3 = new Student("Pesho");
//        entityManager.persist(student);
//        entityManager.persist(student2);
//        entityManager.persist(student3);

        Student firstStudent = entityManager.find(Student.class, 1);
        Student secondStudent = entityManager.find(Student.class, 2);
        Student thirdStudent = entityManager.find(Student.class, 3);

        System.out.printf("Student number %d in your records is called %s.%n", firstStudent.getId(), firstStudent.getName());
        System.out.printf("Student number %d in your records is called %s.%n", secondStudent.getId(), secondStudent.getName());
        System.out.printf("Student number %d in your records is called %s.%n", thirdStudent.getId(), thirdStudent.getName());

        entityManager.remove(firstStudent);
        entityManager.remove(thirdStudent);

        System.out.printf("As per your preference, the student with id %d called %s was removed along with student with id %d - %s.%n",
                firstStudent.getId(), firstStudent.getName(), thirdStudent.getId(), thirdStudent.getName());
        System.out.printf("Currently the only student left in your records is %s. His id is %d.%n", secondStudent.getName(), secondStudent.getId());


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
