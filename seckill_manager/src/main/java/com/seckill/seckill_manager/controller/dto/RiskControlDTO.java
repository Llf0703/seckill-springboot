package com.seckill.seckill_manager.controller.dto;

import com.seckill.seckill_manager.entity.RiskControl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RiskControlOptionsDTO {
    private String label;
    private Integer value;
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
}
