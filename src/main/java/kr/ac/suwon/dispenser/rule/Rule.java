package kr.ac.suwon.dispenser.rule;

import java.util.Map;

public interface Rule {
    Map<String, Double> apply(RuleContext context);
}
