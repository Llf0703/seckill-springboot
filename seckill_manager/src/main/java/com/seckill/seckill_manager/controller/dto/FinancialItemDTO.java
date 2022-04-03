package com.seckill.seckill_manager.controller.dto;

import cn.hutool.core.bean.BeanUtil;
import com.seckill.seckill_manager.controller.vo.FinancialItemVO;
import com.seckill.seckill_manager.entity.FinancialItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class FinancialItemOptionsDTO {
    private String label;
    private Integer value;
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName FinancialItemTableDTO
 * @description: 后台管理表格数据类型
 * @date 2022/3/25 2:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class FinancialItemTableDTO {
    private Integer id;

    private String createdAt;

    private String updatedAt;

    private String productName;

    private BigDecimal dailyCumulativeLimit;

    private String automaticRedemptionAtMaturity;

    private BigDecimal incrementAmount;

    private String productExpirationDate;

    private BigDecimal maximumSinglePurchaseAmount;

    private BigDecimal minimumDepositAmount;

    private String earlyWithdrawal;

    private String productEffectiveDate;

    private String shelfLife;

    private String interestRate;
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName FinancialItemPostFormDTO
 * @description: 后台管理表单数据类型
 * @date 2022/3/25 2:21
 */
class FinancialItemPostFormDTO extends FinancialItemVO {

}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName FinancialItemDTO
 * @description: service数据处理
 * @date 2022/3/25 2:29
 */
public class FinancialItemDTO {
    /*
     * @MethodName toFinancialItemTableDTO
     * @author Wky1742095859
     * @Description entity转前端表格对象
     * @Date 2022/3/25 2:30
     * @Param [financialItems]
     * @Return com.seckill.seckill_manager.controller.dto.FinancialItemTableDTO
     **/
    public static FinancialItemTableDTO toFinancialItemTableDTO(FinancialItems financialItems) {
        FinancialItemTableDTO financialItemTableDTO = new FinancialItemTableDTO();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        financialItemTableDTO.setId(financialItems.getId());
        financialItemTableDTO.setCreatedAt(financialItems.getCreatedAt().format(dateTimeFormatter));
        financialItemTableDTO.setUpdatedAt(financialItems.getUpdatedAt().format(dateTimeFormatter));
        financialItemTableDTO.setProductName(financialItems.getProductName());
        financialItemTableDTO.setDailyCumulativeLimit(financialItems.getDailyCumulativeLimit());
        if (financialItems.getAutomaticRedemptionAtMaturity() == 0) {
            financialItemTableDTO.setAutomaticRedemptionAtMaturity("否");
        } else {
            financialItemTableDTO.setAutomaticRedemptionAtMaturity("是");
        }
        financialItemTableDTO.setIncrementAmount(financialItems.getIncrementAmount());
        financialItemTableDTO.setProductExpirationDate(financialItems.getProductExpirationDate().format(dateTimeFormatter));
        financialItemTableDTO.setMaximumSinglePurchaseAmount(financialItems.getMaximumSinglePurchaseAmount());
        financialItemTableDTO.setMinimumDepositAmount(financialItems.getMinimumDepositAmount());
        if (financialItems.getEarlyWithdrawal() == 0) {
            financialItemTableDTO.setEarlyWithdrawal("否");
        } else {
            financialItemTableDTO.setEarlyWithdrawal("是");
        }
        financialItemTableDTO.setProductEffectiveDate(financialItems.getProductEffectiveDate().format(dateTimeFormatter));
        financialItemTableDTO.setShelfLife(financialItems.getShelfLife().toString() + "天");
        financialItemTableDTO.setInterestRate((financialItems.getInterestRate().multiply(new BigDecimal("100"))) + "%");
        return financialItemTableDTO;
    }

    /*
     * @MethodName toFinancialItemTableDTO
     * @author Wky1742095859
     * @Description 批量entity转前端表格对象
     * @Date 2022/3/25 2:30
     * @Param [financialItemsList]
     * @Return java.util.List<com.seckill.seckill_manager.controller.dto.FinancialItemTableDTO>
     **/
    public static List<FinancialItemTableDTO> toFinancialItemTableDTO(List<FinancialItems> financialItemsList) {
        List<FinancialItemTableDTO> financialItemDTOLinkedList = new LinkedList<>();
        for (FinancialItems items : financialItemsList) {
            financialItemDTOLinkedList.add(toFinancialItemTableDTO(items));
        }
        return financialItemDTOLinkedList;
    }

    /*
     * @MethodName toFinancialItemPostFormDTO
     * @author Wky1742095859
     * @Description entity转前端表单对象
     * @Date 2022/3/25 2:31
     * @Param [financialItems]
     * @Return com.seckill.seckill_manager.controller.dto.FinancialItemPostFormDTO
     **/
    public static FinancialItemPostFormDTO toFinancialItemPostFormDTO(FinancialItems financialItems) {
        FinancialItemPostFormDTO financialItemVO = new FinancialItemPostFormDTO();
        BeanUtil.copyProperties(financialItems, financialItemVO, true);
        financialItemVO.setInterestRate(financialItems.getInterestRate().multiply(new BigDecimal("100")));
        return financialItemVO;
    }

    /*
     * @MethodName toFinancialItemPostFormDTO
     * @author Wky1742095859
     * @Description 批量entity转前端表单对象
     * @Date 2022/3/25 2:31
     * @Param [financialItemsList]
     * @Return java.util.List<com.seckill.seckill_manager.controller.dto.FinancialItemPostFormDTO>
     **/
    public static List<FinancialItemPostFormDTO> toFinancialItemPostFormDTO(List<FinancialItems> financialItemsList) {
        List<FinancialItemPostFormDTO> financialItemVOList = new LinkedList<>();
        for (FinancialItems items : financialItemsList) {
            financialItemVOList.add(toFinancialItemPostFormDTO(items));
        }
        return financialItemVOList;
    }

    public static FinancialItemOptionsDTO toFinancialItemOptionsDTO(FinancialItems financialItems) {
        return new FinancialItemOptionsDTO(financialItems.getProductName(), financialItems.getId());
    }

    public static List<FinancialItemOptionsDTO> toFinancialItemOptionsDTO(List<FinancialItems> financialItemsList) {
        List<FinancialItemOptionsDTO> financialItemOptionsDTOList = new LinkedList<>();
        for (FinancialItems item : financialItemsList) {
            financialItemOptionsDTOList.add(toFinancialItemOptionsDTO(item));
        }
        return financialItemOptionsDTOList;
    }
}
