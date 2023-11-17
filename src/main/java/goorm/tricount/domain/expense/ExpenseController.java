package goorm.tricount.domain.expense;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.api.response.BaseResponseTemplate;
import goorm.tricount.common.login.LoginUser;
import goorm.tricount.domain.expense.dto.CreateExpenseRequestDto;
import goorm.tricount.domain.expense.dto.ExpenseDto;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public List<ExpenseDto> getExpenses(
            @RequestParam Long settlementId,
            @LoginUser User loginUser) {
        return expenseService.getExpensesBySettlementId(settlementId, loginUser);
    }

    @GetMapping("/{expenseId}")
    public ExpenseDto getExpense(
            @PathVariable Long expenseId,
            @LoginUser User loginUser
    ) {
        return expenseService.getExpense(expenseId, loginUser);
    }

    @PostMapping
    public Long addExpense(
            @RequestBody CreateExpenseRequestDto dto,
            @LoginUser User loginUser
            ) {
        return expenseService.createExpense(dto, loginUser);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteExpense(
        @PathVariable Long expenseId,
        @LoginUser User loginUser
    ) {
        expenseService.deleteExpense(expenseId, loginUser);
    }

}
