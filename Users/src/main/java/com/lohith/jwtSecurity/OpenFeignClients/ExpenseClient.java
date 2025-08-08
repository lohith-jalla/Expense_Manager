package com.lohith.jwtSecurity.OpenFeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Expense",configuration = FeignClientConfig.class)
@Component
public interface ExpenseClient {

    @GetMapping("/expense/progress")
    Map<String, Object> getMonthlySummary(
            @RequestHeader("Authorization") String header,
            @RequestParam("period") Long period,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "0") int size
    );
}
