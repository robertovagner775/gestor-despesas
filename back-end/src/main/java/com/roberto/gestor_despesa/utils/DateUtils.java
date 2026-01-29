package com.roberto.gestor_despesa.utils;

import com.roberto.gestor_despesa.handler.exceptions.DateInvalidException;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class DateUtils {

    public void validateYearMonth(YearMonth value) {
        if(YearMonth.now().isAfter(value)) {
           throw new DateInvalidException("O mes e o ano tem que ser depois do mes atual");
        }
    }
}
