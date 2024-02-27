package jpabook.jpashop.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Book extends Item {

    private String author;

    private String isbn;
}