package com.seckill.seckill_manager.controller.dto;

import com.seckill.seckill_manager.entity.RiskControl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RiskControlOptionsDTO {
    private String label;
    private Integer value;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RiskControlTableDTO {
    private Integer id;

    private String createdAt;

    private String updatedAt;

    private String policyName;

    private String workingStatusLimit;

    private String untrustworthyPersonLimit;

    private String ageLimit;

    private String overdueLimit;

    private String exceptionLimit;
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName RiskControlDTO
 * @description: 决策引擎entity转前端数据
 * @date 2022/3/28 1:56
 */
public class RiskControlDTO {
    public static RiskControlOptionsDTO toRiskControlOptionsDTO(RiskControl riskControl) {
        return new RiskControlOptionsDTO(riskControl.getPolicyName(), riskControl.getId());
    }

    public static List<RiskControlOptionsDTO> toRiskControlOptionsDTO(List<RiskControl> riskControlList) {
        List<RiskControlOptionsDTO> riskControlOptionsDTOList = new LinkedList<>();
        for (RiskControl item : riskControlList) {
            riskControlOptionsDTOList.add(toRiskControlOptionsDTO(item));
        }
        return riskControlOptionsDTOList;
    }

    public static RiskControlTableDTO toRiskControlTableDTO(RiskControl riskControl) {
        RiskControlTableDTO riskControlTableDTO = new RiskControlTableDTO();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        riskControlTableDTO.setCreatedAt(riskControl.getCreatedAt().format(dateTimeFormatter));
        riskControlTableDTO.setUpdatedAt(riskControl.getUpdatedAt().format(dateTimeFormatter));
        riskControlTableDTO.setId(riskControl.getId());
        riskControlTableDTO.setPolicyName(riskControl.getPolicyName());
        riskControlTableDTO.setAgeLimit(riskControl.getAgeLimit().toString());
        if (riskControl.getUntrustworthyPersonLimit() == 0) {
            riskControlTableDTO.setUntrustworthyPersonLimit("否");
        } else {
            riskControlTableDTO.setUntrustworthyPersonLimit("是");
        }
        if (riskControl.getWorkingStatusLimit() == 0) {
            riskControlTableDTO.setWorkingStatusLimit("否");
        } else {
            riskControlTableDTO.setWorkingStatusLimit("是");
        }
        riskControlTableDTO.setOverdueLimit("最近" + riskControl.getOverdueYearLimit() + "年逾期" + riskControl.getOverdueNumberLimit() + "次");
        riskControlTableDTO.setExceptionLimit("金额小于" + riskControl.getExceptionAmount() + "元," + riskControl.getExceptionDays() + "天内还清");
        return riskControlTableDTO;
    }

    public static List<RiskControlTableDTO> toRiskControlTableDTO(List<RiskControl> riskControlList) {
        List<RiskControlTableDTO> riskControlTableDTOList = new LinkedList<>();
        for (RiskControl item : riskControlList) {
            riskControlTableDTOList.add(toRiskControlTableDTO(item));
        }
        return riskControlTableDTOList;
    }
}
