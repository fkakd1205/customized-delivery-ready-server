package com.customized_delivery_ready.server.service.excel_translator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Convert;

import com.customized_delivery_ready.server.model.excel_translator_header.dto.UploadDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorUploadHeaderDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_data.dto.ExcelTranslatorDataGetDto;
import com.customized_delivery_ready.server.model.excel_translator_data.dto.TranslatedDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_data.dto.ExcelTranslatorDataDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.DownloadDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.customized_delivery_ready.server.model.excel_translator_header.repository.ExcelTranslatorHeaderRepository;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelTranslatorHeaderService {

    @Autowired
    private ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository;

    public ExcelTranslatorHeaderGetDto createOne(ExcelTranslatorHeaderGetDto dto) {
        for(int i = 0; i < dto.getUploadHeaderDetail().getDetails().size(); i++) {
            dto.getUploadHeaderDetail().getDetails().get(i).setCellNumber(i);
        }
        ExcelTranslatorHeaderEntity entity = ExcelTranslatorHeaderEntity.toEntity(dto);
        excelTranslatorHeaderRepository.save(entity);
        return dto;
    }

    public List<ExcelTranslatorHeaderGetDto> searchList() {
        List<ExcelTranslatorHeaderEntity> entities = excelTranslatorHeaderRepository.findAll();
        List<ExcelTranslatorHeaderGetDto> dtos = entities.stream().map(r -> ExcelTranslatorHeaderGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public ExcelTranslatorHeaderGetDto updateOne(ExcelTranslatorHeaderGetDto dto) {
        Optional<ExcelTranslatorHeaderEntity> entityOpt = excelTranslatorHeaderRepository.findById(dto.getId());

        if (entityOpt.isPresent()) {
            ExcelTranslatorHeaderEntity entity = entityOpt.get();
            entity.setDownloadHeaderTitle(dto.getDownloadHeaderTitle()).setDownloadHeaderDetail(dto.getDownloadHeaderDetail());
            return dto;
        } else {
            throw new NullPointerException();
        }
    }

    public List<ExcelTranslatorDataGetDto> uploadExcelFile(MultipartFile file, ExcelTranslatorHeaderGetDto dto) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<ExcelTranslatorDataGetDto> excelDto = this.getTranslatedExcelForm(sheet, dto);
        return excelDto;
    }

    private List<ExcelTranslatorDataGetDto> getTranslatedExcelForm(Sheet worksheet, ExcelTranslatorHeaderGetDto headerDto) {
        List<DownloadDetailDto> detailDto = headerDto.getDownloadHeaderDetail().getDetails();
        List<TranslatedDetailDto> detailDtos = new ArrayList<>();
        List<ExcelTranslatorDataGetDto> dataGetDtos = new ArrayList<>();

        // 데이터 시작 위치
        Row row = worksheet.getRow(headerDto.getRowStartNumber());

        for(int i = 0; i < detailDto.size(); i++) {
            DownloadDetailDto dto = detailDto.get(i);
            int targetCellNum = dto.getTargetCellNumber();

            Cell cell = row.getCell(targetCellNum);
            detailDtos = new ArrayList<>();

            for(int rowNum = headerDto.getRowStartNumber(); rowNum < worksheet.getPhysicalNumberOfRows(); rowNum++) {
                row = worksheet.getRow(rowNum);
                cell = row.getCell(targetCellNum);

                String cellString = "";
                
                if (cell.getCellType() == CellType.STRING) {
                    cellString = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
                    } else {
                        cellString = String.valueOf((int)cell.getNumericCellValue());
                    }
                }
                
                TranslatedDetailDto translatedDto = TranslatedDetailDto.builder()
                    .id(UUID.randomUUID())
                    .headerName(dto.getHeaderName())
                    .originColData(cellString)
                    .targetCellNumber(dto.getTargetCellNumber())
                    .build();

                detailDtos.add(translatedDto);
            }

            ExcelTranslatorDataDetailDto dataDetailDto = ExcelTranslatorDataDetailDto.builder().details(detailDtos).build();
            
            ExcelTranslatorDataGetDto dataGetDto = ExcelTranslatorDataGetDto.builder()
                .id(UUID.randomUUID())
                .customizedData(dataDetailDto)
                .build();
            
            dataGetDtos.add(dataGetDto);
        }
        return dataGetDtos;
    }
}
