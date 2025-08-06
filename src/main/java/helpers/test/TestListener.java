package helpers.test;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("STARTING TEST: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("TEST PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("TEST FAILED: {}", result.getMethod().getMethodName(), result.getThrowable());
        attachLogFile();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("TEST SKIPPED: {}", result.getMethod().getMethodName());
    }

    @Attachment(value = "Test Log", type = "text/plain")
    public byte[] attachLogFile() {
        try {
            return Files.readAllBytes(Paths.get("logs/test.log"));
        } catch (IOException e) {
            logger.error("Could not read log file to attach", e);
            return "Failed to read log file.".getBytes();
        }
    }
}