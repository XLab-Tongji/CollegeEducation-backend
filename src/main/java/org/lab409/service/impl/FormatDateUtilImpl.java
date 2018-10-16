package org.lab409.service.impl;

import org.lab409.util.FormatDateUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FormatDateUtilImpl implements FormatDateUtil {
    @Override
    public String formatDate(String formatString, Date date) {
        SimpleDateFormat ft = new SimpleDateFormat(formatString);
        return ft.format(date);
    }

    @Override
    public String formatDate(Date date) {
        return this.formatDate("yyyy-MM-dd hh::mm:ss", date);
    }
}
