package com.hebi;

import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.exceptions.DBusException;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ZomboidCrashMonitor {

    private static final Config config = new Config();
    private static DiscordBot discordBot;
    private static SystemdInteraction systemdInteraction;

    public static void main(String[] args) throws Exception {
        // Initialize and start the Discord bot
        discordBot = new DiscordBot(config.getDiscordBotToken(), config.getDiscordChannelId());
        discordBot.start();

        systemdInteraction = new SystemdInteraction(config.getZomboidServerServiceName());

        // Start monitoring
        ZomboidCrashMonitor monitor = new ZomboidCrashMonitor();
        monitor.startMonitoring();
    }

    public void startMonitoring() {
        while (true) {
            try {
                monitorService(config.getZomboidServerServiceName());
                systemdInteraction.checkServiceStatus();
                // Sleep for 30 seconds (or any other interval you prefer)
                TimeUnit.SECONDS.sleep(600);
            } catch (Exception e) {
                log.error("An error occurred during monitoring", e);
                discordBot.sendMessage("Error: " + e.getMessage());
            }
        }
    }

    public void monitorService(String serviceName) {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        List<OSProcess> processes = os.getProcesses();
        boolean isRunning = processes.stream()
                .anyMatch(osProcess -> osProcess.getName().contains("ProjectZomboid6"));

        if (!isRunning) {
            log.warn("{} is not running. Sending alert...", serviceName);
            discordBot.sendMessage(serviceName + " has stopped or crashed!");
        } else {
            log.info("{} is running normally.", serviceName);
        }
    }

    private void listSystemdUnits() throws DBusException {

    }
}