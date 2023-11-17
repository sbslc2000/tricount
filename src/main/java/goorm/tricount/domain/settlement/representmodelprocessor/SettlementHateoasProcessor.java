package goorm.tricount.domain.settlement.representmodelprocessor;

import goorm.tricount.common.RepresentModel;
import goorm.tricount.domain.expense.ExpenseController;
import goorm.tricount.domain.expense.dto.CreateExpenseRequestDto;
import goorm.tricount.domain.settlement.SettlementController;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import goorm.tricount.domain.user.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class SettlementHateoasProcessor implements RepresentationModelProcessor<EntityModel<SettlementDto.Detail>> {
    @Override
    public EntityModel<SettlementDto.Detail> process(EntityModel<SettlementDto.Detail> model) {
        Class<SettlementController> settlementControllerClass = SettlementController.class;
        Class<ExpenseController> expenseControllerClass = ExpenseController.class;

        Long settlementId = Objects.requireNonNull(model.getContent(), "심각한 오류 발생!").getId();

        Method joinSettlementMethod = ReflectionUtils.findMethod(settlementControllerClass, "joinSettlement", Long.class, User.class);
        Method addExpenseMethod = ReflectionUtils.findMethod(expenseControllerClass, "addExpense", CreateExpenseRequestDto.class, User.class);

        model.add(linkTo(methodOn(settlementControllerClass).getSettlement(settlementId, null)).withSelfRel())
                .add(linkTo(methodOn(settlementControllerClass).getBalance(settlementId, null)).withRel("balance"))
                .add(linkTo(joinSettlementMethod, settlementId, null).withRel("join"))
//                .add(Link.of("http://localhost:8080/settle/" + settlementId + "/balance", "balance"))
                .add(linkTo(addExpenseMethod, null, null).withRel("addExpense"));

        return model;
    }
}
