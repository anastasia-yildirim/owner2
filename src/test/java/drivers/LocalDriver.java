package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.AppConfig;
import config.DeviceConfig;
import config.TestEnvConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static io.appium.java_client.remote.MobilePlatform.ANDROID;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

public class LocalDriver implements WebDriverProvider {

    private static final TestEnvConfig testEnvConfig = ConfigFactory.create(TestEnvConfig.class, System.getProperties());
    public static final DeviceConfig deviceConfig = ConfigFactory.create(DeviceConfig.class, System.getProperties());
    public static final AppConfig appConfig = ConfigFactory.create(AppConfig.class, System.getProperties());

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();

        options.setAutomationName(ANDROID_UIAUTOMATOR2)
                .setPlatformName(ANDROID)
                .setPlatformVersion(deviceConfig.getPlatformVersion())
                .setDeviceName(deviceConfig.getDeviceName())
                .setApp(getAppPath())
                .setAppPackage(appConfig.getAppPackage())
                .setAppActivity(appConfig.getAppActivity());

        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    public static URL getAppiumServerUrl() {
        try {
            return new URL(testEnvConfig.getRemoteUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAppPath() {
        String appVersion = appConfig.getAppVersion();
        String appUrl = appConfig.getAppUrl() + appVersion;
        String appPath = appConfig.getAppPath() + appVersion;

        File app = new File(appPath);
        if (!app.exists()) {
            try (InputStream in = new URL(appUrl).openStream()) {
                copyInputStreamToFile(in, app);
            } catch (IOException e) {
                throw new AssertionError("Failed to download application", e);
            }
        }
        return app.getAbsolutePath();
    }
}