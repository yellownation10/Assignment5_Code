package org.example.Amazon;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.RegularCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AmazonIntegrationTest {

    private Database database;
    private ShoppingCartAdaptor cart;
    private Amazon amazon;

    @BeforeEach
    void setUp() {
        // real in-memory DB from your Database class
        database = new Database();
        database.resetDatabase();     // clean table before every test

        // real cart that talks to the DB
        cart = new ShoppingCartAdaptor(database);

        // real Amazon with all real rules
        amazon = new Amazon(
                cart,
                List.of(
                        new RegularCost(),
                        new DeliveryPrice(),
                        new ExtraCostForElectronics()
                )
        );
    }

    @Test
    @DisplayName("specification-based: add items → calculate > 0 and items are in DB")
    void addThenCalculate_endToEnd() {
        amazon.addToCart(new Item(ItemType.OTHER, "Notebook", 2, 5.0));
        amazon.addToCart(new Item(ItemType.ELECTRONIC, "Headphones", 1, 40.0));

        double total = amazon.calculate();

        assertThat(cart.getItems()).hasSize(2);
        assertThat(total).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("structural-based: all items stored in DB are processed in price calc")
    void allItemsProcessed() {
        amazon.addToCart(new Item(ItemType.ELECTRONIC, "Phone", 1, 500.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Case", 1, 20.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Charger", 1, 30.0));

        double total = amazon.calculate();

        assertThat(cart.getItems()).hasSize(3);


        assertThat(total).isGreaterThanOrEqualTo(550 + 5 + 7.5);
    }
}

