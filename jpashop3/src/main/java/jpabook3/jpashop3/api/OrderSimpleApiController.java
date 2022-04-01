package jpabook3.jpashop3.api;

import jpabook3.jpashop3.domain.Address;
import jpabook3.jpashop3.domain.Order;
import jpabook3.jpashop3.repository.OrderSearch;
import jpabook3.jpashop3.domain.OrderStatus;
import jpabook3.jpashop3.repository.OrderRepository;
import jpabook3.jpashop3.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook3.jpashop3.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //v1은 그냥 리스트 형태로 return(하지마..)

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        List<SimpleOrderDto> order = orderRepository.findAllByCriteria(new OrderSearch())
                .stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return order;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){
        List<Order> orders = orderRepository.findOrdersWithMemberDelivery();
        List<SimpleOrderDto> result = orders
                .stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
       return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime date;
        private OrderStatus status;
        private Address address;

        public SimpleOrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName();  // lazy초기화
            this.date = order.getOrderDate();
            this.status = order.getStatus();
            this.address = order.getDelivery().getAddress(); //lazy초기화 // 1+n+n 번 쿼리가 실행됨(성능 저하..)
        }
    }

}
