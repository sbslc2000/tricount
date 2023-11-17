package goorm.tricount.domain.expense;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ExpenseRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Expense> findBySettlementId(Long settlementId) {
        String sql = "select * from expense e " +
                "join settlement_user su " +
                "on e.settlement_id = su.settlement_id " +
                "and e.user_id = su.user_id " +
                "join user u " +
                "on u.id = su.user_id " +
                "where su.settlement_id = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Expense(
                rs.getLong("e.id"),
                rs.getString("e.title"),
                new User(
                        rs.getLong("u.id"),
                        rs.getString("u.username"),
                        rs.getString("u.password"),
                        rs.getString("u.nickname")
                ),
                rs.getBigDecimal("e.amount"),
                rs.getDate("e.date").toLocalDate()
        ), settlementId);
    }

    public boolean hasAuthToReadExpense(Long userId, Long settlementId) {
        String sql = "select count(*) from settlement_user " +
                "where user_id = ? and settlement_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, settlementId) > 0;
    }

    public Long save(
            String title,
            Long userId,
            Long settlementId,
            BigDecimal amount
    ) {
        String sql = "insert into expense (title, user_id, settlement_id, amount) " +
                "values (?,?,?,?)";
        jdbcTemplate.update(sql, title, userId, settlementId, amount);
        return jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
    }

    public boolean hasAuthToCreateExpense(Long settlementId, Long creatorId) {
        String sql = "select count(*) from settlement_user " +
                "where settlement_id = ? and user_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, settlementId, creatorId) > 0;

    }

    public boolean hasAuthToDeleteExpense(Long expenseId, Long userId) {
        String sql = "select count(id) from expense " +
                "where id = ? and user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, expenseId, userId) > 0;
    }

    public void deleteById(Long expenseId) {
        String sql = "delete from expense where id = ?";
        int affectedRows = jdbcTemplate.update(sql, expenseId);

        if (affectedRows == 0) {
            throw new ClientFaultException(ErrorCode.INVALID_REQUEST, "삭제에 실패했습니다.");
        }
    }

    public Expense findById(Long expenseId) {
        String sql = "select * from expense e " +
                "join user u " +
                "on e.user_id = u.id " +
                "where e.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Expense(
                rs.getLong("e.id"),
                rs.getString("e.title"),
                new User(
                        rs.getLong("u.id"),
                        rs.getString("u.username"),
                        rs.getString("u.password"),
                        rs.getString("u.nickname")
                ),
                rs.getBigDecimal("e.amount"),
                rs.getDate("e.date").toLocalDate()
        ), expenseId);
    }
}
