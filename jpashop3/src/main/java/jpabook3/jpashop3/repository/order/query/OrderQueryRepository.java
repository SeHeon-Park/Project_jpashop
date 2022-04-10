package jpabook3.jpashop3.repository.order.query;

import jpabook3.jpashop3.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderDtos() {  // 쿼리 3번(n+1)만에 해결
        List<OrderQueryDto> orderQueryDto = getOrderQueryDto();
        orderQueryDto.forEach(o -> {
            List<OrderItemsDto> orderItemsDto = getOrderItemsDto(o.getOrderId());
            o.setOrderItems(orderItemsDto);
        });
        return orderQueryDto;

    }

    public List<OrderQueryDto> findAllByDto_optimization() {  // 쿼리 2번만에 해결
        List<OrderQueryDto> result = getOrderQueryDto();

        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemsDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderItems())));
        return result;
    }

    public List<OrderFlatDto> findAllByDto_flat() {  // 쿼리 1번만에 해결(데이터 많으면 x, (order기준)paging x)
        return em.createQuery(
                        "select new jpabook3.jpashop3.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }

    private List<OrderQueryDto> getOrderQueryDto() {
        return em.createQuery(
                "select new jpabook3.jpashop3.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

    private List<OrderItemsDto> getOrderItemsDto(Long orderId){
        return em.createQuery(
                        "select new jpabook3.jpashop3.repository.order.query.OrderItemsDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderId", OrderItemsDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


    private Map<Long, List<OrderItemsDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemsDto> orderItems = em.createQuery(
                        "select new jpabook3.jpashop3.repository.order.query.OrderItemsDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemsDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        Map<Long, List<OrderItemsDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemsDto -> orderItemsDto.getOrderId()));
        // map으로 바꾸기
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        return orderIds;
    }
}