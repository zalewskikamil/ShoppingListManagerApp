package pl.kamilzalewski.shoppinglistmanager.email;

import lombok.Builder;

@Builder
public record EmailBody(String to, String subject, String text) {
}
