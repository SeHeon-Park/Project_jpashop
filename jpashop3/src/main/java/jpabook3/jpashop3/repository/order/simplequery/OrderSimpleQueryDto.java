package jpabook3.jpashop3.repository.order.simplequery;

import jpabook3.jpashop3.domain.Address;
import jpabook3.jpashop3.domain.Order;
import jpabook3.jpashop3.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime date;
    private OrderStatus status;
    private Address address;

    public OrderSimpleQueryDto(Long id, String name, LocalDateTime date, OrderStatus status, Address address) {
        this.orderId = id;
        this.name = name;  // lazy초기화
        this.date = date;
        this.status = status;
        this.address = address; //lazy초기화 // 1+n+n 번 쿼리가 실행됨(성능 저하..)
    }
}
