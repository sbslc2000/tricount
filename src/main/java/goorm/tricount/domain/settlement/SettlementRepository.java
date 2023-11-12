package goorm.tricount.domain.settlement;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ServerFaultException;
import goorm.tricount.domain.expense.Expense;
import goorm.tricount.domain.settlement.model.Settlement;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long save(Settlement settlement, Long creatorId) {
        String sql = "insert into settlement (title, creator_id) values (?,?)";
        jdbcTemplate.update(sql, settlement.getTitle(), creatorId);
        return jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
    }

    public Settlement findById(Long settlementId) {
        String sql = "select s.id, s.title, u.id, u.username, u.password, u.nickname " +
                "from settlement s " +
                "join settlement_user su " +
                "on su.settlement_id = s.id " +
                "join user u " +
                "on su.user_id = u.id " +
                "where s.id = ?"; //

        return jdbcTemplate.query(sql,
                (rs) -> {
                    Settlement settlement = null;
                    while (rs.next()) {
                        if (settlement == null) {
                            settlement = new Settlement(
                                    rs.getLong("s.id"),
                                    rs.getString("s.title")
                            );
                        }

                        settlement.getUsers().add(new User(
                                rs.getLong("u.id"),
                                rs.getString("u.username"),
                                rs.getString("u.password"),
                                rs.getString("u.nickname")
                        ));
                    }
                    return settlement;
                }
                , settlementId);
    }
    public void deleteById(Long settlementId, Long deleterId) {
        String sql = "delete from settlement where id = ? and creator_id = ?";
        jdbcTemplate.update(sql, settlementId, deleterId);
    }

    public List<Settlement> findByUserId(Long userId) {
        String sql = "select s.id, s.title " +
                "from settlement s " +
                "join settlement_user su " +
                "on su.settlement_id = s.id " +
                "where su.user_id = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Settlement(
                        rs.getLong("s.id"),
                        rs.getString("s.title")
                ),
                userId);
    }

    public int count() {
        String sql = "select count(s.id) from settlement s";
        Integer i = jdbcTemplate.queryForObject(sql, Integer.class);
        return Objects.requireNonNull(i, "count에서 에러가 발생했습니다.");
    }

    public void join(Long settlementId, Long userId) {
        String sql = "insert into settlement_user (settlement_id, user_id) values (?,?)";
        jdbcTemplate.update(sql, settlementId, userId);
    }

    public boolean hasAuthToReadSettlement(Long settlementId, Long userId) {
        String sql = "select count(*) from settlement_user " +
                "where settlement_id = ? and user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, settlementId, userId) > 0;
    }

    //jdbc 한방 쿼리
    public Settlement findBySettlementIdWithExpenseAndUsers(Long settlementId) {
        String sql = "select s.id, s.title, u.id, u.username, u.password, u.nickname, e.id, e.title, e.amount, e.date " +
                "from settlement s " +
                "join settlement_user su " +
                "on su.settlement_id = s.id " +
                "join user u " +
                "on su.user_id = u.id " +
                "left join expense e " +
                "on e.settlement_id = su.settlement_id and e.user_id = su.user_id " +
                "where s.id = ?";

        return jdbcTemplate.query(sql, (rs) -> {
            Settlement settlement = null;
            while(rs.next()) {
                if(settlement == null) {
                    settlement = new Settlement(
                            rs.getLong("s.id"),
                            rs.getString("s.title")
                    );
                }
                //null 발생 validation 해줘야댐
                //비어있거나, 전거랑 다를때
                if(!isUserAlreadyInserted(settlement, rs.getLong("u.id"))) {
                    settlement.getUsers().add(new User(
                            rs.getLong("u.id"),
                            rs.getString("u.username"),
                            rs.getString("u.password"),
                            rs.getString("u.nickname")
                    ));
                }

                if(rs.getLong("e.id") != 0) {
                    settlement.getExpenses().add(new Expense(
                            rs.getLong("e.id"),
                            rs.getString("e.title"),
                            settlement.getUsers().stream().filter(user -> {
                                        try {
                                            return user.getId().equals(rs.getLong("u.id"));
                                        } catch (SQLException e) {
                                            throw new ServerFaultException(ErrorCode.DATA_INTEGRITY_FAILURE);
                                        }
                                    }
                            ).findFirst().orElseThrow(
                                    () -> new ServerFaultException(ErrorCode.DATA_INTEGRITY_FAILURE)),
                            rs.getBigDecimal("e.amount"),
                            rs.getDate("e.date").toLocalDate()
                    ));
                }
            }
            return settlement;
        }, settlementId);
    }

    private boolean isUserAlreadyInserted(Settlement settlement, Long userId) {
        return settlement.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
    }

    public boolean hasAuthToDeleteSettlement(Long settlementId, Long deleterId) {
        String sql = "select count(s.id) " +
                "from settlement s " +
                "where s.id = ? " +
                "and creator_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, settlementId, deleterId) > 0;
    }
}
