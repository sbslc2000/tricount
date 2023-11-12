package goorm.tricount.domain.expense.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateExpenseRequestDto {
    private String title;
    private Long settlementId;

    private BigDecimal amount;
}
