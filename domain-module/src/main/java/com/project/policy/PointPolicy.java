package com.project.policy;

import org.springframework.stereotype.Component;

@Component
public class PointPolicy {
    public int calculatePoints(int amount) {
        return (int) (amount * 0.05);
    }

}
