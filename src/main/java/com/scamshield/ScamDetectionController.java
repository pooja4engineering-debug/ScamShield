
package com.scamshield;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScamDetectionController {

    @PostMapping("/check")
    public Map<String, Object> checkMessage(
            @RequestBody Map<String, String> request) {

        String message = request.get("message");
        Map<String, Object> result = new HashMap<>();

        if (message == null || message.isBlank()) {
            result.put("error", "Message cannot be empty");
            return result;
        }

        String text = message.toLowerCase();
        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (text.contains("bank details")
                || text.contains("password")
                || text.contains("otp")) {
            score += 40;
            reasons.add("Requests sensitive information");
        }

        if (text.contains("won a prize")
                || text.contains("lottery")
                || text.contains("claim your reward")) {
            score += 30;
            reasons.add("Contains a suspicious prize or reward claim");
        }

        if (text.contains("urgent")
                || text.contains("immediately")
                || text.contains("act now")) {
            score += 20;
            reasons.add("Uses urgency to pressure the recipient");
        }

        if (text.contains("click this link")
                || text.contains("click here")) {
            score += 20;
            reasons.add("Encourages clicking a link");
        }

        score = Math.min(score, 100);

        String riskLevel;

        if (score >= 60) {
            riskLevel = "HIGH";
        } else if (score >= 30) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        result.put("riskScore", score);
        result.put("riskLevel", riskLevel);
        result.put("reasons", reasons);

        return result;
    }
}
