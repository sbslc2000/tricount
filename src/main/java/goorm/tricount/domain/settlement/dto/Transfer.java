package goorm.tricount.domain.settlement.dto;

import goorm.tricount.common.Entity;
import goorm.tricount.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transfer {
    private Long senderUserId;
    private String senderUserNickname;
    private BigDecimal sendAmount;
    private Long receiverUserId;
    private String receiverUserNickname;
}
