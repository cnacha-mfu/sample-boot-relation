package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SaleOrderController {

    @Autowired
    SaleOrderRepository orderRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    ProductRepository productRepo;

    // POST for creating an order UNDER a customer (note the nested URL)
    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<String> createOrder(@PathVariable Long customerId, @RequestBody SaleOrder order) {
        // TODO: find the customer by customerId; if absent -> 404 NOT FOUND
        // TODO: set the customer on the order (this is what links the two rows!)
        // TODO: save the order -> 201 CREATED
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

    // GET for getting orders by customer
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<Collection<SaleOrder>> getOrdersByCustomer(@PathVariable Long customerId) {
        // TODO: if the customer does not exist -> 404 NOT FOUND
        // TODO: return orderRepo.findByCustomerId(customerId) with 200 OK
        return new ResponseEntity<Collection<SaleOrder>>(HttpStatus.NOT_IMPLEMENTED);
    }


    // GET for getting all orders
    @GetMapping("/orders")
    public ResponseEntity<Collection<SaleOrder>> getAllOrders() {
        System.out.println("getAllorder called...");
        return new ResponseEntity<Collection<SaleOrder>>(orderRepo.findAll(), HttpStatus.OK);
    }


}
