package jpabook3.jpashop3.repository;

import jpabook3.jpashop3.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id){
        Member member = em.find(Member.class, id);
        return member;
    }

    public List<Member> findAll(){
        List<Member> m =  em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return m;
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :Name", Member.class)
                .setParameter("Name", name)
                .getResultList();
    }

}
