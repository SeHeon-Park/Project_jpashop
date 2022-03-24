package jpabook3.jpashop3.repository;

import jpabook3.jpashop3.domain.Items.Item;
import jpabook3.jpashop3.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public Long save(Item item){
        if(item.getId() == null)
            em.persist(item);
        else
            em.merge(item);
        return item.getId();
    }

    public Item findOne(Long id){
        Item item = em.find(Item.class, id);
        return item;
    }

    public List<Item> findAll(){
        List<Item> i =  em.createQuery("select i from Item i", Item.class)
                .getResultList();
        return i;
    }

}
