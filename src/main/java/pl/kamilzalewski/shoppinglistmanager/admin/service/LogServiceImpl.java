package pl.kamilzalewski.shoppinglistmanager.admin.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.admin.LogFileNotFoundException;

import java.io.File;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);

    private static final String LOG_FILE_PATH = "logs/app.log";

    @Override
    public Resource getLogFile() {
        File file = new File(LOG_FILE_PATH);
        if (!file.exists()) {
            throw new LogFileNotFoundException("Log file not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminName = authentication.getName();
        LOGGER.info("The admin {} has downloaded the log file", adminName);
        return new FileSystemResource(file);
    }
}
