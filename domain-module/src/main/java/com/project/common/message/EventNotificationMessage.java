package com.project.common.message;

import com.project.enums.NotificationTemplate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventNotificationMessage implements NotificationMessage {
    private List<String> phones;
    private String eventName;
    private String content;

    @Override
    public String getTemplate() {
        return NotificationTemplate.EVENT_TEMPLATE.name();
    }

    @Override
    public Map<String, Object> toParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("eventName", eventName);
        params.put("content", content);
        return params;
    }
}
