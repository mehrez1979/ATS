package ATS;

import org.testng.annotations.Test;

import com.xpandit.testng.annotations.Xray;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class TestLogin {

private WebDriver driver;
	String Username;
	String Password;
	String ResultatAttendu;

	
  @BeforeTest
  public void beforeClass() throws InterruptedException {
	  
	// Instantiation du driver 
	  
	  System.setProperty("webdriver.chrome.driver","C:/selenium/driver/chromedriver.exe");
	  driver = new ChromeDriver();
	  driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	  driver.manage().window().maximize();
	
	// Connexion à l'ATS 
	  
	  driver.get("http://ats.faimerecruiter.com/");
	  //Temporisateur wait attend jusqu'à remplir la condition username
	  WebDriverWait wait = new WebDriverWait(driver, 5);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='username']")));
	  
  }
  @DataProvider(name="RegisterData")
  
  public Object[][] RegisterData() throws IOException {
	  
	    FileInputStream inputStream = new FileInputStream(new File("C:/Excel/TestLogin.xlsx"));
		XSSFWorkbook Workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = Workbook.getSheetAt(0);
		int NombreLigne=sheet.getLastRowNum();
		Object[][] Data=new Object[NombreLigne][4];
		DataFormatter myDataFormatter = new DataFormatter();
	 	
	 	for (int i=1;i<=NombreLigne;i++)
		{
			XSSFCell CellUsername=sheet.getRow(i).getCell(0);
			XSSFCell CellPassword=sheet.getRow(i).getCell(1);
			XSSFCell CellResultatAttendu=sheet.getRow(i).getCell(2);
			
			Username=myDataFormatter.formatCellValue(CellUsername);
			Password=myDataFormatter.formatCellValue(CellPassword);
			ResultatAttendu=myDataFormatter.formatCellValue(CellResultatAttendu);
			
			Data[i-1][0]=Username;
	 		Data[i-1][1]=Password;
	 		Data[i-1][2]=ResultatAttendu;
	 		Data[i-1][3]=i;
		}
	 	return Data;
  }

  @Test(dataProvider="RegisterData")
  public void TestLogin (String Username, String Password, String ResultatAttendu,int i) throws InterruptedException {
	  
		WebElement ObjUsername =  driver.findElement(By.xpath("//input[@id='username']"));
		WebElement ObjPassword =  driver.findElement(By.xpath("//input[@id='password']"));
		WebElement ObjValidation =  driver.findElement(By.xpath("//input[@value='Login']"));
	    
		ObjUsername.clear();
		ObjUsername.sendKeys(Username);
		ObjPassword.clear();
		ObjPassword.sendKeys(Password);

		ObjValidation.click();
		String ResultatTest="";
		String ResultatActuel="";
		String Anomalie="";
		if(ResultatAttendu.contentEquals("Connexion reussi"))
		{
			try {
				WebElement Logout=driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
				ResultatActuel=ResultatAttendu;
				ResultatTest="PASSED";
			}
			catch(Exception e)
			{
				driver.get("http://ats.faimerecruiter.com/index.php?m=logout");
				ResultatTest="FAILED";
				Anomalie="Nous Devons Avoir comme résultat : "+ResultatAttendu+" alors que nous avons comme résultat: "+ResultatActuel;
			}
			
		}
  }

@AfterClass
  public void afterClass() {
//	driver.quit();
  }

}
