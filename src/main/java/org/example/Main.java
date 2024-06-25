package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public  class Main {
    public static WebDriver browser;

    public static Wait<WebDriver> wait;

    public static final String IDENTIFICATION = "Password345?";

    public static final String MAIN_URL = "http://localhost:8080/";

    public static void setup() { //konfiguruoja narsykle-ja paleidzia

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--ignore-certificate-errors");

        browser = new ChromeDriver(chromeOptions);
        browser.get(MAIN_URL);

        wait = new FluentWait<>(browser)
                .withTimeout(Duration.ofSeconds(3)) //buvo 2
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(ElementNotInteractableException.class);
    }
    public static void fillForm(String[] args) {

        setup(); //konfiguruoja narsykle-ja paleidzia
        String userName = generateRandomUserName(); //sukuria vartotojo varda
        createNewUser(userName, true); //kuria vartotoja su random vardu sukurtu
        logoutUserWithButton(); //atjunga paskira
        loginUser(userName); //prijungia sukusrta vartotoja

        performCalculation("10", "20"); //skaiciai paduodavi skaiciuotuvui

        performOperationSearch("", "", ""); //operacijos paieska su tusciais parametrais

    }
    public static void createNewUser(String userName, boolean confirmPassword){ //kuria nauja vartotoja
        clickOnElement(By.xpath("/html/body/div[1]/form/div/h4/a"));

        WebElement userNameInput = browser.findElement(By.id("username")); //randa vartotojo vardo laukeli ir ji uzpildo
        userNameInput.sendKeys(userName);

        WebElement passwordInput = browser.findElement(By.id("password")); // iveda slaptazodi
        passwordInput.sendKeys(IDENTIFICATION);
// Jei confirmPassword yra true, randa slaptažodžio patvirtinimo įvesties laukelį ir įveda slaptažodį dar kartą
        if (confirmPassword){
            WebElement passwordConfirmInput = browser.findElement(By.id("passwordConfirm"));
            passwordConfirmInput.sendKeys(IDENTIFICATION);
        }

        clickOnElement(By.xpath("//*[@id=\"userForm\"]/button")); //paspaudzia migtuka kuris pateikia forma taip sukurdamas vartotoja

    }
    public static void logoutUserWithButton(){clickOnElement(By.xpath("/html/body/nav/div/ul[2]/a"));}
    //atjungia vartotoja nuo paskiros

    public static void loginUser(String userName){ //prijungia sukurta vartotoja
        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(10)); //ar reikia?
        WebElement userNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))); //iesko vartotojo vardas laukelio ir ji uzpildo
        userNameInput.sendKeys(userName);
//randa slaptazodzio laukeli ir ji uzpildo
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordInput.sendKeys(IDENTIFICATION);
// spusteli prisijungimo migtuka
        clickOnElement(By.xpath("//button[@type='submit']"));
    }
    public static void performCalculation(String firstNumber, String secondNumber){
        WebElement firstNumberInput = browser.findElement(By.id("sk1"));
        firstNumberInput.clear();
        firstNumberInput.sendKeys(firstNumber);

        WebElement secondNumberInput = browser.findElement(By.id("sk2"));
        secondNumberInput.clear();
        secondNumberInput.sendKeys(secondNumber);

        clickOnElement(By.xpath("//*[@id=\"number\"]/input[3]"));
    }
    public static boolean performOperationSearch(String firstNumber, String secondNumber, String result){
        clickOnElement(By.xpath("/html/body/nav/div/ul[1]/li/a"));
        WebElement resultTable = browser.findElement(By.xpath("/html/body/div/table/tbody"));

        for(WebElement row : resultTable.findElements(By.tagName("tr"))){
            List<WebElement> columns = row.findElements(By.tagName("td"));

            if (columns.toArray().length > 0){

                if(columns.get(0).getText().equals(firstNumber) && columns.get(2).getText().equals(secondNumber) && columns.get(3).getText().equals(result)){
                    return true;
                }
            }
        }
        return false;
    }
    public static void clickOnElement(By locator){browser.findElement(locator).click();} //randa elementa pagal nurodyta lokatoriu ir ji paspaudzia



    public static String generateRandomUserName() {
        Random rand = new Random();
        return "test" + rand.nextInt(100000);
    }
    public static int generateRandomNumber(){
        Random rand = new Random();
        return rand.nextInt(100000) ;
    }
    public static void closeBrowser(){
        browser.quit();
    }

}