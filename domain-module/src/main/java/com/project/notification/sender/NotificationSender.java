package com.project.notification.sender;


import java.util.List;
import java.util.Map;

public interface NotificationSender {
    void send(String phone, Map<String, Object> params);
    void sendAll(List<String> phones, Map<String, Object> params);
}
