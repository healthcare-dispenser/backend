package kr.ac.suwon.dispenser.profile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagCode code;
    private String label;

    @Builder(access = AccessLevel.PRIVATE)
    public Tag(TagCode code, String label) {
        this.code = code;
        this.label = label;
    }

    public static Tag create(TagCode code, String label) {
        return Tag.builder()
                .code(code)
                .label(label).build();
    }
}
