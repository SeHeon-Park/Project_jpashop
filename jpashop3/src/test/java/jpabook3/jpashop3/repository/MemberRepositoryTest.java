package jpabook3.jpashop3.repository;

import jpabook3.jpashop3.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    public void save(){
        Member member = new Member();
        member.setName("박세헌");
        Long id = memberRepository.save(member);

        Member findMember = memberRepository.findOne(id);
        Assertions.assertThat(findMember.getId()).isEqualTo(id);
    }


}