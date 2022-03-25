package jpabook3.jpashop3.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
