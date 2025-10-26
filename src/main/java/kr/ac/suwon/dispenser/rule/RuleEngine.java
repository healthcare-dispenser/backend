package kr.ac.suwon.dispenser.rule;

import java.util.Collections;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static kr.ac.suwon.dispenser.profile.domain.condition.ConditionCode.*;
import static kr.ac.suwon.dispenser.profile.domain.tag.TagCode.*;

@Service
@RequiredArgsConstructor
public class RuleEngine {

    private static final String MELATONIN   = "MELATONIN";   // mg
    private static final String MAGNESIUM   = "MAGNESIUM";   // mg
    private static final String ZINC        = "ZINC";        // mg
    private static final String ELECTROLYTE = "ELECTROLYTE"; // ml

    private final NutrientStandardService std;

    private final List<Rule> rules = List.of(
            melatoninRule(),
            magnesiumRule(),
            zincRule(),
            electrolyteRule()
    );

    public Map<String, Double> run(RuleContext ctx) {
        Map<String, Double> out = new HashMap<>();
        for (Rule r : rules) {
            r.apply(ctx).forEach((k, v) -> out.merge(k, v, Double::sum));
        }
        capMelatonin(out);
        return Collections.unmodifiableMap(out);
    }

    // ----------------- Rules -----------------

    /** 🌙 멜라토닌: 밤(21:00~02:59)+SLEEP, 금기(PREGNANT/LIVER/CARDIO) 제외, 18~64세만.
     *  ☕ CAFFEINE 있으면 억제(0.5x) */
    private Rule melatoninRule() {
        return ctx -> {
            Integer age = nvl(ctx.age(), 0);
            if (age < 18 || age >= 65) return Map.of();

            LocalTime now = LocalTime.now();
            boolean isNight = !now.isBefore(LocalTime.of(21, 0)) || !now.isAfter(LocalTime.of(2, 59));
            if (!isNight) return Map.of();

            if (!has(ctx.tags(), SLEEP)) return Map.of();

            if (has(ctx.conditions(), PREGNANT) ||
                    has(ctx.conditions(), LIVER_DISEASE) ||
                    has(ctx.conditions(), CARDIOVASCULAR)) {
                return Map.of();
            }

            double dose = std.melatoninRecommendedDoseMg(age); // 기본 1mg
            if (has(ctx.tags(), CAFFEINE)) dose *= 0.5; // 카페인 섭취 시 억제
            if (dose <= 0) return Map.of();
            return Map.of(MELATONIN, round1(dose));
        };
    }

    /** Mg: 기본치에서 생활/질환 조건 보정
     *  🍺 ALCOHOL +10%, ☕ CAFFEINE +10%, 🏃 EXERCISE +15%, 🌙 SLEEP -10%(야간 위장부담↓),
     *  KIDNEY -50%, PREGNANT -15%  → 80~200mg 클램프 */
    private Rule magnesiumRule() {
        return ctx -> {
            double dose = std.magnesiumServingMg(ctx.weight(), ctx.gender());
            if (dose <= 0) return Map.of();

            if (has(ctx.tags(), ALCOHOL))   dose *= 1.10;
            if (has(ctx.tags(), CAFFEINE))  dose *= 1.10;
            if (has(ctx.tags(), EXERCISE))  dose *= 1.15;
            if (has(ctx.tags(), SLEEP))     dose *= 0.90;

            if (has(ctx.conditions(), KIDNEY_DISEASE)) dose *= 0.50;
            if (has(ctx.conditions(), PREGNANT))       dose *= 0.85;

            dose = clamp(dose, 80.0, 200.0);
            return Map.of(MAGNESIUM, round1(dose));
        };
    }

    /** Zn: 기본치에서 보정
     *  🍺 ALCOHOL +20%, 🥗 VEGAN +15%, LIVER -20% → 3~10mg 클램프 */
    private Rule zincRule() {
        return ctx -> {
            double dose = std.zincServingMg(ctx.gender());
            if (dose <= 0) return Map.of();

            if (has(ctx.tags(), ALCOHOL)) dose *= 1.20;
            if (has(ctx.tags(), VEGAN))   dose *= 1.15;

            if (has(ctx.conditions(), LIVER_DISEASE)) dose *= 0.80;

            dose = clamp(dose, 3.0, 10.0);
            return Map.of(ZINC, round1(dose));
        };
    }

    /** 전해질: 기본치에서 보정
     *  🏃 EXERCISE +20%, 🍜 SALTY_FOOD -25%,
     *  CARDIO/KIDNEY → 낮 제한(미배출), 밤만 허용 +30%감량 → 120~300ml 클램프 */
    private Rule electrolyteRule() {
        return ctx -> {
            double vol = std.electrolyteServingMl();
            if (vol <= 0) return Map.of();

            boolean cardio = has(ctx.conditions(), CARDIOVASCULAR);
            boolean kidney = has(ctx.conditions(), KIDNEY_DISEASE);

            if (has(ctx.tags(), EXERCISE)) vol *= 1.20;
            if (has(ctx.tags(), SALTY_FOOD)) vol *= 0.75;

            if (cardio || kidney) {
                LocalTime now = LocalTime.now();
                boolean isNight = !now.isBefore(LocalTime.of(21, 0)) || !now.isAfter(LocalTime.of(2, 59));
                if (!isNight) return Map.of(); // 낮에는 미배출
                vol *= 0.70; // 야간에도 감량
            }

            vol = clamp(vol, 120.0, 300.0);
            return Map.of(ELECTROLYTE, round1(vol));
        };
    }

    // ----------------- Post process & utils -----------------

    private void capMelatonin(Map<String, Double> out) {
        if (!out.containsKey(MELATONIN)) return;
        out.put(MELATONIN, Math.min(out.get(MELATONIN), std.melatoninMaxPerDayMg()));
    }

    private static boolean has(Set<?> set, Object v) { return set != null && set.contains(v); }
    private static <T> T nvl(T v, T d) { return v == null ? d : v; }
    private static double clamp(double v, double min, double max) { return Math.max(min, Math.min(max, v)); }
    private static double round1(double v) { return Math.round(v * 10.0) / 10.0; }
}