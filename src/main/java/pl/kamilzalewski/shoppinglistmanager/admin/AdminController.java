package pl.kamilzalewski.shoppinglistmanager.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamilzalewski.shoppinglistmanager.admin.service.LogService;
import pl.kamilzalewski.shoppinglistmanager.admin.service.NotificationService;
import pl.kamilzalewski.shoppinglistmanager.admin.service.UserService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final LogService logService;
    private final NotificationService notificationService;

    @GetMapping("/logs")
    public ResponseEntity<Resource> downloadLogFile() {
        Resource resource = logService.getLogFile();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.TRANSFER_ENCODING, "binary")
                .body(resource);
    }

    @PostMapping("/changeStatus/{userId}")
    public ResponseEntity<String> changeUserBlockedStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.changeUserBlockedStatus(userId));
    }

    @PostMapping("/getStatus/{userId}")
    public ResponseEntity<String> grantAdminStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.grantAdminStatus(userId));
    }

    @PostMapping("/technicalBreak")
    public ResponseEntity<String> sendTechnicalBreakMessage(@RequestParam String startDateTime,
                                                            @RequestParam String endDateTime) {
        return ResponseEntity.ok(notificationService.sendTechnicalBreakMessage(startDateTime, endDateTime));
    }
}
