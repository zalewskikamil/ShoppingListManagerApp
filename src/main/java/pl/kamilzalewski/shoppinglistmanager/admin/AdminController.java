package pl.kamilzalewski.shoppinglistmanager.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
class AdminController {

    private final AdminService adminService;

    @GetMapping("/logs")
    public ResponseEntity<Resource> downloadLogFile() {
        Resource resource = adminService.getLogFile();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }

    @PostMapping("/changeBlockedStatus/{userId}")
    public ResponseEntity<String> changeBlockedStatusHandler(@PathVariable Integer userId) {
        return new ResponseEntity<>(adminService.changeBlockedStatus(userId), HttpStatus.OK);
    }

    @PostMapping("/getAdminStatus/{userId}")
    public ResponseEntity<String> giveAdminStatusToUserHandler(@PathVariable Integer userId) {
        return new ResponseEntity<>(adminService.giveAdminStatusToUser(userId), HttpStatus.OK);
    }

    @PostMapping("/sendBreakMessage")
    public ResponseEntity<String> sendTechnicalBreakMessageHandler(@RequestParam("startDate") String startDate,
                                                                   @RequestParam("endDate") String endDate) {
        return ResponseEntity.ok(adminService.sendTechnicalBreakMessage(startDate, endDate));
    }
}
