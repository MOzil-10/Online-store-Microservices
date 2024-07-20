package com.order.Service;

import com.order.Dto.InventoryDTO;
import com.order.Entity.Inventory;
import com.order.Exception.InventoryServiceException;
import com.order.Repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public InventoryService(InventoryRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Inventory createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setStock(inventoryDTO.getStock());

        Inventory savedInventory = repository.save(inventory);
        sendInventoryEvent("INVENTORY_UPDATED", inventoryDTO.getProductName());
        return savedInventory;
    }

    public Inventory getInventory(Long id) {
        return repository.findById(id).orElseThrow(() -> new InventoryServiceException("Inventory not found with id: " + id));
    }

    @Transactional
    public void updateStock(String productName, int quantity) {
        Optional<Inventory> inventoryOpt = repository.findByProductName(productName);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setStock(inventory.getStock() - quantity);
            repository.save(inventory);
            sendInventoryEvent("INVENTORY_UPDATED", productName);
        } else {
            throw new InventoryServiceException("Inventory not found for product: " + productName);
        }
    }


    private void sendInventoryEvent(String eventType, String productName) {
        kafkaTemplate.send("inventory_topic", String.format("%s: %s", eventType, productName));
    }
}
