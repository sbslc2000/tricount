package goorm.tricount.domain.expense;

import goorm.tricount.common.Entity;
import goorm.tricount.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Expense extends Entity {

    private String title;
    private User user;
    private BigDecimal amount;
    private LocalDate date;

    public Expense(String title, User user, BigDecimal amount, LocalDate date) {
        this.title = title;
        this.user = user;
        this.amount = amount;
        this.date = date;
    }

    public Expense(Long id, String title, User user, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.amount = amount;
        this.date = date;
    }
}
