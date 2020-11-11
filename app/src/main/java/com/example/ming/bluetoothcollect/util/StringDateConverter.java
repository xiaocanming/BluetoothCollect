package com.example.ming.bluetoothcollect.util;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StringDateConverter implements PropertyConverter<Date, String> {
    private final static List<String> FORMATS = Arrays.asList(
            "yyyy-MM-dd HH:mm:ss"
    );

    @Override
    public Date convertToEntityProperty(String databaseValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(databaseValue);
        } catch (ParseException e) {

        }
        if (date == null) {
            date = new Date(0);
        }
        return date;
    }

    @Override
    public String convertToDatabaseValue(Date entityProperty) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(entityProperty);
    }
}
