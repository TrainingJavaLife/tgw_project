package com.tgw360.management;


import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class SocketSessionRegistry {

    //集合存储所有的session，Key：用户ID，Value：会话ID
    private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap();
    private final Object lock = new Object();

    public SocketSessionRegistry() {
    }

    /** 获取会话Ids */
    public Set<String> getSessionIds(String user) {
        Set set = (Set)this.userSessionIds.get(user);
        return set != null?set: Collections.emptySet();
    }

    /** 获取所有会话 */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    /** 注册会话 */
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User 不能为空");
        Assert.notNull(sessionId, "Session ID 不能为空");
        synchronized(this.lock) {
            Object set = (Set)this.userSessionIds.get(user);
            if(set == null) {
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(user, (Set<String>) set);
            }else {
                this.userSessionIds.remove(user);
                // 当想要修改时，把内容Copy出去然后再改,可以对CopyOnWrite容器进行并发的读，而不需要加锁
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(user,(Set<String>)set);
            }

            ((Set)set).add(sessionId);
        }
    }
    // 移除会话
    public void unregisterSessionId(String userId, String sessionId) {
        Assert.notNull(userId, "User Id 不能为空");
        Assert.notNull(sessionId, "Session ID 不能为空");
        synchronized(this.lock) {
            Set set = (Set)this.userSessionIds.get(userId);
            if(set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(userId);
            }

        }
    }
}
