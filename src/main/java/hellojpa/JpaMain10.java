package hellojpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hellojpa.entity.Member5;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

import static hellojpa.entity.QMember5.member5;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    // JPQL
    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member5> result = em.createQuery(
                    "select m From MBR5 m where m.name like '%kim%'",
                    Member5.class
            ).getResultList();
            result.forEach(System.out::println);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // Criteria 사용 준비
    public static void test2() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member5> query = cb.createQuery(Member5.class);

            Root<Member5> m = query.from(Member5.class);

            CriteriaQuery<Member5> cq = query.select(m);

            String name = "choi";
            if (name != null)
                cq = cq.where(cb.equal(m.get("name"), "kim"));

            List<Member5> result = em.createQuery(cq).getResultList();
            result.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // QueryDsl
    public static void test3() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member5> result = queryFactory
                    .select(member5)
                    .from(member5)
                    .where(member5.name.like("kim"))
                    .orderBy(member5.id.desc())
                    .fetch();
            result.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 네이티브 SQL
    public static void test4() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member5> members = em.createNativeQuery("SELECT * FROM MBR5").getResultList();
            members.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
