package jpabook3.jpashop3.service;

import jpabook3.jpashop3.domain.Items.Book;
import jpabook3.jpashop3.domain.Items.Item;
import jpabook3.jpashop3.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item){
        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long id, Book param){
        Item findItem = itemRepository.findOne(id);
        findItem.setPrice(param.getPrice());
        findItem.setStockQuantity(param.getStockQuantity());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }
}
