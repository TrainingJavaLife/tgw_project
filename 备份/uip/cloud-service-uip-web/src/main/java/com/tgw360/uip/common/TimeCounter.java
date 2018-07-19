package com.tgw360.uip.common;

import org.apache.log4j.Logger;

/**
 * 计时器，用来计算一些操作花费的时间。
 *
 * @author 邹祥
 * @date 2017年6月2日 上午9:19:05
 */
public class TimeCounter {
    private final long first; // 记录计时器对象被创建的时间
    private long prev; // 记录上一次计时的时间。
    private static Logger log = Logger.getLogger(TimeCounter.class);

    public TimeCounter() {
        this.first = System.currentTimeMillis();
        this.prev = first;
    }

    /**
     * @see #count(String)
     */
    public void count() {
        count("耗时： %s");
    }

    /**
     * 统计此方法被调用的时刻，到上一次此方法被调用的时刻（如果是第一次调用，则为计时器被创建的时刻)，总共的耗时
     *
     * @param info 输出格式
     * @return
     */
    public long count(String info) {
        long now = System.currentTimeMillis();
        long time = now - prev;
        prev = now; // 更新prev
        System.out.printf(String.format(info, time));
//		log.info(String.format(info, time));
        return time;
    }

    /**
     * 统计此方法被调用的时刻到计时器被创建的时刻，总共的耗时。
     *
     * @return
     */
    public long countAll() {
        long now = System.currentTimeMillis();
        System.out.printf("总耗时： %s\n", now - first);
//        log.info("总耗时： " + (now - first));
        return now - first;
    }
}
