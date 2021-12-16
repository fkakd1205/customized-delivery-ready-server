package com.customized_delivery_ready.server.model.custom_data.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.customized_delivery_ready.server.model.custom_data.entity.CustomDeliveryReadyItemEntity;

import org.springframework.stereotype.Repository;

@Repository
public class CustomDeliveryReadyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createItem(List<CustomDeliveryReadyItemEntity> entities){
        for(CustomDeliveryReadyItemEntity entity : entities){
            entityManager.createNativeQuery("INSERT INTO custom_delivery_ready_item (id, delivery_ready_custom_item, order_number, prod_order_number, prod_name, option_info, receiver, destination)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
            .setParameter(1, entity.getId().toString())
            .setParameter(2, entity.getDeliveryReadyCustomItem().toJSONString())
            .setParameter(3, entity.getOrderNumber())
            .setParameter(4, entity.getProdOrderNumber())
            .setParameter(5, entity.getProdName())
            .setParameter(6, entity.getOptionInfo())
            .setParameter(7, entity.getReceiver())
            .setParameter(8, entity.getDestination())
            .executeUpdate();
        }
    }

    public List<String> searchListDeliveryReadyCustomItem(UUID titleId) {
        List<String> list = entityManager.createNativeQuery("SELECT delivery_ready_custom_item FROM custom_delivery_ready_item WHERE custom_table_header_title_id = ?")
                .setParameter(1, titleId.toString())
                .getResultList();
        return list;
    }
}
