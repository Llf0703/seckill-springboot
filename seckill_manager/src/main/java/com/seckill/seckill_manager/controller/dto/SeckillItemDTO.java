package com.seckill.seckill_manager.controller.dto;

import cn.hutool.core.bean.BeanUtil;
import com.seckill.seckill_manager.controller.vo.SeckillItemVO;
import com.seckill.seckill_manager.entity.SeckillItems;
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
class SeckillItemTableDTO {
    private Integer id;

    private String createdAt;

    private String updatedAt;

    private String title;

    private Long stock;

    private BigDecimal amount;

    private String description;

    private String startTime;

    private String endTime;

    private Long remainingStock;

    private Integer financialItemId;

    private Integer riskControlId;
}

class SeckillItemPostFormDTO extends SeckillItemVO {

}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName SeckillItemDTO
 * @description: entity转换类
 * @date 2022/3/27 0:52
 */
public class SeckillItemDTO {
    public static SeckillItemTableDTO toSeckillItemTableDTO(SeckillItems seckillItems) {
        SeckillItemTableDTO seckillItemTableDTO = new SeckillItemTableDTO();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        seckillItemTableDTO.setId(seckillItems.getId());
        seckillItemTableDTO.setUpdatedAt(seckillItems.getUpdatedAt().format(dateTimeFormatter));
        seckillItemTableDTO.setCreatedAt(seckillItems.getCreatedAt().format(dateTimeFormatter));
        seckillItemTableDTO.setTitle(seckillItems.getTitle());
        seckillItemTableDTO.setStock(seckillItems.getStock());
        seckillItemTableDTO.setAmount(seckillItems.getAmount());
        seckillItemTableDTO.setDescription(seckillItems.getDescription());
        seckillItemTableDTO.setStartTime(seckillItems.getStartTime().format(dateTimeFormatter));
        seckillItemTableDTO.setEndTime(seckillItems.getEndTime().format(dateTimeFormatter));
        seckillItemTableDTO.setRemainingStock(seckillItems.getRemainingStock());
        seckillItemTableDTO.setFinancialItemId(seckillItems.getFinancialItemId());
        seckillItemTableDTO.setRiskControlId(seckillItems.getRiskControlId());
        return seckillItemTableDTO;
    }

    public static List<SeckillItemTableDTO> toSeckillItemTableDTO(List<SeckillItems> seckillItemsList) {
        List<SeckillItemTableDTO> seckillItemTableDTOList = new LinkedList<>();
        for (SeckillItems item : seckillItemsList
        ) {
            seckillItemTableDTOList.add(toSeckillItemTableDTO(item));
        }
        return seckillItemTableDTOList;
    }

    public static SeckillItemPostFormDTO toSeckillPostFormDTO(SeckillItems seckillItems) {
        SeckillItemPostFormDTO seckillItemPostFormDTO = new SeckillItemPostFormDTO();
        BeanUtil.copyProperties(seckillItems, seckillItemPostFormDTO, true);
        return seckillItemPostFormDTO;
    }

    public static List<SeckillItemPostFormDTO> toSeckillPostFormDTO(List<SeckillItems> seckillItemsList) {
        List<SeckillItemPostFormDTO> seckillItemPostFormDTOList = new LinkedList<>();
        for (SeckillItems item :
                seckillItemsList) {
            seckillItemPostFormDTOList.add(toSeckillPostFormDTO(item));
        }
        return seckillItemPostFormDTOList;
    }
}
