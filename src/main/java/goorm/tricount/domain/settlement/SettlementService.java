package goorm.tricount.domain.settlement;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.domain.settlement.dto.CreateSettlementRequestDto;
import goorm.tricount.domain.settlement.dto.SettlementDto;
import goorm.tricount.domain.settlement.model.Settlement;
import goorm.tricount.domain.settlement.dto.Transfer;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    public void createAndJoinSettlement(Long userId, CreateSettlementRequestDto dto) {
        Settlement settlement = new Settlement(dto.getName());
        Long createdSettlementId = settlementRepository.save(settlement, userId);
        joinSettlement(createdSettlementId, userId);
    }

    public List<SettlementDto.Summary> findByUserId(Long userId) {
        return settlementRepository.findByUserId(userId)
                .stream().map(SettlementDto.Summary::of).toList();
    }

    public SettlementDto.Detail getSettlement(Long settlementId) {
        return SettlementDto.Detail.of(settlementRepository.findById(settlementId).orElseThrow(
                () -> new ClientFaultException(ErrorCode.INVALID_REQUEST, "정산이 존재하지 않습니다.")
        ));
    }

    public void deleteSettlement(Long deleterId, Long settlementId) {
        int updatedRows = settlementRepository.deleteById(settlementId, deleterId);

        if(updatedRows == 0) {
            throw new ClientFaultException(ErrorCode.INVALID_REQUEST, "삭제에 실패했습니다.");
        }
    }

    public void joinSettlement(Long settlementId, Long userId) {
        try {
            settlementRepository.join(settlementId, userId);
        } catch (DuplicateKeyException e) {
            throw new ClientFaultException(ErrorCode.INVALID_REQUEST, "이미 가입한 정산입니다.");
        }
    }

    public List<Transfer> calculateBalance(Long settlementId, User loginUser) {
        if(settlementRepository.hasAuthToReadSettlement(settlementId, loginUser.getId())) {
            Settlement settlement = settlementRepository.findBySettlementIdWithExpenseAndUsers(settlementId);
            return settlement.calculateBalance();
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "정산이 없거나 읽기 권한이 없습니다.");
        }
    }
}
