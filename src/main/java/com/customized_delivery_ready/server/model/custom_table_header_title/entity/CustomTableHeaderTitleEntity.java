package com.customized_delivery_ready.server.model.custom_table_header_title.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.customized_delivery_ready.server.model.custom_table_header_title.dto.CustomTableHeaderTitleGetDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "custom_table_header_title")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTableHeaderTitleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    UUID id;

    @Column(name = "title")
    String title;

    public static CustomTableHeaderTitleEntity toEntity(CustomTableHeaderTitleGetDto dto) {
        CustomTableHeaderTitleEntity entity = CustomTableHeaderTitleEntity.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .build();

        return entity;
    }
}
