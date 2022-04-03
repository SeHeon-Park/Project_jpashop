package jpabook3.jpashop3.api;

import jpabook3.jpashop3.domain.Address;
import jpabook3.jpashop3.domain.Order;
import jpabook3.jpashop3.domain.OrderItem;
import jpabook3.jpashop3.domain.OrderStatus;
import jpabook3.jpashop3.repository.OrderRepository;
import jpabook3.jpashop3.repository.OrderSearch;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1(){ // Entity로 관계 맺기
        List<Order> order = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order o : order) {
            o.getMember().getName();
            o.getDelivery().getAddress();

            List<OrderItem> orderItems = o.getOrderItems();
            orderItems.stream().forEach(or -> or.getItem().getName());
        }
        return order;
    }

    @GetMapping("/api/v2/orders") // Dto로 관계맺기
    public List<OrderDto> orderV2(){
        List<Order> order = orderRepository.findAllByCriteria(new OrderSearch());
        return order.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders") // fetch join // 코드가 같음
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findOrderWithFetch();
        return orders.stream().map(o->new OrderDto(o)).collect(Collectors.toList());
    }

    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime date;
        private OrderStatus status;
        private Address address;
        private List<OrderItemDto> orderItemDto;

        public OrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName();  // lazy초기화
            this.date = order.getOrderDate();
            this.status = order.getStatus();
            this.address = order.getDelivery().getAddress(); //lazy초기화 // 1+n+n 번 쿼리가 실행됨(성능 저하..)
            List<OrderItem> orderItems = order.getOrderItems();
            this.orderItemDto = orderItems.stream().map(o -> new OrderItemDto(o.getItem().getName(), o.getOrderPrice(), o.getCount()))
                    .collect(Collectors.toList());

        }
    }

    @Data
    static class OrderItemDto{
         private String name;
         private int price;
         private int count;

        public OrderItemDto(String name, int price, int count) {
            this.name = name;
            this.price = price;
            this.count = count;
        }
    }


}
