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
@DiscriminatorValue("M")
@Entity
public class Movie extends Item{
    private String director;
    private String actor;
}
