package goorm.tricount.domain.expense;

import goorm.tricount.common.RepresentModel;
import goorm.tricount.domain.expense.dto.ExpenseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ExpenseHateoasProcessor implements RepresentationModelProcessor<EntityModel<ExpenseDto>> {
    @Override
    public EntityModel<ExpenseDto> process(EntityModel<ExpenseDto> model) {
        return model.add(linkTo(methodOn(ExpenseController.class).getExpense(model.getContent().getId(), null)).withSelfRel()
        ).add(linkTo(methodOn(ExpenseController.class).getExpense(model.getContent().getId(), null)).withRel("update")
        ).add(linkTo(methodOn(ExpenseController.class).getExpense(model.getContent().getId(), null)).withRel("delete"));
    }
}
