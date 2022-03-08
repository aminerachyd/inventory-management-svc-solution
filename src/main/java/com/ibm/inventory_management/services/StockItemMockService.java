package com.ibm.inventory_management.services;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.ibm.inventory_management.models.StockItem;
import com.ibm.inventory_management.repository.StockItemRepository;

@Service
@Profile("mock")
public class StockItemMockService implements StockItemApi {
        private final StockItemRepository stockItemRepository;

        @Autowired
        public StockItemMockService(@Lazy StockItemRepository stockItemRepository) {
                this.stockItemRepository = stockItemRepository;
        }

        @Override
        public List<StockItem> listStockItems() {
                return stockItemRepository.findAll();
        }
}
