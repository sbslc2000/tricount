package goorm.tricount;

import goorm.tricount.domain.expense.Expense;
import goorm.tricount.domain.settlement.dto.Transfer;
import goorm.tricount.domain.settlement.model.Settlement;
import goorm.tricount.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class BalancingTest {

    @Test
    @DisplayName("사용자 또는 지출이 하나도 없는 경우")
    void test1() {
        Settlement settlement = new Settlement(1L, "test");
//
//        User usera = new User(1L, "usera", "test", "usera");
//        User userb = new User(2L, "userb", "test", "userb");
//        User userc = new User(3L, "userc", "test", "userc");

//        List<User> testUsers = Arrays.asList(
//                usera,
//                userb,
//                userc
//        );
//
//        Expense expensea = new Expense(1L, "expensea", usera, BigDecimal.valueOf(5000), LocalDate.now());
//        Expense expenseb = new Expense(2L, "expenseb", userb, BigDecimal.valueOf(10000), LocalDate.now());
//        Expense expensec = new Expense(3L, "expensec", userc, BigDecimal.valueOf(15000), LocalDate.now());
//
//        List<Expense> testExpenses = Arrays.asList(
//                expensea,
//                expenseb,
//                expensec
//        );
//
//        settlement.getUsers().addAll(testUsers);
//        settlement.getExpenses().addAll(testExpenses);

        Assertions.assertThat(settlement.calculateBalance()).isEmpty();
    }

    @Test
    @DisplayName("모든 사용자의 지출이 동일한 경우")
    void test2() {
        Settlement settlement = new Settlement(1L, "test");

        User usera = new User(1L, "usera", "test", "usera");
        User userb = new User(2L, "userb", "test", "userb");
        User userc = new User(3L, "userc", "test", "userc");

        List<User> testUsers = Arrays.asList(
                usera,
                userb,
                userc
        );

        Expense expensea1 = new Expense(1L, "expensea", usera, BigDecimal.valueOf(3000), LocalDate.now());
        Expense expensea2 = new Expense(2L, "expensea", usera, BigDecimal.valueOf(12000), LocalDate.now());
        Expense expenseb = new Expense(3L, "expenseb", userb, BigDecimal.valueOf(15000), LocalDate.now());
        Expense expensec1 = new Expense(4L, "expensec", userc, BigDecimal.valueOf(5000), LocalDate.now());
        Expense expensec2 = new Expense(4L, "expensec", userc, BigDecimal.valueOf(10000), LocalDate.now());

        List<Expense> testExpenses = Arrays.asList(
                expensea1,
                expensea2,
                expenseb,
                expensec1,
                expensec2
        );

        settlement.getUsers().addAll(testUsers);
        settlement.getExpenses().addAll(testExpenses);

        Assertions.assertThat(settlement.calculateBalance()).isEmpty();
//        Assertions.assertThat(settlement.calculateBalance()).containsExactly(
//                new goorm.tricount.domain.settlement.dto.Transfer(1L, "usera", BigDecimal.valueOf(4000), 2L, "userb"),
//                new goorm.tricount.domain.settlement.dto.Transfer(1L, "usera", BigDecimal.valueOf(4000), 3L, "userc")
//        );
    }

    @Test
    @DisplayName("단 한 명의 사용자가 모든 지출을 한 경우")
    void test3() {
        Settlement settlement = new Settlement(1L, "test");

        User usera = new User(1L, "usera", "test", "usera");
        User userb = new User(2L, "userb", "test", "userb");
        User userc = new User(3L, "userc", "test", "userc");

        List<User> testUsers = Arrays.asList(
                usera,
                userb,
                userc
        );

        Expense expensea1 = new Expense(1L, "expensea", usera, BigDecimal.valueOf(10000), LocalDate.now());
        Expense expensea2 = new Expense(1L, "expensea", usera, BigDecimal.valueOf(10001), LocalDate.now());

        List<Expense> testExpenses = Arrays.asList(
                expensea1,
                expensea2
        );

        settlement.getUsers().addAll(testUsers);
        settlement.getExpenses().addAll(testExpenses);

//        Assertions.assertThat(settlement.calculateBalance()).isEmpty();
        Assertions.assertThat(settlement.calculateBalance()).containsExactly(
                new Transfer(3L, "userc", BigDecimal.valueOf(6667), 1L, "usera"),
                new Transfer(2L, "userb", BigDecimal.valueOf(6667), 1L, "usera")
        );
    }
}
