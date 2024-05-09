package pl.kamilzalewski.shoppinglistmanager.share;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShareDto {

    private Integer id;
    private String userEmail;
}
