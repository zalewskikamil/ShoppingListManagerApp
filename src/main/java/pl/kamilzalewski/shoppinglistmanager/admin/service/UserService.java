package pl.kamilzalewski.shoppinglistmanager.admin.service;

public interface UserService {
    String changeUserBlockedStatus(Long userId);
    String grantAdminStatus(Long userId);
}
