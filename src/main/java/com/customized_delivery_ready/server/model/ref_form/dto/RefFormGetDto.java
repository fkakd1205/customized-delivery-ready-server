package com.customized_delivery_ready.server.model.ref_form.dto;

import java.util.UUID;

import com.customized_delivery_ready.server.model.ref_form.entity.RefFormEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RefFormGetDto {
    private Integer cid;
    private UUID id;
    private String originColName;
    private String cellNumber;

    public static RefFormGetDto toDto(RefFormEntity entity) {
        RefFormGetDto dto = RefFormGetDto.builder()
            .id(entity.getId())
            .originColName(entity.getOriginColName())
            .cellNumber(entity.getCellNumber())
            .build();

        return dto;
    }
}
