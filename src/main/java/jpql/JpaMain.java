package jpql;

import javax.persistence.*;
import java.util.List;

import static jpql.MemberType.ADMIN;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("팀A");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(team);

            Member member = new Member();
            member.setUsername("회원1");
            member.setTeam(team);
            member.changeTeam(team);
            em.persist(member);

            Member member1 = new Member();
            member1.setUsername("회원2");
            member1.setTeam(team);
            member1.changeTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원3");
            member2.setTeam(team);
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select t from Team t join fetch t.members";


            List<Team> result = em.createQuery(query, Team.class).getResultList();

            for (Team team1 : result) {
                System.out.println("team  = " + team1.getName() + "|members=" + team1.getMembers().size());
                for (Member member3 : team1.getMembers()) {
                    System.out.println("-> member3 = " + member3);
                }
            }

//            String query = "select function('group_concat', m.username) from Member m";


            // 기본 CASE 식
//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금'" +
//                            "     when m.age <= 60 then '경로요금'" +
//                            "     else '일반요금'" +
//                            "end " +
//                    "from Member m";

            // CASE식 (사용자 이름이 없으면 이름 없는 회원을 반환함)
//            String query = "select coalesce(m.username, '이름 없는 회원') from Member m";

            // 사용자 이름이 관리자면 null을 반환하고 나머지는 본인의 이름을 반환함
//            String query = "select nullif(m.username, '관리자') from Member m";



//            String query = "select m.username, 'HELLO', TRUE from Member m " +
//                            "where m.memberType = :userType ";
//
//            List<Object[]> result = em.createQuery(query)
//                    .setParameter("userType", MemberType.ADMIN)
//                    .getResultList();

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
