package order_service.Service;

import com.order.Dto.OrderDto;
import com.order.Entity.Order;
import com.order.Event.OrderEvent;
import com.order.Exception.OrderNotFoundException;
import order_service.Repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderService(OrderRepository repository, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Order placeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setProductName(orderDto.getProductName());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());

        Order savedOrder = repository.save(order);
        sendOrderEvent(savedOrder);

        return savedOrder;
    }

    public Order getOrder(Long id) {
        return repository.findById(id).orElseThrow(()-> new OrderNotFoundException("Order not found with id: " + id ));
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrder(id);
        repository.delete(order);
    }

    public void sendOrderEvent(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setProductName(order.getProductName());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setPrice(order.getPrice());

        OrderEvent orderEvent = new OrderEvent("ORDER_PLACED", orderDto);
        kafkaTemplate.send("order_topic", orderEvent);
    }
}
