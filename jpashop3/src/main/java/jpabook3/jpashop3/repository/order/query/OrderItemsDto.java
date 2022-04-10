package jpabook3.jpashop3.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemsDto {
    @JsonIgnore
    private Long orderId;
    private String name;
    private int price;
    private int count;

    public OrderItemsDto(Long orderId, String name, int price, int count) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
