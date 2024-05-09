package pl.kamilzalewski.shoppinglistmanager.admin.service;

public interface NotificationService {
    String sendTechnicalBreakMessage(String startDateTime, String endDateTime);
}
