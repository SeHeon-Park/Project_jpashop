package jpabook3.jpashop3.domain.Items;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("A")
@Entity
public class Album extends Item{
    private String artist;
    private String etc;
}
