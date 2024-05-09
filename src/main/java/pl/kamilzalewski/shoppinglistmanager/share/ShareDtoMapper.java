package pl.kamilzalewski.shoppinglistmanager.share;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ShareDtoMapper {

    public static List<ShareDto> mapToSharesDtos(Page<Share> page) {
        List<Share> shares = page.getContent();
        List<ShareDto> shareDtos = new ArrayList<>();
        if (!shares.isEmpty()) {
            shares.forEach(s -> shareDtos.add(mapToShareDto(s)));
        }
        return shareDtos;
    }

    public static ShareDto mapToShareDto(Share share) {
        return ShareDto.builder()
                .id(share.getId())
                .userEmail(share.getUserWithShare().getUsername())
                .build();
    }
}
