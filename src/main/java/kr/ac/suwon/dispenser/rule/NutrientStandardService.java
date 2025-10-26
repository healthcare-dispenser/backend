package kr.ac.suwon.dispenser.rule;

import kr.ac.suwon.dispenser.profile.domain.Gender;
import org.springframework.stereotype.Service;

@Service
public class NutrientStandardService {

    /** 멜라토닌: 권장(1회) */
    public double melatoninRecommendedDoseMg(int age) {
        if (age < 18 || age >= 65) return 0.0;   // 보수적으로 미권장
        return 1.0; // 0.5~1mg 범위에서 1mg 기본
    }

    /** 멜라토닌: 국내 건강기능식품 상한(1일) — 보수적으로 2mg */
    public double melatoninMaxPerDayMg() {
        return 2.0;
    }

    /** 마그네슘: 1회 기본 서빙(보수값). 운동/수면 태그로 약간 가감 예정 */
    public double magnesiumServingMg(Double weight, Gender gender) {
        // 가볍게 체중 보정(≈ 1.8 mg/kg, 최대 180 mg)
        if (weight == null) return 150.0;
        return Math.min(180.0, Math.max(100.0, weight * 1.8));
    }

    /** 아연: 1회 기본 서빙(보수값) */
    public double zincServingMg(Gender gender) {
        return 5.0; // RDA 대비 보수적 단회량
    }

    /** 전해질 음료: 1회 기본 서빙(ml) */
    public double electrolyteServingMl() {
        return 250.0;
    }
}
