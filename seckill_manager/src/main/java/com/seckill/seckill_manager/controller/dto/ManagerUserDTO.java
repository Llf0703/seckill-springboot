package com.seckill.seckill_manager.controller.dto;

import cn.hutool.core.bean.BeanUtil;
import com.seckill.seckill_manager.controller.vo.ManagerUsersVO;
import com.seckill.seckill_manager.entity.ManagerUsers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ManagerUserTableDTO {
    private Integer id;

    private String createdAt;

    private String updatedAt;


    private String account;

    //private String password;

    private String seckillItemsPermissions;

    private String seckillRecordPermissions;

    private String rechargeRecordPermissions;

    private String adminInfoPermissions;

    private String financialItemsPermissions;

    private String riskControlPermissions;

    private String guestInfoPermissions;
}

class ManagerUserPostFormDTO extends ManagerUsersVO {
}

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName ManagerUserDTO
 * @description: 用于将entity转化为前端需要的数据
 * @date 2022/3/25 2:16
 */
public class ManagerUserDTO {
    private static String toPermissionsDTO(Integer permission) {
        switch (permission) {
            case 1:
                return "可自由查看";
            case 2:
                return "可自由查看,增添,修改,删除";
            case 0:
            default:
                return "无权限";
        }
    }

    public static ManagerUserTableDTO toManagerUserTableDTO(ManagerUsers managerUsers) {
        ManagerUserTableDTO managerUserTableDTO = new ManagerUserTableDTO();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        managerUserTableDTO.setId(managerUsers.getId());
        managerUserTableDTO.setCreatedAt(managerUsers.getCreatedAt().format(dateTimeFormatter));
        managerUserTableDTO.setUpdatedAt(managerUsers.getUpdatedAt().format(dateTimeFormatter));
        managerUserTableDTO.setAccount(managerUsers.getAccount());
        managerUserTableDTO.setSeckillItemsPermissions(toPermissionsDTO(managerUsers.getSeckillItemsPermissions()));
        managerUserTableDTO.setSeckillRecordPermissions(toPermissionsDTO(managerUsers.getSeckillRecordPermissions()));
        managerUserTableDTO.setRechargeRecordPermissions(toPermissionsDTO(managerUsers.getRechargeRecordPermissions()));
        managerUserTableDTO.setAdminInfoPermissions(toPermissionsDTO(managerUsers.getAdminInfoPermissions()));
        managerUserTableDTO.setFinancialItemsPermissions(toPermissionsDTO(managerUsers.getFinancialItemsPermissions()));
        managerUserTableDTO.setRiskControlPermissions(toPermissionsDTO(managerUsers.getRiskControlPermissions()));
        managerUserTableDTO.setGuestInfoPermissions(toPermissionsDTO(managerUsers.getGuestInfoPermissions()));
        return managerUserTableDTO;
    }

    public static List<ManagerUserTableDTO> toManagerUserTableDTO(List<ManagerUsers> managerUsersList) {
        List<ManagerUserTableDTO> managerUserTableDTOList = new LinkedList<>();
        for (ManagerUsers item : managerUsersList) {
            managerUserTableDTOList.add(toManagerUserTableDTO(item));
        }
        return managerUserTableDTOList;
    }

    public static ManagerUserPostFormDTO toManagerUserPostFormDTO(ManagerUsers managerUsers) {
        ManagerUserPostFormDTO managerUserPostFormDTO = new ManagerUserPostFormDTO();
        BeanUtil.copyProperties(managerUsers, managerUserPostFormDTO, true);
        managerUserPostFormDTO.setPassword("");//屏蔽密码
        return managerUserPostFormDTO;
    }

    public static List<ManagerUserPostFormDTO> toManagerUserPostFormDTO(List<ManagerUsers> managerUsersList) {
        List<ManagerUserPostFormDTO> managerUserPostFormDTOList = new LinkedList<>();
        for (ManagerUsers item :
                managerUsersList) {
            managerUserPostFormDTOList.add(toManagerUserPostFormDTO(item));
        }
        return managerUserPostFormDTOList;
    }
}
