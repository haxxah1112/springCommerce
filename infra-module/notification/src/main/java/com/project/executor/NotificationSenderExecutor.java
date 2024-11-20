package com.project.executor;

import com.project.notification.sender.NotificationSender;

import java.util.List;
import java.util.Map;


public class NotificationSenderExecutor implements NotificationSender {
    @Override
    public void send(String phone, Map<String, Object> params) {
        // TODO: 단건 알림 외부 api
    }

    @Override
    public void sendAll(List<String> phones, Map<String, Object> params) {
        // TODO: 대량 알림 외부 api
    }
}
