package goorm.tricount.domain.settlement.model;

import goorm.tricount.common.Entity;
import goorm.tricount.domain.expense.Expense;
import goorm.tricount.domain.settlement.dto.Transfer;
import goorm.tricount.domain.user.User;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Getter
//Entity JDBC
//
public class Settlement extends Entity {

    private String title;

    private List<User> users = new ArrayList<>();

    private User creator;

    private List<Expense> expenses = new ArrayList<>();

    public Settlement(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Settlement(String title) {
        this.title = title;
    }

    private BigDecimal getTotalExpenseAmount() {
        return expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Data
    private static class UserTransferAmount implements Comparable<UserTransferAmount>{
        private User user;
        private BigDecimal expenseAmount;

        public UserTransferAmount(User user, BigDecimal expenseAmount) {
            this.user = user;
            this.expenseAmount = expenseAmount;
        }

        @Override
        public int compareTo(UserTransferAmount o) {
            return o.expenseAmount.compareTo(this.expenseAmount);
        }
    }

    public List<Transfer> calculateBalance() {
        if(users.isEmpty())
            return Collections.emptyList();

        //평균 금액 계산
        BigDecimal totalAmount = getTotalExpenseAmount();
        BigDecimal averageAmount = totalAmount.divide(BigDecimal.valueOf(users.size()), RoundingMode.HALF_UP);

        //유저별 지출액
        Map<User, BigDecimal> userExpenseAmount = calculateUserExpense();

        //유저별 송금 받아야하는 금액 계산 및 정렬
        List<UserTransferAmount> userTransferAmounts = userExpenseAmount.entrySet().stream()
                .map(e -> new UserTransferAmount(e.getKey(), e.getValue().subtract(averageAmount)))
                .sorted().toList();

        //transfer 생성
        List<Transfer> transfers = new ArrayList<>();
        int left = 0, right = userTransferAmounts.size() - 1;

        while (left < right) {
            UserTransferAmount leftUserTransferAmount = userTransferAmounts.get(left);
            User leftUser = leftUserTransferAmount.getUser();
            BigDecimal leftAmount = leftUserTransferAmount.getExpenseAmount();

            UserTransferAmount rightUserTransferAmount = userTransferAmounts.get(right);
            User rightUser = rightUserTransferAmount.getUser();
            BigDecimal rightAmount = rightUserTransferAmount.getExpenseAmount();

            if (leftAmount.compareTo(BigDecimal.ZERO) == 0) {
                left++;
                continue;
            } else if (rightAmount.compareTo(BigDecimal.ZERO) == 0) {
                right--;
                continue;
            }

            //우측의 abs가 더 크다면
            if (leftAmount.compareTo(rightAmount.abs()) < 0) {
                transfers.add(new Transfer(
                        rightUser.getId(), rightUser.getNickname(),
                        leftAmount,
                        leftUser.getId(), leftUser.getNickname()
                ));
                rightUserTransferAmount.setExpenseAmount(rightAmount.add(leftAmount));
                leftUserTransferAmount.setExpenseAmount(BigDecimal.ZERO);
                left++;
            } else if (leftAmount.equals(rightAmount)) { //그대여 나와 같다면
                transfers.add(new Transfer(
                        rightUser.getId(), rightUser.getNickname(),
                        leftAmount,
                        leftUser.getId(), leftUser.getNickname()
                ));
                leftUserTransferAmount.setExpenseAmount(BigDecimal.ZERO);
                rightUserTransferAmount.setExpenseAmount(BigDecimal.ZERO);
                left++;
                right--;
            } else {
                transfers.add(new Transfer(
                        rightUser.getId(), rightUser.getNickname(),
                        rightAmount.negate(),
                        leftUser.getId(), leftUser.getNickname()
                ));
                leftUserTransferAmount.setExpenseAmount(leftAmount.add(rightAmount));
                rightUserTransferAmount.setExpenseAmount(BigDecimal.ZERO);
                right--;
            }
        }

        return transfers;
    }

    private Map<User, BigDecimal> calculateUserExpense() {

        Map<User, BigDecimal> userExpenses = this.expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getUser,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add)
                ));

        users.forEach(
                user -> userExpenses.putIfAbsent(user, BigDecimal.ZERO)
        );

        return userExpenses;
    }
}
