package goorm.tricount.domain.settlement;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.common.login.LoginUser;
import goorm.tricount.domain.settlement.dto.CreateSettlementRequestDto;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import goorm.tricount.domain.settlement.dto.Transfer;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settle")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping
    public List<SettlementDto.Summary> getSettlement(@LoginUser User loginUser) {
        return settlementService.findByUserId(loginUser.getId());
    }

    @GetMapping("/{settlementId}")
    public BaseResponse<SettlementDto.Detail> getSettlement(@PathVariable Long settlementId) {
        SettlementDto.Detail settlement = settlementService.getSettlement(settlementId);
        return BaseResponse.ok(settlement);
    }

    @PostMapping
    public void createAndJoinSettlement(
            @RequestBody CreateSettlementRequestDto dto,
            @LoginUser User loginUser
    ) {
        settlementService.createAndJoinSettlement(loginUser.getId(), dto);
    }

    @PostMapping("/{settlementId}/join")
    public void joinSettlement(
            @PathVariable Long settlementId,
            @LoginUser User loginUser
    ) {
        settlementService.joinSettlement(settlementId, loginUser.getId());
    }

    @DeleteMapping("/{settlementId}")
    public void deleteSettlement(
            @LoginUser User loginUser,
            @PathVariable Long settlementId
    ) {
        settlementService.deleteSettlement(loginUser.getId(), settlementId);
    }

    @GetMapping("/{settlementId}/balance")
    public List<Transfer> getBalance(
            @PathVariable Long settlementId,
            @LoginUser User loginUser
            ) {
        return settlementService.calculateBalance(settlementId, loginUser);
    }
}
