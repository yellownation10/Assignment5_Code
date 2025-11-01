package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Test
    @DisplayName("specification-based: addToCart delegates to ShoppingCart")
    void addToCartDelegates() {
        ShoppingCart cart = mock(ShoppingCart.class);
        PriceRule rule = mock(PriceRule.class);

        Amazon amazon = new Amazon(cart, List.of(rule));
        Item item = new Item(ItemType.OTHER, "Pen", 1, 2.0);

        amazon.addToCart(item);

        verify(cart, times(1)).add(item);
    }

    @Test
    @DisplayName("specification-based: calculate sums all rules")
    void calculateSumsAllRules() {
        ShoppingCart cart = mock(ShoppingCart.class);
        List<Item> items = List.of(new Item(ItemType.OTHER, "USB", 2, 10.0));
        when(cart.getItems()).thenReturn(items);

        PriceRule rule1 = mock(PriceRule.class);
        PriceRule rule2 = mock(PriceRule.class);
        when(rule1.priceToAggregate(items)).thenReturn(20.0);
        when(rule2.priceToAggregate(items)).thenReturn(5.0);

        Amazon amazon = new Amazon(cart, List.of(rule1, rule2));
        double total = amazon.calculate();

        assertThat(total).isEqualTo(25.0);
        verify(rule1).priceToAggregate(items);
        verify(rule2).priceToAggregate(items);
    }

    @Test
    @DisplayName("structural-based: empty rules → total 0")
    void emptyRules() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cart.getItems()).thenReturn(List.of());

        Amazon amazon = new Amazon(cart, List.of());

        assertThat(amazon.calculate()).isZero();
    }

    @Test
    @DisplayName("structural-based: empty cart still calls rule")
    void emptyCartStillCallsRule() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cart.getItems()).thenReturn(List.of());

        PriceRule rule = mock(PriceRule.class);
        when(rule.priceToAggregate(List.of())).thenReturn(0.0);

        Amazon amazon = new Amazon(cart, List.of(rule));
        double total = amazon.calculate();

        assertThat(total).isZero();
        verify(rule).priceToAggregate(List.of());
    }
}


