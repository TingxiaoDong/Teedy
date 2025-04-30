package com.sismics.util;  

import org.junit.Test;  

import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Locale;  

import static org.junit.Assert.*;  

public class TestHttpUtil {  

    @Test  
    public void testBuildExpiresHeader_formatAndTime() throws ParseException {  
        long oneHourMs = 3600_000L;  

        String expiresHeader = HttpUtil.buildExpiresHeader(oneHourMs);  

        System.out.println("Expires header: " + expiresHeader);  

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);  
        Date parsedDate = sdf.parse(expiresHeader);  
        long parsedTime = parsedDate.getTime();  

        long now = System.currentTimeMillis();  

        assertTrue("Expired time should be about now + oneHourMs",   
            Math.abs(parsedTime - (now + oneHourMs)) < 1000);  
    }  

    @Test  
    public void testBuildExpiresHeader_zeroInterval() throws ParseException {  
        String expiresHeader = HttpUtil.buildExpiresHeader(0);  

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);  
        Date parsedDate = sdf.parse(expiresHeader);  
        long parsedTime = parsedDate.getTime();  

        long now = System.currentTimeMillis();  

        assertTrue("Expired time should be about now",   
            Math.abs(parsedTime - now) < 1000);  
    }  

    @Test  
    public void testBuildExpiresHeader_negativeInterval() throws ParseException {  
        long negativeInterval = -3600_000L;  

        String expiresHeader = HttpUtil.buildExpiresHeader(negativeInterval);  

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);  
        Date parsedDate = sdf.parse(expiresHeader);  
        long parsedTime = parsedDate.getTime();  

        long expectedTime = System.currentTimeMillis() + negativeInterval;  

        assertTrue("Expired time should be about now - 1 hour",   
            Math.abs(parsedTime - expectedTime) < 1000);  
    }  
}  