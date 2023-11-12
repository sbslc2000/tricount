package goorm.tricount;

import goorm.tricount.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //단건 조회 테스트, collection으로 나오지 않는 것들
    //count,
    // select * from user where id = 1
    @Test
    void test() {
        String sql = "select count(user.id) from user";
        //데이터 공간에 어떻게 담을 것인가 binding
        //parameter : sql 에 들어갈 값
        //ctrl + u
        //3
        Integer i = jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println("count = " + i);
    }

    //단건 조회 - 클래스 매핑
    //ResultSetMapper
    //RowMapper
    @Test
    void test2() {
        //MySQL Connector, Oracle Driver, ...
        //JDBC API -> Connection, Statement, ResultSet

        //ResultSet
        //id , username, password, nickname
        //->{1, pete, 1234, peter}
        //{2, john, 1234, johnny}
        //{3, mary, 1234, mary}


        Long id = 1L;
        String sql = "select * from user where id = ?";
        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("nickname")
        ), id); //하나가 없으면 exception
        System.out.println("user = " + user);
    }

    @Test
    void test3() {
        String sql = "select * from user";


        ResultSetExtractor<List<User>> extractor = rs -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nickname")
                ));
            }
            return users;
        };

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("nickname")
        ));

    }

    @Test
    void test4() {
        String username = "pete";
        String password = "1234";
        String nickname = "peter";
        String sql = "insert into user (username, password, nickname) values (?,?,?)";

        jdbcTemplate.update(sql, username, password, nickname);
        Long createdUserId = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
    }
}
