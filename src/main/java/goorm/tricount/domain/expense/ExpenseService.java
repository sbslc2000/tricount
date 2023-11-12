package goorm.tricount.domain.expense;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.domain.expense.dto.CreateExpenseRequestDto;
import goorm.tricount.domain.expense.dto.ExpenseDto;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    public List<ExpenseDto> getExpensesBySettlementId(Long settlementId, User user) {
        if(expenseRepository.hasAuthToReadExpense(user.getId(), settlementId)) {
            return expenseRepository.findBySettlementId(settlementId).stream().map(ExpenseDto::of).toList();
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "정산이 없거나 읽기 권한이 없습니다");
        }
    }

    @Transactional
    public Long createExpense(CreateExpenseRequestDto dto, User creator) {
        if(expenseRepository.hasAuthToCreateExpense(dto.getSettlementId(), creator.getId())) {
            return expenseRepository.save(dto.getTitle(), creator.getId(), dto.getSettlementId(), dto.getAmount());
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "정산이 없거나 생성 권한이 없습니다.");
        }
    }

    @Transactional
    public void deleteExpense(Long expenseId, User loginUser) {
        if(expenseRepository.hasAuthToDeleteExpense(expenseId, loginUser.getId())) {
            expenseRepository.deleteById(expenseId);
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "지출 내역이 없거나 삭제 권한이 없습니다.");
        }
    }
}
