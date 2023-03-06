package OrangeHRM;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class OrangeHRMTestCases extends DriverClass {
    /**https://opensource-demo.orangehrmlive.com/web/index.php/auth/login*/
    /**
     * "1. In the login Panel, enter the username
     * <p>
     * 2. Enter the Password for the ESS-User account in the password field
     * <p>
     * 3. Click ""Login"" button"
     */
    @Test(priority = 2, groups = "smokeTest")
    public void successfulLogin() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("Admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("admin123");
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
        WebElement userDropDown = driver.findElement(By.className("oxd-userdropdown-tab"));
//        Expected Result: The user is logged in successfully.
        Assert.assertTrue(userDropDown.isDisplayed(), "User was not able to login");
    }

    /**
     * "1. In the login Panel, enter the username
     * <p>
     * 2. Enter the Password for the ESS-User account in the password field
     * <p>
     * 3. Click ""Login"" button"
     */
    @Test(priority = 1)
    public void unsuccessfulLogin() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("Admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("abc123");
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
        WebElement errormsg = driver.findElement(By.xpath("//p[text()='Invalid credentials']"));
//        Expected Result: The user is logged in successfully.
        Assert.assertTrue(errormsg.getText().equals("Invalid credentials"), "Error message did not occur");
    }

    /**
     * "1. In the login Panel, enter the username
     * <p>
     * 2. Enter the Password for the ESS-User account in the password field
     * <p>
     * 3. Click ""Login"" button
     * <p>
     * 4. Check the fields on the ""Personal information Page"""
     */
    @Test(dependsOnMethods = "successfulLogin", priority = 3)
    public void firstTimeLogin() {
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement pim = driver.findElement(By.xpath("//h6[text()='PIM']"));
        WebElement employeeId = driver.findElement(By.xpath("//label[text()='Employee Id']/../..//input"));
        WebElement ssnNo = driver.findElement(By.xpath("//label[text()='SSN Number']/../..//input"));
        WebElement sinNo = driver.findElement(By.xpath("//label[text()='SIN Number']/../..//input"));
        WebElement driverLicenseNo = driver.findElement(By.xpath("//label[contains(text(),'License Number')]/../..//input"));
        WebElement dob = driver.findElement(By.xpath("//label[text()='Date of Birth']/../..//input"));
        /**Expected result: "The user is logged in successfully and the personal information page is displayed

         Check if the following fields are disabled for entry in Personal Details:
         ● Employee ID
         ● SSN No
         ● SIN No
         ● Driver License No
         ● Date of Birth "
         */
        Assert.assertTrue(pim.isDisplayed(), "Personal information page was not displayed");
        Assert.assertFalse(employeeId.isEnabled(), "Employee ID is enabled");
        Assert.assertFalse(ssnNo.isEnabled(), "SSN No is enabled");
        Assert.assertFalse(sinNo.isEnabled(), "SIN No is enabled");
        Assert.assertFalse(driverLicenseNo.isEnabled(), "Driver License No is enabled");
        Assert.assertFalse(dob.isEnabled(), "Date of Birth is enabled");
    }

    /**
     * "1. Check the fields on the ""Personal information Page""
     * <p>
     * 2. Change the field, ""First Name"" with enter a valid new name in this field
     * <p>
     * 3. Click on ""Save"""
     */
    @Test(dependsOnMethods = "successfulLogin", priority = 4, groups = "smokeTest")
    public void changeTheFirstName() {
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement fName = driver.findElement(By.name("firstName"));
        fName.click();
        fName.sendKeys(Keys.chord(Keys.CONTROL, "a"), "Tester");
        WebElement saveBtn = driver.findElement(By.xpath("(//button[@type='submit'])[1]"));
        saveBtn.click();
        driver.navigate().refresh();
        fName = driver.findElement(By.name("firstName"));
        String confirmFName = driver.findElement(By.cssSelector(".oxd-userdropdown-tab>p")).getText();
        fName.click();
        fName.sendKeys(Keys.chord(Keys.CONTROL, "a"), "Donn");
        saveBtn = driver.findElement(By.xpath("(//button[@type='submit'])[1]"));
        saveBtn.click();
        driver.navigate().refresh();
        WebElement pim = driver.findElement(By.xpath("//h6[text()='PIM']"));
        /**Expected result:
         * "The users information is displayed
         *
         * The first name field needs to now show the new value entered"*/
        Assert.assertTrue(pim.isDisplayed(), "Users information is not displayed");
        Assert.assertTrue(confirmFName.contains("Tester"));
    }

    /**
     * "1- Click on the photograph displayed at the top left corner of the page
     * <p>
     * 2- Click on ""Choose a file"" button
     * <p>
     * 3- Choose a image file of type ""JPG"" that is less than 1 MB
     * <p>
     * 4- Click on upload"
     */
    /**
     * "The ""Photograph screen"" will be displayed
     * <p>
     * You will be able to browse your local machine for images
     * <p>
     * The file name is selected in the ""Choose a file"" box
     * <p>
     * The file gets uploaded and the older image is replaced"
     */
    /*src="/web/index.php/pim/viewPhoto/empNumber/7"
     * src="/web/index.php/pim/viewPhoto/empNumber/7"*/
    @Test(dependsOnMethods = "successfulLogin", priority = 5, groups = "smokeTest")
    public void uploadJPGImage() throws AWTException {
        Robot robot = new Robot();
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement img = driver.findElement(By.className("employee-image"));
        img.click();
        String beforeImg = driver.findElement(By.xpath("(//img[@class='employee-image'])[2]")).getAttribute("src");
        WebElement addImg = driver.findElement(By.cssSelector(".oxd-icon.bi-plus"));

        addImg.click();
//        before running change the absolute path to the file. The file is in OrangeIMG package.
        StringSelection stringSelection = new StringSelection("\"C:\\Users\\aslan\\IdeaProjects\\OrangeHRM_WholeProject\\src\\test\\java\\OrangeHRM\\OrangeIMG\\download.jpg\"");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clipboard.setContents(stringSelection, stringSelection);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String afterImg = driver.findElement(By.xpath("(//img[@class='employee-image'])[2]")).getAttribute("src");
        WebElement saveBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        saveBtn.click();

        WebElement photoScreen = driver.findElement(By.xpath("(//img[@class='employee-image'])[1]"));
        /**Expected result:
         * "The ""Photograph screen"" will be displayed
         *
         *      You will be able to browse your local machine for images
         *
         *      The file name is selected in the ""Choose a file"" box
         *
         *      The file gets uploaded and the older image is replaced"*/
        Assert.assertNotEquals(beforeImg, afterImg, "image was not changed");
        Assert.assertTrue(photoScreen.isDisplayed(), "Photograph screen is not displayed");

    }

    /**
     * "1- Click on the photograph displayed at the top left corner of the page
     * <p>
     * 2- Click on ""Choose a file"" button
     * <p>
     * 3- Choose a image file of type PNG that is less than 1 MB
     * <p>
     * 4- Click on upload"
     */
    @Test(dependsOnMethods = "successfulLogin", priority = 6)
    public void uploadPNGImage() throws AWTException {
        Robot robot = new Robot();
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement img = driver.findElement(By.className("employee-image"));
        img.click();
        String beforeImg = driver.findElement(By.xpath("(//img[@class='employee-image'])[2]")).getAttribute("src");
        WebElement addImg = driver.findElement(By.cssSelector(".oxd-icon.bi-plus"));

        addImg.click();
//        before running change the absolute path to the file. The file is in OrangeIMG package.
        StringSelection stringSelection = new StringSelection("\"C:\\Users\\aslan\\IdeaProjects\\OrangeHRM_WholeProject\\src\\test\\java\\OrangeHRM\\OrangeIMG\\sample-profile-picture.png\"");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clipboard.setContents(stringSelection, stringSelection);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String afterImg = driver.findElement(By.xpath("(//img[@class='employee-image'])[2]")).getAttribute("src");
        WebElement saveBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        saveBtn.click();

        WebElement photoScreen = driver.findElement(By.xpath("(//img[@class='employee-image'])[1]"));
        /**Expected result:
         * "The ""Photograph screen"" will be displayed
         *
         * You will be able to browse your local machine for images
         *
         * The file name is selected in the ""Choose a file"" box
         *
         * The file gets uploaded and the older image is replaced"*/
        Assert.assertNotEquals(beforeImg, afterImg, "image was not changed");
        Assert.assertTrue(photoScreen.isDisplayed(), "Photograph screen is not displayed");
    }

    /**
     * "1- Click on the photograph displayed at the top left corner of the page
     * <p>
     * 2- Click on ""Choose a file"" button
     * <p>
     * 3- Choose the doc file that is less than 1 MB
     * <p>
     * 4- Click on upload
     * "
     */
    @Test(dependsOnMethods = "successfulLogin", priority = 7)
    public void uploadDocFile() throws AWTException {
        Robot robot = new Robot();
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement img = driver.findElement(By.className("employee-image"));
        img.click();
        WebElement addImg = driver.findElement(By.cssSelector(".oxd-icon.bi-plus"));

        addImg.click();
//        before running change the absolute path to the file. The file is in OrangeIMG package.
        StringSelection stringSelection = new StringSelection("\"C:\\Users\\aslan\\IdeaProjects\\OrangeHRM_WholeProject\\src\\test\\java\\OrangeHRM\\OrangeIMG\\newFile.doc\"");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clipboard.setContents(stringSelection, stringSelection);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement docFile = driver.findElement(By.xpath("//span[text()='Attachment Size Exceeded']"));
        String afterImg = docFile.getText();
        /**Expected result:
         * "The ""Photograph screen"" will be displayed
         *
         * You will be able to browse your local machine for images
         *
         * The file name is selected in the ""Choose a file"" box
         *
         * An error message is displayed that the format is not supported- <The exact error message>"*/
        Assert.assertEquals(afterImg, "Attachment Size Exceeded", "error is not as expected");
        Assert.assertTrue(docFile.isDisplayed(), "Error message was not displayed");
    }

    /**
     * "1- Click on the photograph displayed at the top left corner of the page
     * <p>
     * 2- Click on ""Choose a file"" button
     * <p>
     * 3- Choose the JPG file that is more than 1 MB
     * <p>
     * 4- Click on upload"
     */
    @Test(dependsOnMethods = "successfulLogin", priority = 8)
    public void upload1MBJPGImage() throws AWTException {
        Robot robot = new Robot();
        WebElement myInfo = driver.findElement(By.xpath("//span[text()='My Info']"));
        myInfo.click();
        WebElement img = driver.findElement(By.className("employee-image"));
        img.click();
        WebElement addImg = driver.findElement(By.cssSelector(".oxd-icon.bi-plus"));

        addImg.click();
//        before running change the absolute path to the file. The file is in OrangeIMG package.
        StringSelection stringSelection = new StringSelection("\"C:\\Users\\aslan\\IdeaProjects\\OrangeHRM_WholeProject\\src\\test\\java\\OrangeHRM\\OrangeIMG\\highresolutionfile.jpg\"");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clipboard.setContents(stringSelection, stringSelection);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement docFile = driver.findElement(By.xpath("//span[text()='Attachment Size Exceeded']"));
        String afterImg = docFile.getText();
        /**Expected result:
         * "The ""Photograph screen"" will be displayed
         *
         * You will be able to browse your local machine for images
         *
         * The file name is selected in the ""Choose a file"" box
         *
         * An error message is displayed that the format is not supported- <The exact error message>"*/
        Assert.assertEquals(afterImg, "Attachment Size Exceeded", "error is not as expected");
        Assert.assertTrue(docFile.isDisplayed(), "Error message was not displayed");

    }

}
