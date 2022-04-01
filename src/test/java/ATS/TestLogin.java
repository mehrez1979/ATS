package ATS;

import org.testng.annotations.Test;

import com.xpandit.testng.annotations.Xray;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class TestLogin {
    @Test
    @Xray(requirement = "HTUTO-34", test="HTUTO-35")
  public void f() {
    	System.out.println("jjjd);
  }
  @BeforeSuite
  public void beforeSuite() {
  }

  @AfterSuite
  public void afterSuite() {
  }

}
