package goorm.tricount.domain.settlement.dto;

import goorm.tricount.domain.settlement.model.Settlement;
import goorm.tricount.domain.user.dto.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class SettlementDto {

    private Long id;
    private String title;


    public static class Summary extends SettlementDto {

        public static Summary of(Settlement settlement) {
            Summary summary = new Summary();
            summary.setId(settlement.getId());
            summary.setTitle(settlement.getTitle());
            return summary;
        }
    }

    @Data
    public static class Detail extends SettlementDto {
        private List<UserDto> users;

        public static Detail of(Settlement settlement) {
            Detail detail = new Detail();
            detail.setId(settlement.getId());
            detail.setTitle(settlement.getTitle());
            detail.setUsers(settlement.getUsers().stream().map(UserDto::of).toList());
            return detail;
        }
    }
}
