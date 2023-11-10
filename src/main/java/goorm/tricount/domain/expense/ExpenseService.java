package goorm.tricount.domain.expense;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.domain.expense.dto.CreateExpenseRequestDto;
import goorm.tricount.domain.expense.dto.ExpenseDto;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    public List<ExpenseDto> getExpensesBySettlementId(Long settlementId, User user) {
        if(expenseRepository.hasAuthToReadExpense(user.getId(), settlementId)) {
            return expenseRepository.findBySettlementId(settlementId).stream().map(ExpenseDto::of).toList();
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "읽기 권한이 없거나 없는 정산입니다.");
        }
    }

    public void createExpense(CreateExpenseRequestDto dto, User creator) {
        /*
         * todo: validation
         * user가 존재? 이건 존재
         * settlement가 존재? 이건 확인해봐야됨
         * user가 settlement에 속해있는지? 이것도 확인해봐야됨
         * amount가 0보다 큰지? 이건 여기서 안해도 됨
         */
        if(expenseRepository.hasAuthToCreateExpense(dto.getSettlementId(), creator.getId())) {
            expenseRepository.save(dto.getTitle(), creator.getId(), dto.getSettlementId(), dto.getAmount());

        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "Expense를 생성할 수 없습니다.");
        }
    }



    public void deleteExpense(Long expenseId, User loginUser) {
        if(expenseRepository.hasAuthToDeleteExpense(expenseId, loginUser.getId())) {
            expenseRepository.deleteById(expenseId);
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "Expense를 삭제할 수 없습니다.");
        }
    }
}
