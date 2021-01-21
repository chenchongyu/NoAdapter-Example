package com.baidu.adt.hmi.taxihailingandroid;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baidu.adt.hmi.taxihailingandroid.cron.CronParser;
import com.baidu.adt.hmi.taxihailingandroid.utils.Util;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        System.out.println(CronParser.parse("6+ 10+ 2020 *",new Date()));
//        System.out.println(CronParser.parse("* * * 5-7",new Date()));
//        System.out.println(CronParser.parse("* * * 4",new Date()));
//        System.out.println(CronParser.parse("5 11 2020 *",new Date()));

        Map<String, String> params = new HashMap<>();
        params.put("time", "11111");
        params.put("addd", "2222");
        System.out.println(Util.appendParams("www.baidu.com", params));

        assertEquals(4, 2 + 2);
    }
}