package jpabook3.jpashop3.domain.Items;

import javax.persistence.*;

import jpabook3.jpashop3.domain.Category;
import jpabook3.jpashop3.exception.NotEnoughStockException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int StockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    public void addStock(int quantity){
        this.StockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int resStock = this.StockQuantity - quantity;
        if (resStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.StockQuantity = resStock;
    }
}
