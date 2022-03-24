package jpabook3.jpashop3.service;

import jpabook3.jpashop3.domain.Address;
import jpabook3.jpashop3.domain.Items.Item;
import jpabook3.jpashop3.domain.Member;
import jpabook3.jpashop3.domain.Order;
import jpabook3.jpashop3.domain.OrderStatus;
import jpabook3.jpashop3.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 주문(){
        Member member = new Member();
        member.setName("박세헌");
        member.setAddress(new Address("서울", "바오", "광교"));
        em.persist(member);

        Item item = new Item();
        item.setName("비틀즈");
        item.setPrice(1000);
        item.setStockQuantity(10);
        em.persist(item);

        Long id = orderService.order(member.getId(), item.getId(), 2);

        Order getOrder = orderRepository.findOne(id);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getOrderStatus());
        Assertions.assertEquals(1, getOrder.getOrderItems().size());
        Assertions.assertEquals(1000*2, getOrder.getTotalPrice());
        Assertions.assertEquals(8, item.getStockQuantity());

    }
}