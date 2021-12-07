package com.customized_delivery_ready.server.model.custom_table_header.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "custom_table_header")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTableHeaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "custom_col_name")
    private String customColName;

    @Column(name = "ref_form_id")
    private UUID refFormId;

    public static CustomTableHeaderEntity toEntity(CustomTableHeaderGetDto dto) {
        CustomTableHeaderEntity entity = CustomTableHeaderEntity.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .customColName(dto.getCustomColName())
            .refFormId(dto.getRefFormId())
            .build();

        return entity;
    }
}
