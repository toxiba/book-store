package com.example.bookstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CART_ITEMS")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    private BookEntity book;

    @Column(name = "QUANTITY")
    private Integer quantity;

}
