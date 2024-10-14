package com.hebi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@AllArgsConstructor
@Getter
@Slf4j
public class SystemdInteraction {

    private final String serviceName;

    // Check the service status
    public void checkServiceStatus() {
        try {
            ProcessBuilder builder = new ProcessBuilder("systemctl", "list-units", "--type=service", "--all");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }
    }

    // Tail the service logs
    public void tailLogs() {
        try {
            ProcessBuilder builder = new ProcessBuilder("journalctl", "-u", serviceName, "-f");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                // Here you can add crash detection logic
                // For example, if you find a line that indicates a crash, trigger your bot's alert
            }
            process.waitFor();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public boolean isCrashed() {
        return false;
    }

}
