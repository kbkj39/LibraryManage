package com.ksamar.library.controller;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Convert;
import javax.persistence.Column;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// 属性转换器类
@Converter
public class DateToStringConverter implements AttributeConverter<Date, String> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }

    @Override
    public Date convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? dateFormat.parse(dbData) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}