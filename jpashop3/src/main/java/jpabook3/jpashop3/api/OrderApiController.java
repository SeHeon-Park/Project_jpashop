package jpabook3.jpashop3.api;

import jpabook3.jpashop3.domain.Address;
import jpabook3.jpashop3.domain.Order;
import jpabook3.jpashop3.domain.OrderItem;
import jpabook3.jpashop3.domain.OrderStatus;
import jpabook3.jpashop3.repository.OrderRepository;
import jpabook3.jpashop3.repository.OrderSearch;
import jpabook3.jpashop3.repository.order.query.OrderFlatDto;
import jpabook3.jpashop3.repository.order.query.OrderQueryDto;
import jpabook3.jpashop3.repository.order.query.OrderQueryRepository;
import jpabook3.jpashop3.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook3.jpashop3.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /** 엔티티 조회 **/
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

    @GetMapping("/api/v3.1/orders") // paging 추가
    public List<OrderDto> orderV3_1(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", defaultValue = "100") int limit
    ){
        List<Order> orders = orderRepository.findOrderWithFetch(offset, limit);
        return orders.stream().map(o->new OrderDto(o)).collect(Collectors.toList());
    }

    /** JPA에서 DTO 직접 조회 **/
    @GetMapping("/api/v4/orders")  // 컬렉션 조회
    public List<OrderQueryDto> orderV4(){
        return orderQueryRepository.findOrderDtos();
    }

    @GetMapping("/api/v5/orders")  // 컬렉션 조회 최적화
    public List<OrderQueryDto> orderV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")  // 플랫 데이터 최적화(한번에 조회하기), 노가다로 OrderQueryDto 형태로 반환 가능
    public List<OrderFlatDto> orderV6(){
        return orderQueryRepository.findAllByDto_flat();
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