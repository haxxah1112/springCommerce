package com.project.common.message;

import com.project.domain.users.Users;
import com.project.enums.NotificationTemplate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryCompletionNotificationMessage implements NotificationMessage {
    private Users user;
    private Long orderItemId;

    @Override
    public String getTemplate() {
        return NotificationTemplate.DELIVERY_COMPLETION_TEMPLATE.name();
    }

    @Override
    public Map<String, Object> toParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", user.getName());
        return params;
    }
}
