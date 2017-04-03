import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LinkedIn {

    private WebDriver driver;

    public LinkedIn() {
        SetUp setUp = new SetUp();
        driver = setUp.chromeDriver();
    }

    /**
     * Orchestrates the methods to compose the correct behavior
     */
    public void executeSearch() {
        findPeopleBySkill("docker");
        Professional professional = findPersonSkills();
        if (professional != null)
            System.out.println(professional.toString());
        else
            System.out.println("No hay perfiles visibles");
        driver.close();
    }

    /**
     * Finds all people with the given skill, the results are paging
     *
     * @param skill The name of the required skill
     */
    private void findPeopleBySkill(String skill) {

        driver.get("https://www.linkedin.com/");

        //Login form
        driver.findElement(By.id("login-email")).sendKeys("jhon.dallas5898@gmail.com");
        driver.findElement(By.id("login-password")).sendKeys("pruebas12");
        driver.findElement(By.id("login-submit")).click();

        //Search skill
        driver.findElement(By.className("type-ahead-input")).findElement(By.tagName("input")).sendKeys(skill);
        driver.findElement(By.className("nav-search-button")).click();

        //Waits until the view is refreshed
        WebElement divClass = driver.findElement(By.className("authentication-outlet")).findElement(By.tagName("div"));
        WebDriverWait wait = new WebDriverWait(driver, 5L);
        wait.until(ExpectedConditions.stalenessOf(divClass));

        //Finds only the people
        driver.findElement(By.xpath("//*/div[@class='authentication-outlet']/div/ul/li[2]/button")).click();
    }

    /**
     * Finds all the skills by clicking in the view more option
     *
     * @return and object Professional who contains the skills
     */
    private Professional findPersonSkills() {
        Professional professional = new Professional();

        if (findVisibleProfile()) {
            
            //Waits until the scroll arrives to the the content because it is charged dynamically
            WebDriverWait waitLoad = new WebDriverWait(driver, 5l);
            WebElement skillsMoreButton = waitLoad.until(d ->
            {
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                jse.executeScript("window.scrollBy(0,300)", "");
                return driver.findElement(By.xpath("//*[@class='profile-detail']/div[5]/div/section/button"));
            });

            //Shows all the skills
            skillsMoreButton.click();

            WebElement username = driver.findElement(By.xpath("//*[@class='pv-top-card-section__body']/div[1]/h1"));
            professional.setUser(username.getText());
            professional.setSkill(getSkills());

            return professional;
        }
        return null;
    }

    /**
     * Some profiles are not allowed to the given user so is necessary compare the name to check if it is visible
     */
    private boolean findVisibleProfile() {
        int page = driver.findElements(By.xpath("//*[@class='page-list']/ol/li")).size();
        boolean isVisible = false;
        WebElement visiblePerson = null;
        while (page-- > 0 && !isVisible) {
            //List with the people on the view
            int peopleListSize = driver.findElements(By.xpath("//*/ul[@class='results-list']/li")).size();
            for (int i = 1; i <= peopleListSize; i++) {
                visiblePerson = driver.findElement(By.xpath("//*/ul[@class='results-list']/li[" + i + "]/div/div/div[2]/a"));
                //LinkedIn Member is the default name when the profile is no visible
                if (!"LinkedIn Member".equals(visiblePerson.getText())) {
                    isVisible = !isVisible;
                    break;
                }
            }

            //If there is a visible profile, opens the profile
            if (isVisible) {
                visiblePerson.sendKeys(Keys.ENTER);
                return true;
            } else {
                while (!scrollToNextButton()) ;
            }
        }
        return false;
    }

    /**
     * Scroll the actual page until the next button is able to click
     * @return true if it is able, false if it is not
     */
    private boolean scrollToNextButton() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0, document.body.scrollHeight)", "");
        try {
            driver.findElement(By.className("next")).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets all the skills of the actual profile, there are two types of views for the skills
     *
     * @return List with the name of all the strings of the given profile
     */
    private List<String> getSkills() {
        List<String> skills = new ArrayList<>();

        //First type
        List<WebElement> firstTypeSkills = driver.findElements(By.xpath("//*[@class='profile-detail']/div[5]/div/section/ul/li"));
        for (int i = 1; i <= firstTypeSkills.size(); i++) {
            WebElement skill = driver.findElement(By.xpath("//*[@class='profile-detail']/div[5]/div/section/ul/li["
                    + i + "]/div[@class='pv-skill-entity__header']/div/a/div/span[1]"));
            skills.add(skill.getText());
        }

        //Second type
        List<WebElement> secondTypeSkills = driver.findElements(By.xpath("//*[@id='featured-skills-expanded']/ul/li"));
        for (int i = 1; i <= secondTypeSkills.size(); i++) {
            try {
                WebElement skill = driver.findElement(By.xpath("//*[@id='featured-skills-expanded']/ul/li["
                        + i + "]/div/div/a/div/span[1]"));
                skills.add(skill.getText());
            } catch (Exception e) {
                WebElement skill = driver.findElement(By.xpath("//*[@id='featured-skills-expanded']/ul/li["
                        + i + "]/div/div/div/span"));
                skills.add(skill.getText());
            }
        }
        return skills;
    }
}
