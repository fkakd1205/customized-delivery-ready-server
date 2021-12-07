package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.util.List;

import com.customized_delivery_ready.server.model.ref_form.dto.RefFormGetDto;
import com.customized_delivery_ready.server.model.ref_form.entity.RefFormEntity;
import com.customized_delivery_ready.server.model.ref_form.repository.RefFormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefFormService {
    
    @Autowired
    private RefFormRepository refFormRepository;

    public List<RefFormEntity> searchList() {
        return refFormRepository.findAll();
    }
}
