package com.ibm.inventory_management.repository;

import com.ibm.inventory_management.models.StockItem;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends MongoRepository<StockItem, String> {

}
