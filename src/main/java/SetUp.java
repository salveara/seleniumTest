import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SetUp {

    /**
     * This method implements the required configurations to executeSearch the driver in the chrome browser
     * @return the configured webdriver for chrome
     */
    public  WebDriver chromeDriver() {
        // You have to download the chromedriver and copy the route to executeSearch it
        // https://chromedriver.storage.googleapis.com/index.html?path=2.28/
        System.setProperty("webdriver.chrome.driver", "C:/Users/salveara/Documents/Programs/chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        capabilities.setCapability("chrome.binary", "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver(capabilities);
    }
}
