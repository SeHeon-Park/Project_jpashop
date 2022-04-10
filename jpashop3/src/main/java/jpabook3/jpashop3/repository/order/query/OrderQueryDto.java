package jpabook3.jpashop3.repository.order.query;

import jpabook3.jpashop3.domain.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderQueryDto {
    private Long orderId;
    private String memberName;
    private LocalDateTime date;
    private OrderStatus status;
    private Address address;
    private List<OrderItemsDto> orderItems = new ArrayList<>();

    public OrderQueryDto(Long orderId, String memberName, LocalDateTime date, OrderStatus status, Address address) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.date = date;
        this.status = status;
        this.address = address;
    }
}
