package goorm.tricount.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nickname")
    );

    public User save(User user) {
        jdbcTemplate.update("INSERT INTO user (username, password, nickname) VALUES (?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getNickname());

        Long lastInsertId = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);

        return new User(lastInsertId, user.getUsername(), user.getPassword(), user.getNickname());
    }

    public User findById(Long userId) {
        String sql = "select * from user where id = ?";
        return jdbcTemplate.queryForObject(sql,userMapper, userId);
    }

    public User findByUsername(String username) {
        String sql = "select * from user where username = ?";
        return jdbcTemplate.queryForObject(sql, userMapper, username);
    }
}
