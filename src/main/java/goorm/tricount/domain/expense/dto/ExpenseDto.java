package goorm.tricount.domain.expense.dto;

import goorm.tricount.domain.expense.Expense;
import goorm.tricount.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseDto {
    private Long id;
    private String name;
    private String nickname;
    private BigDecimal amount;
    private LocalDate date;

    public static ExpenseDto of(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getTitle(),
                expense.getUser().getNickname(),
                expense.getAmount(),
                expense.getDate()
        );
    }
}
