package LUMA_Website.utilities.extent_report;

import LUMA_Website.driver.WebDriverSingleton;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Screenshot {
    private static WebDriverSingleton webDriver = WebDriverSingleton.getDriverSingleton();
    private static final String imageEncode = "data:image/png;base64,";

    public static String getScreenShot() {
        String base64Screenshot =  imageEncode + takeScreenShot();
        return base64Screenshot;
    }
    public static String takeScreenShot() {
        return ((TakesScreenshot) webDriver.getWebDriver()).getScreenshotAs(OutputType.BASE64);
    }

}