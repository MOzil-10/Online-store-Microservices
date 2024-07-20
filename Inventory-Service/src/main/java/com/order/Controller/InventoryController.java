package com.order.Controller;

import com.order.Dto.InventoryDTO;
import com.order.Entity.Inventory;
import com.order.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;


    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = service.createInventory(inventoryDTO);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        Inventory inventory = service.getInventory(id);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @PatchMapping("/update-stock")
    public ResponseEntity<Void> updateStock(@RequestParam String productName, @RequestParam int quantity) {
        service.updateStock(productName, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
