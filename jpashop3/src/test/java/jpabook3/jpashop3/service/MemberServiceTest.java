package jpabook3.jpashop3.service;

import jpabook3.jpashop3.domain.Member;
import jpabook3.jpashop3.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;


    private Member park(){
        Member member = new Member();
        member.setName("박세헌");
        return member;
    }

    @Test
    public void 회원가입(){
        Member park = park();
        memberService.join(park);

        assertThat(park).isEqualTo(memberRepository.findOne(park.getId()));
    }

    @Test
    public void 중복회원예외(){
        Member park1 = park();
        Member park2 = park();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(park1);
            memberService.join(park2);
        });
    }


}