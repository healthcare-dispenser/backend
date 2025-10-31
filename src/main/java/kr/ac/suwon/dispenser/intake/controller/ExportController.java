package kr.ac.suwon.dispenser.intake.controller;


import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import kr.ac.suwon.dispenser.common.AccountPrincipal;
import kr.ac.suwon.dispenser.intake.dto.csv.Csv;
import kr.ac.suwon.dispenser.intake.repository.IntakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExportController {

    private final IntakeRepository repository;

    @GetMapping(value = "/api/exports/intake-feedback.csv", produces = "text/csv; charset=UTF-8")
    public void downloadCsv(HttpServletResponse resp,
                            @AuthenticationPrincipal AccountPrincipal account,
                            @RequestParam(required = false) Long profileId,
                            @RequestParam(required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                            @RequestParam(required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) throws Exception {

        // 파일 이름 인코딩
        String filename = URLEncoder.encode("intake_feedback.csv", StandardCharsets.UTF_8);
        resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);

        List<Csv> rows = repository.exportCsv(profileId, from, to);

        try (var writer = resp.getWriter()) {
            // 헤더
            writer.println(String.join(",",
                    "Intake ID","Profile ID","Command UUID","Status",
                    "Requested At","Completed At",
                    "Zinc","Melatonin","Magnesium","Electrolyte",
                    "Feedback ID","Sleep Rating","Fatigue Rating","Feedback Created At"
            ));

            // 데이터 행
            for (var r : rows) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        n(r.intakeId()),
                        n(r.profileId()),
                        csv(r.commandUuid()),
                        csv(String.valueOf(r.status())),
                        ts(r.requestedAt()),
                        ts(r.completedAt()),
                        n(r.zinc()),
                        n(r.melatonin()),
                        n(r.magnesium()),
                        n(r.electrolyte()),
                        n(r.feedbackId()),
                        n(r.sleepRating()),
                        n(r.fatigueRating()),
                        ts(r.feedbackCreatedAt())
                );
            }
        }
    }

    // ---- 간단한 CSV 헬퍼 메서드들 ----
    private String n(Number v) { return v == null ? "" : String.valueOf(v); }
    private String ts(LocalDateTime t) { return t == null ? "" : t.toString(); }

    private String csv(String s) {
        if (s == null || s.isEmpty()) return "";
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return needQuote ? "\"" + escaped + "\"" : escaped;
    }

}
