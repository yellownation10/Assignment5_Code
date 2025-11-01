package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based: null order returns null")
    void nullOrderReturnsNull() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);

        BarnesAndNoble store = new BarnesAndNoble(db, process);

        PurchaseSummary result = store.getPriceForCart(null);

        assertNull(result);
        verifyNoInteractions(db, process);
    }

    @Test
    @DisplayName("specification-based: single known book totals price and buys it")
    void singleBookTotalsPrice() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);

        Book book = new Book("ISBN-123", 50, 10); // price=50, stock=10
        when(db.findByISBN("ISBN-123")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("ISBN-123", 2);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertNotNull(summary);
        assertEquals(100, summary.getTotalPrice()); // 2 * 50

        verify(db, times(1)).findByISBN("ISBN-123");
        verify(process, times(1)).buyBook(book, 2);
    }

    @Test
    @DisplayName("structural-based: if stock is 0, it records it as unavailable and buys 0")
    void zeroStockIsRecordedAndProcessCalledWithZero() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);

        // book exists but has 0 in stock
        Book outOfStock = new Book("OUT", 20, 0);
        when(db.findByISBN("OUT")).thenReturn(outOfStock);

        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("OUT", 3); // user wants 3, store has 0

        PurchaseSummary summary = store.getPriceForCart(order);

        assertNotNull(summary);
        // price should stay 0
        assertEquals(0, summary.getTotalPrice());

        // it should have been marked unavailable
        assertTrue(summary.getUnavailable().containsKey(outOfStock));
        assertEquals(3, summary.getUnavailable().get(outOfStock));

        // IMPORTANT: your real code still calls process, but with quantity 0
        verify(db, times(1)).findByISBN("OUT");
        verify(process, times(1)).buyBook(outOfStock, 0);
    }

    @Test
    @DisplayName("structural-based: multiple ISBNs → DB and process called for each")
    void multipleItemsCallsDbForEach() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);

        Book b1 = new Book("A", 10, 5);
        Book b2 = new Book("B", 20, 5);

        when(db.findByISBN("A")).thenReturn(b1);
        when(db.findByISBN("B")).thenReturn(b2);

        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("A", 1);
        order.put("B", 2);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertNotNull(summary);
        assertEquals(1 * 10 + 2 * 20, summary.getTotalPrice());

        verify(db, times(1)).findByISBN("A");
        verify(db, times(1)).findByISBN("B");

        verify(process, times(1)).buyBook(b1, 1);
        verify(process, times(1)).buyBook(b2, 2);
    }
}


