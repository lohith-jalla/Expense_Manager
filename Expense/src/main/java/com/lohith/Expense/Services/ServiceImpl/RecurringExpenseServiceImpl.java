package com.lohith.Expense.Services.ServiceImpl;

import com.lohith.Expense.Dto.RecurringExpenseRequestDto;
import com.lohith.Expense.Model.RecurringExpense;
import com.lohith.Expense.OpenFeignClients.UserClient;
import com.lohith.Expense.Repo.RecurringExpenseRepo;
import com.lohith.Expense.Services.RecurringExpenseServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecurringExpenseServiceImpl implements RecurringExpenseServices {

    private final RecurringExpenseRepo recurringRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;

    public RecurringExpense create(Long userId, RecurringExpenseRequestDto dto) {
        RecurringExpense exp = RecurringExpense.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .frequency(dto.getFrequency())
                .startDate(dto.getStartDate())
                .status(dto.getStatus())
                .type(dto.getType())
                .paymentType(dto.getPaymentType())
                .userId(userId)
                .build();
        return recurringRepo.save(exp);
    }

    public RecurringExpense update(Long expenseId,RecurringExpenseRequestDto expense) {
        RecurringExpense rexpense= recurringRepo.findById(expenseId).orElse(null);

        if(rexpense==null){
            return null;
        }
        rexpense.setName(expense.getName());
        rexpense.setDescription(expense.getDescription());
        rexpense.setAmount(expense.getAmount());
        rexpense.setFrequency(expense.getFrequency());
        rexpense.setPaymentType(expense.getPaymentType());
        rexpense.setType(expense.getType());
        rexpense.setStatus(expense.getStatus());
        rexpense.setStartDate(expense.getStartDate());

        return recurringRepo.save(rexpense);
    }

    public Page<RecurringExpense> getAllForUser(Long userId, Pageable pageable) {
        return recurringRepo.findAllByUserId(userId,pageable);
    }

    public RecurringExpense getById(Long expenseId) {
        return recurringRepo.findById(expenseId).orElse(null);
    }

    public boolean deleteWithPassword(Long recurringId, Long userId, String password , String token) {
        String storedHashedPassword = userClient.getHashedPassword("Bearer "+token,userId);
        System.out.println(storedHashedPassword+"---"+passwordEncoder.encode(password)+"_----"+password);
        if (passwordEncoder.matches(password, storedHashedPassword)) {
            recurringRepo.deleteById(recurringId);
            return true;
        }
        return false;
    }
}
