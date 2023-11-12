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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    @Transactional
    public Long createAndJoinSettlement(User user, CreateSettlementRequestDto dto) {
        Settlement settlement = new Settlement(dto.getName());
        Long createdSettlementId = settlementRepository.save(settlement, user.getId());
        joinSettlement(createdSettlementId, user);
        return createdSettlementId;
    }

    public List<SettlementDto.Summary> findByUserId(Long userId) {
        return settlementRepository.findByUserId(userId)
                .stream().map(SettlementDto.Summary::of).toList();
    }

    public SettlementDto.Detail getSettlement(Long settlementId, User loginUser) {
        if(settlementRepository.hasAuthToReadSettlement(settlementId, loginUser.getId())) {
            return SettlementDto.Detail.of(settlementRepository.findById(settlementId));
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "정산이 없거나 읽기 권한이 없습니다.");
        }

    }

    @Transactional
    public void deleteSettlement(Long deleterId, Long settlementId) {
        if(settlementRepository.hasAuthToDeleteSettlement(settlementId, deleterId)) {
            settlementRepository.deleteById(settlementId, deleterId);
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "정산이 없거나 삭제 권한이 없습니다.");
        }
    }

    @Transactional
    public void joinSettlement(Long settlementId, User joinUser) {
        try {
            settlementRepository.join(settlementId, joinUser.getId());
        } catch (DuplicateKeyException e) {
            throw new ClientFaultException(ErrorCode.INVALID_REQUEST, "이미 가입한 정산입니다.");
        } catch (DataIntegrityViolationException e) {
            throw new ClientFaultException(ErrorCode.INVALID_REQUEST, "정산이 존재하지 않습니다.");
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
