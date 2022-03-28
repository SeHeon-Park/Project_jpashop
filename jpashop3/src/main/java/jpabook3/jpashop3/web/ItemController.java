package jpabook3.jpashop3.web;


import jpabook3.jpashop3.domain.Items.Book;
import jpabook3.jpashop3.domain.Items.Item;
import jpabook3.jpashop3.form.ItemForm;
import jpabook3.jpashop3.repository.ItemRepository;
import jpabook3.jpashop3.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/items/new")
    public String createItemsForm(Model model){
        Model form = model.addAttribute("form", new ItemForm());
        return "items/createItem";
    }

    @PostMapping("/items/new")
    public String create(ItemForm form){
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        log.info(itemRepository.findOne(book.getId()).getName());

        return "redirect:/";
    }

    @GetMapping("/items")
    public String findItems(Model model){
        List<Item> all = itemService.findAll();
        model.addAttribute("items", all);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String editItemsForm(@PathVariable("itemId") Long itemId, Model model){
        Book book = (Book)itemService.findOne(itemId);
        ItemForm itemForm = new ItemForm();
        itemForm.setName(book.getName());
        itemForm.setAuthor(book.getAuthor());
        itemForm.setIsbn(book.getIsbn());
        itemForm.setStockQuantity(book.getStockQuantity());
        itemForm.setPrice(book.getPrice());

        model.addAttribute("form", itemForm);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String editItems(@PathVariable("itemId") Long itemId, ItemForm form){
        Book book = (Book)itemService.findOne(itemId);
        book.setPrice(form.getPrice());
        book.setName(form.getName());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }

}
