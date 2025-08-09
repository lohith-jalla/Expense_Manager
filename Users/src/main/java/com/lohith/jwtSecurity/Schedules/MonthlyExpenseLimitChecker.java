package com.lohith.jwtSecurity.Schedules;


import com.lohith.jwtSecurity.OpenFeignClients.ExpenseClient;
import com.lohith.jwtSecurity.config.AuthUtil;
import com.lohith.jwtSecurity.model.User;
import com.lohith.jwtSecurity.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MonthlyExpenseLimitChecker {

    private final ExpenseClient expenseClient;
    private final JavaMailSender javaMailSender;
    private final AuthUtil authUtil;
    private final UserRepo userRepository;


//    @Scheduled(cron = "0 0 9 * * Sun")
    @Scheduled(cron = "0 * * * * *")
    public void sendLimitAlerts() {
        System.out.println("Sending limit alerts to expense");
        List<User> users = userRepository.findAll();
        int currentMonth = LocalDate.now().getMonthValue();

        for (User user : users) {
            Double limit = user.getMonthlyLimit();
            if (limit == null) continue;

            String token = authUtil.generateAccessToken(user);
            String authHeader = "Bearer " + token;

            try {
                Map<String, Object> response = expenseClient.getMonthlySummary(authHeader, 1L, 0, 5);

                List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");

                if (content != null && !content.isEmpty()) {
                    Double monthlyTotal = ((Number) content.get(0).get("total")).doubleValue();

                    if (monthlyTotal > limit &&
                            (user.getLastLimitAlertMonth() == null || user.getLastLimitAlertMonth() != currentMonth)) {

                        sendEmail(user.getEmail(), monthlyTotal, limit,user.getUsername());
                        user.setLastLimitAlertMonth(currentMonth);
                        userRepository.save(user);
                    }
                }

            } catch (Exception e) {
                System.out.println("Error checking limit for user: " + user.getUsername() + ", " + e.getMessage());
            }
        }
    }


    private void sendEmail(String to, double spent, double limit, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("⚠️ Monthly Expense Limit Exceeded");

        String body = String.format(
                "Hello %s,\n\nYou have spent ₹%.2f this month, exceeding your monthly limit of ₹%.2f.\n\nPlease check your expense dashboard for details.\n\nRegards,\nExpense Tracker",
                username, spent, limit);

        message.setText(body);
        javaMailSender.send(message);
    }


}
