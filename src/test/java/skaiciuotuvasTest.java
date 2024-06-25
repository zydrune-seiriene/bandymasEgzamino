import org.example.Main;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class skaiciuotuvasTest {
    public static final String USER_NAME = Main.generateRandomUserName();
    public static final int FIRST_NUMBER = Main.generateRandomNumber();
    public static final int NEXT_NUMBER = Main.generateRandomNumber();



    @BeforeAll
    public static void setUp(){
        Main.setup();
    }

    @Test
    @Order(1)
    public void PositiveCreateNewUser(){

        Main.createNewUser(USER_NAME, true);
        String result = Main.browser.findElement(By.xpath("/html/body/nav/div/ul[2]/a")).getText();
        Assertions.assertEquals("Logout, " + USER_NAME, result);
        Main.logoutUserWithButton();
    }

    @Test
    @Order(2)
    public void NegativeNewUser(){

        Main.createNewUser(USER_NAME, false);
        String result = Main.browser.findElement(By.id("passwordConfirm.errors")).getText();
        Assertions.assertEquals("Įvesti slaptažodžiai nesutampa", result);
        Main.browser.get(Main.MAIN_URL);
    }

    @Test
    @Order(3)
    public void Positivelogin(){

        Main.loginUser(USER_NAME);
        String result = Main.browser.findElement(By.xpath("/html/body/nav/div/ul[2]/a")).getText();
        Assertions.assertEquals("Logout, " + USER_NAME, result);
        Main.logoutUserWithButton();
    }

    @Test
    @Order(4)
    public void Negativelogin(){

        Main.loginUser("wrongUserName");
        String result = Main.browser.findElement(By.xpath("/html/body/div/form/div/span[2]")).getText();
        Assertions.assertEquals("Įvestas prisijungimo vardas ir/ arba slaptažodis yra neteisingi", result);
        Main.browser.get(Main.MAIN_URL);
    }

    @Test
    @Order(5)
    public void positiveCalculation(){

        Main.loginUser(USER_NAME);
        Main.performCalculation(String.valueOf(FIRST_NUMBER), String.valueOf(NEXT_NUMBER));
        String result = Main.browser.findElement(By.xpath("/html/body/h4")).getText();
        Assertions.assertEquals(FIRST_NUMBER +" + "+ NEXT_NUMBER+" = "+(FIRST_NUMBER+NEXT_NUMBER), result);
        Main.logoutUserWithButton();
    }

    @Test
    @Order(6)
    public void NegativeCalculation(){

        Main.loginUser(USER_NAME);
        Main.performCalculation("-3", "9");
        String result = Main.browser.findElement(By.id("sk1.errors")).getText();
        Assertions.assertEquals("Validacijos klaida: skaičius negali būti neigiamas", result);
        Main.logoutUserWithButton();
    }

    @Test
    @Order(7)
    public void positiveSearch(){

        Main.loginUser(USER_NAME);
        boolean result = Main.performOperationSearch(String.valueOf(FIRST_NUMBER), String.valueOf(NEXT_NUMBER), String.valueOf(FIRST_NUMBER + NEXT_NUMBER));
        Assertions.assertTrue(result);
        Main.logoutUserWithButton();
    }

    @Test
    @Order(8)
    public void NegativeSearch(){

        Main.loginUser(USER_NAME);
        boolean result = Main.performOperationSearch("-7", "-1", "-9");
        Assertions.assertFalse(result);
        Main.logoutUserWithButton();
    }

    @AfterAll
    public static void close(){
        Main.closeBrowser();
    }
}
