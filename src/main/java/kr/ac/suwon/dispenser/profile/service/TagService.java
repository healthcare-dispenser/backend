package kr.ac.suwon.dispenser.profile.service;

import kr.ac.suwon.dispenser.profile.domain.tag.Tag;
import kr.ac.suwon.dispenser.profile.domain.tag.TagCode;
import kr.ac.suwon.dispenser.profile.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public Tag findByCode(TagCode tagCode) {
        return tagRepository.findByCode(tagCode).orElseThrow(() -> new RuntimeException("존재하지 않는 태그 입니다."));
    }
}
