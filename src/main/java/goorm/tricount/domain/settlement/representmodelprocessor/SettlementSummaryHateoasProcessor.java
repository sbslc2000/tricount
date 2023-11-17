package goorm.tricount.domain.settlement.representmodelprocessor;

import goorm.tricount.common.RepresentModel;
import goorm.tricount.domain.settlement.SettlementController;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepresentModel
public class SettlementSummaryHateoasProcessor implements RepresentationModelProcessor<EntityModel<SettlementDto.Summary>> {
    @Override
    public EntityModel<SettlementDto.Summary> process(EntityModel<SettlementDto.Summary> model) {

        Long settlementId = model.getContent().getId();
        model.add(linkTo(methodOn(SettlementController.class).getSettlement(settlementId, null)).withSelfRel());
        model.add(linkTo(methodOn(SettlementController.class).getBalance(settlementId, null)).withRel("balance"));

        return model;
    }
}
