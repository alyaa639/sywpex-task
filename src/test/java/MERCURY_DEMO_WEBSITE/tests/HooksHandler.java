package MERCURY_DEMO_WEBSITE.tests;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import MERCURY_DEMO_WEBSITE.driver.WebDriverSingleton;
import MERCURY_DEMO_WEBSITE.properties_reading.ReadPropertiesFile;
import MERCURY_DEMO_WEBSITE.utilities.extent_report.ExtentReport;
import MERCURY_DEMO_WEBSITE.utilities.extent_report.Screenshot;

import java.io.IOException;

public class HooksHandler extends BaseTest {
    String LumaWebsiteLink;

    @Before(order = 1)
    public void setUpLUMALink() throws IOException {
        edgeDriver = WebDriverSingleton.getDriverSingleton();
        linksProperties = ReadPropertiesFile.setUrlsProperties();
        LumaWebsiteLink = linksProperties.getProperty("LumaWebsiteLink");
        edgeDriver.navigateTo(LumaWebsiteLink);

        WebDriverSingleton.maximizeTheWindow();
    }
    @Before(value = "@Luma", order = 1)
    public void startTCHooks(Scenario scenario) {
        ExtentReport.setScenario(scenario);
        ExtentReport.startTC();
    }
    @Before(value = "@Luma", order = 2)
    public void setStepDefs() throws NoSuchFieldException, IllegalAccessException {
        ExtentReport.setStepDefs();
    }
    @AfterStep(value = "@Luma")
    public void
    afterStep(Scenario scenario) {
        String stepName = ExtentReport.getCurrentStepName();
        Status logStatus;

        if (scenario.isFailed()) {
            logStatus = Status.FAIL;
            String base64Screenshot = Screenshot.getScreenShot();
            ExtentReport.getTest().log(logStatus, stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        else {
            logStatus = Status.PASS;
            ExtentReport.getTest().log(logStatus,stepName);
        }
    }
    @After(value = "@Luma", order = 1)
    public void endTC() {
        if (ExtentReport.isCurrentlyUsingReport()) {
            ExtentReport.getExtent().flush();
        }
    }

    @After(value = "@Luma", order = 0)
    public void tearDownSiebel() throws InterruptedException {
        edgeDriver.resetCache();
        closeWebDriverAfterAllTestsHook();
        edgeDriver.close();
        edgeDriver.quit() ;


    }

    private void closeWebDriverAfterAllTestsHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> { WebDriverSingleton.close(); }));
    }
}
