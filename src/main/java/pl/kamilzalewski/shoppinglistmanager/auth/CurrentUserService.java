package pl.kamilzalewski.shoppinglistmanager.auth;

import pl.kamilzalewski.shoppinglistmanager.user.User;


public interface CurrentUserService {

    String getCurrentUsername();

    User getCurrentUser();
}
