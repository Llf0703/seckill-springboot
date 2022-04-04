package com.seckill.seckill_manager.controller.dto;

import com.seckill.seckill_manager.entity.OperateRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName OperateRecordDTO
 * @description: entity转前端数据
 * @date 2022/4/5 0:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class OperateRecordTableDTO {
    private Long id;

    private String createdAt;

    private Integer managerUserId;

    private String managerUserAccount;

    private String operate;

    private Integer level;

    private Integer operateId;
}

public class OperateRecordDTO {
    public static OperateRecordTableDTO toOperateRecordTableDTO(OperateRecord operateRecord) {
        OperateRecordTableDTO operateRecordTableDTO = new OperateRecordTableDTO();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        operateRecordTableDTO.setId(operateRecord.getId());
        operateRecordTableDTO.setCreatedAt(operateRecord.getCreatedAt().format(dateTimeFormatter));
        operateRecordTableDTO.setManagerUserId(operateRecord.getManagerUserId());
        operateRecordTableDTO.setOperate(operateRecord.getOperate());
        operateRecordTableDTO.setLevel(operateRecord.getLevel());
        operateRecordTableDTO.setOperateId(operateRecord.getOperateId());
        return operateRecordTableDTO;
    }

    public static List<OperateRecordTableDTO> toOperateRecordTableDTO(List<OperateRecord> operateRecordList) {
        List<OperateRecordTableDTO> operateRecordTableDTOList = new LinkedList<>();
        for (OperateRecord item : operateRecordList) {
            operateRecordTableDTOList.add(toOperateRecordTableDTO(item));
        }
        return operateRecordTableDTOList;
    }
}
