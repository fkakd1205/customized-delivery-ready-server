package com.customized_delivery_ready.server.service.excel_translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorUploadHeaderDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.UploadDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.customized_delivery_ready.server.model.excel_translator_header.repository.ExcelTranslatorHeaderRepository;
import com.customized_delivery_ready.server.model.upload_excel_data.dto.UploadExcelDataDetailDto;
import com.customized_delivery_ready.server.model.upload_excel_data.dto.UploadExcelDataGetDto;
import com.customized_delivery_ready.server.model.upload_excel_data.dto.UploadedDetailDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelTranslatorHeaderService {
    
    @Autowired
    private ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository;

    public ExcelTranslatorHeaderGetDto createTitle(ExcelTranslatorHeaderGetDto dto) {
        ExcelTranslatorHeaderEntity entity = ExcelTranslatorHeaderEntity.toEntity(dto);
        excelTranslatorHeaderRepository.save(entity);
        return dto;
    }

    public List<ExcelTranslatorHeaderGetDto> searchList() {
        List<ExcelTranslatorHeaderEntity> entities = excelTranslatorHeaderRepository.findAll();
        List<ExcelTranslatorHeaderGetDto> dtos = entities.stream().map(r -> ExcelTranslatorHeaderGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    public List<UploadExcelDataGetDto> uploadExcelFile(MultipartFile file, ExcelTranslatorHeaderGetDto dto) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<UploadExcelDataGetDto> excelDto = this.getUploadedExcelForm(sheet, dto);
        return excelDto;
    }

    private List<UploadExcelDataGetDto> getUploadedExcelForm(Sheet worksheet, ExcelTranslatorHeaderGetDto dto) {
        List<UploadExcelDataGetDto> dtos = new ArrayList<>();
        List<UploadDetailDto> uploadDetailDtos = dto.getUploadHeaderDetail().getDetails();

        if(uploadDetailDtos.size() != dto.getUploadHeaderDetail().getDetails().size()) {
            throw new IllegalArgumentException();
        }

        for(int i = dto.getRowStartNumber()-1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            List<UploadedDetailDto> uploadedDetailDtos = new ArrayList<>();

            for(int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);

                Object cellObj = new Object();
                if(cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellObj = "";
                } else if (cell.getCellType().equals(CellType.STRING)) {
                    cellObj = cell.getStringCellValue();
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellObj = cell.getDateCellValue();
                    } else {
                        cellObj = cell.getNumericCellValue();
                    }
                }

                if(uploadDetailDtos.size() > 0 && i == dto.getRowStartNumber()-1) {
                    if(!uploadDetailDtos.get(j).getHeaderName().equals(cellObj.toString())){
                        throw new IllegalArgumentException();
                    }
                }

                UploadedDetailDto uploadedDto = UploadedDetailDto.builder().colData(cellObj).cellType(cellObj.getClass().getSimpleName()).build();  
                uploadedDetailDtos.add(uploadedDto);
            }
            
            UploadExcelDataDetailDto detailDto = UploadExcelDataDetailDto.builder().details(uploadedDetailDtos).build();
            UploadExcelDataGetDto dataDto = UploadExcelDataGetDto.builder().id(UUID.randomUUID()).uploadedData(detailDto).build();
            dtos.add(dataDto);
        }

        return dtos;
    }

    public void updateUploadHeaderDetailOfExcelTranslator(ExcelTranslatorHeaderGetDto dto) {
        Optional<ExcelTranslatorHeaderEntity> entityOpt = excelTranslatorHeaderRepository.findById(dto.getId());

        if (entityOpt.isPresent()) {
            ExcelTranslatorHeaderEntity entity = entityOpt.get();
            entity.setUploadHeaderDetail(dto.getUploadHeaderDetail());
            excelTranslatorHeaderRepository.save(entity);
        } else {
            throw new NullPointerException();
        }
    }
}
