package pl.kamilzalewski.shoppinglistmanager.password.change;

public interface ChangePasswordService {

    String changePassword(ChangePassword changePassword);

    String changeForgottenPassword(String forgotPasswordToken, ChangeForgottenPassword changeForgottenPassword);
}
