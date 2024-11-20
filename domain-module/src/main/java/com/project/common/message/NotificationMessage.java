package com.project.common.message;


import java.util.Map;


public interface NotificationMessage {
    String getTemplate();

    Map<String, Object> toParams();

}
