package com.example.bookstore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "CARTS")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
    private UserEntity user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    // Getters, setters, constructors
    public void addItem(CartItemEntity item) {
        this.getItems().add(item);
        item.setCart(this);
    }

    public void removeItem(CartItemEntity item) {
        this.getItems().remove(item);
        item.setCart(null);
    }

    public List<CartItemEntity> getItems() {
        if (Objects.isNull(this.items)) {
            this.items = new ArrayList<>();
        }

        return this.items;
    }

    public Optional<CartItemEntity> findCartItemByBookId(Integer bookId) {
        return this.getItems().stream()
                .filter(cartItem -> bookId.equals(cartItem.getBook().getId()))
                .findFirst();
    }



}
