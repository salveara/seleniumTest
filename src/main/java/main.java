import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;

public class main {
    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            driver = setUp();
            linkedinDockerPeople(driver);
            personSkills(driver);
        } finally {
            if (driver != null) {
                //driver.quit();
            }
        }
    }

    private static void linkedinDockerPeople(final WebDriver driver) {
        driver.get("https://www.linkedin.com/");
        WebElement email = driver.findElement(By.id("login-email"));
        email.sendKeys("jhon.dallas5898@gmail.com");
        WebElement password = driver.findElement(By.id("login-password"));
        password.sendKeys("pruebas12");
        WebElement submit = driver.findElement(By.id("login-submit"));
        submit.click();
        WebElement searchInput = driver.findElement(By.className("type-ahead-input")).findElement(By.tagName("input"));
        searchInput.sendKeys("Docker");
        WebElement searchButton = driver.findElement(By.className("nav-search-button"));
        searchButton.click();
        WebElement divClass = driver.findElement(By.className("authentication-outlet")).findElement(By.tagName("div"));
        WebDriverWait wait = new WebDriverWait(driver, 5L);
        wait.until(ExpectedConditions.stalenessOf(divClass));
        driver.findElement(By.xpath("//*/div[@class='authentication-outlet']/div/ul/li[2]/button")).click();
    }

    private static void personSkills(WebDriver driver) {
        List<WebElement> peopleList = driver.findElements(By.xpath("//*/ul[@class='results-list']/li"));
        WebElement visiblePerson = null;
        for (int i = 1; i <= peopleList.size(); i++) {
            WebElement person = peopleList.get(i);
            WebElement aux = person.findElement(By.xpath("//*/ul[@class='results-list']/li[" + i + "]/div/div/div[2]/a"));
            if (!aux.getText().equals("LinkedIn Member")) {
                visiblePerson = aux;
                break;
            }
        }
        if (visiblePerson != null) {
            visiblePerson.sendKeys(Keys.ENTER);
        }
        WebDriverWait waitLoad = new WebDriverWait(driver, 15L);
        WebElement skillsMoreButton = waitLoad.until(d ->
        {
            JavascriptExecutor jse = (JavascriptExecutor)driver;
            jse.executeScript("window.scrollBy(0,250)", "");
            return driver.findElement(By.xpath("//*[@class='profile-detail']/div[5]/div/section/button"));
        });
        skillsMoreButton.click();
        List<WebElement> firstTypeSkills = driver.findElements(By.xpath("//*[@class='profile-detail']/div[5]/div/section/ul/li"));
        for (int i = 1; i <= firstTypeSkills.size(); i++) {
            WebElement skill = driver.findElement(By.xpath("//*[@class='profile-detail']/div[5]/div/section/ul/li["
                    + i + "]/div[@class='pv-skill-entity__header']/div/a/div/span[1]"));
            System.out.println(skill.getText());
        }
        List<WebElement> secondTypeSkills = driver.findElements(By.xpath("//*[@id='featured-skills-expanded']/ul/li"));
        for (int i = 1; i <= secondTypeSkills.size(); i++) {
            try {
                WebElement skill = driver.findElement(By.xpath("//*[@id='featured-skills-expanded']/ul/li["
                        + i + "]/div/div/a/div/span[1]"));
                System.out.println(skill.getText());
            } catch (Exception e){
                WebElement skill = driver.findElement(By.xpath("//*[@id='featured-skills-expanded']/ul/li["
                        + i + "]/div/div/div/span"));
                System.out.println(skill.getText());
            }
        }
    }

    private static WebDriver setUp() {
        //TODO you have to download the crhomedriver and copy the route in order to execute it
        System.setProperty("webdriver.chrome.driver", "C:/Users/salveara/Documents/Programs/chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        capabilities.setCapability("chrome.binary", "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver(capabilities);
    }

}
