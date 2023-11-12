package goorm.tricount.domain.settlement;

import goorm.tricount.domain.settlement.dto.SettlementDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SettlementRepresentationModelProcessor implements RepresentationModelProcessor<EntityModel<SettlementDto.Detail>> {
    @Override
    public EntityModel<SettlementDto.Detail> process(EntityModel<SettlementDto.Detail> model) {
        Long settlementId = Objects.requireNonNull(model.getContent(),"심각한 오류 발생!").getId();
        return model.add(linkTo(methodOn(SettlementController.class).getSettlement(settlementId, null)).withSelfRel())
                .add(linkTo(methodOn(SettlementController.class).getBalance(settlementId, null)).withRel("balance"));
//                .add(linkTo(methodOn(SettlementController.class).joinSettlement(settlementId,null)).withRel("join"));
    }
}
