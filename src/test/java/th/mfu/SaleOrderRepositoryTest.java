package th.mfu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// This test is already complete — read it together, then make it pass.
// It proves the relationship works: orders saved with a customer can be
// found again THROUGH that customer. The lab's ConcertControllerTest checks
// the same idea with Concert and Seat.
@DataJpaTest
public class SaleOrderRepositoryTest {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private SaleOrderRepository orderRepo;

    @Test
    public void testOrdersBelongToCustomer() {
        Customer customer = new Customer();
        customer.setName("Alice");
        customer.setEmail("alice@example.com");
        customerRepo.save(customer);

        SaleOrder order1 = new SaleOrder();
        order1.setNotes("first order");
        order1.setCustomer(customer);
        orderRepo.save(order1);

        SaleOrder order2 = new SaleOrder();
        order2.setNotes("second order");
        order2.setCustomer(customer);
        orderRepo.save(order2);

        List<SaleOrder> orders = orderRepo.findByCustomerId(customer.getId());

        assertEquals(2, orders.size());
        assertEquals("Alice", orders.get(0).getCustomer().getName());
    }

    @Test
    public void testNoOrdersForOtherCustomer() {
        Customer alice = new Customer();
        alice.setName("Alice");
        customerRepo.save(alice);

        Customer bob = new Customer();
        bob.setName("Bob");
        customerRepo.save(bob);

        SaleOrder order = new SaleOrder();
        order.setCustomer(alice);
        orderRepo.save(order);

        assertEquals(1, orderRepo.findByCustomerId(alice.getId()).size());
        assertEquals(0, orderRepo.findByCustomerId(bob.getId()).size());
    }
}
