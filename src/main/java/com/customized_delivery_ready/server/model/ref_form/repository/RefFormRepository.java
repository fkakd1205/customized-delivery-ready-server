package com.customized_delivery_ready.server.model.ref_form.repository;

import com.customized_delivery_ready.server.model.ref_form.entity.RefFormEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefFormRepository extends JpaRepository<RefFormEntity, Integer>{
    
}
