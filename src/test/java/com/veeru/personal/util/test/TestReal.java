package com.veeru.personal.util.test;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: veerana
 * Date: 6/17/15
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestReal {

    private static Logger logger= LogManager.getLogger(TestReal.class);
    private static String reportFileName="RealPlayerCloudUserDetails.csv";
    private static FileWriter fileStream=null;
    private static BufferedWriter bufferedWriter=null;
    public static void main(String[] args){

        Gson gson=new Gson();
    }

    @Test
    public void testCreateRealPlayerUser() throws  Exception{

        int count=1;
        String userPass="P1234567p";

        fileStream = new FileWriter(new File(reportFileName),true);
        bufferedWriter = new BufferedWriter(fileStream);
       /* bufferedWriter.write("FirstName,LastName,eMail,Password\n");
        bufferedWriter.flush();
        */
        String SELENIUM_HUBHOST=System.getenv("SELENIUM_HUBHOST");
        if(StringUtils.isBlank(SELENIUM_HUBHOST)){
            SELENIUM_HUBHOST="spreadsaid.corp.gq1.yahoo.com";
        }

        String 	SELENIUM_HUBPORT=System.getenv("SELENIUM_HUBPORT");
        if(StringUtils.isBlank(SELENIUM_HUBPORT)){
            SELENIUM_HUBPORT= "4444";
        }

        String remoteWebDriverHub="http://".concat(SELENIUM_HUBHOST).concat(":").concat(SELENIUM_HUBPORT).concat("/wd/hub");
        for(int i=1;i<=count;i++){

            DesiredCapabilities capability = DesiredCapabilities.firefox();
            WebDriver driver = new RemoteWebDriver(new URL(remoteWebDriverHub),
                    capability);

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.manage().window().setSize(new Dimension(1920, 1080));

            ///WebDriver driver = new FirefoxDriver();
            String firstName= RandomStringUtils.randomAlphanumeric(6);
            String lastName=RandomStringUtils.randomAlphanumeric(6);
            String emailId=firstName.concat("@yahoo.com");

            driver.get("https://cloud.real.com/r/uATnL");
            Thread.sleep(2000);
            WebElement fName = driver.findElement(By.id("first_name"));
            fName.sendKeys(firstName);
            WebElement lName = driver.findElement(By.id("last_name"));
            lName.sendKeys(lastName);
            WebElement eMail = driver.findElement(By.id("signupUsername"));
            eMail.sendKeys(emailId);
            WebElement uPasswd = driver.findElement(By.id("signupPassword"));
            uPasswd.sendKeys(userPass);
            //WebElement tos = driver.findElement(By.id("tos_agree"));
            WebElement tos = driver.findElement(By.cssSelector("html body.signup.en.extended div#mainContainer div#content div.rpSignupWrapper form#createAccountForm div.rpFormFieldWrapper.tosWrapper input#tos_agree.rpFieldEmpty.validate"));
            tos.click();
            WebElement createAccountButtom=driver.findElement(By.id("rpSignupCreateAccount"));
            createAccountButtom.submit();

            bufferedWriter.write(firstName+"," + lastName + "," + emailId + "," + userPass + "\n");
            bufferedWriter.flush();
            Thread.sleep(5000);
            driver.quit();
        }

        fileStream.close();
        bufferedWriter.close();

    }

}
