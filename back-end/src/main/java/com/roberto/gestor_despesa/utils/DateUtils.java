package com.roberto.gestor_despesa.utils;

import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class DateUtils {

    public void validateYearMonth(YearMonth value) {
        if(YearMonth.now().isAfter(value)) {
           throw new NotFoundException(value.getYear());
        }
    }
}
