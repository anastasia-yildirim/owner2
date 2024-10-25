package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.appium.java_client.AppiumBy.*;
import static io.qameta.allure.Allure.step;

public class SearchTests extends TestBase {

    @Tag("mobile")
    @Test
    void successfulSearchTest() throws MalformedURLException, InterruptedException {
        step("Search", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(id("org.wikipedia.alpha:id/search_src_text")).sendKeys("Appium");
        });
        step("Verify content found", () -> {
            $$(id("org.wikipedia.alpha:id/page_list_item_title")).shouldHave(sizeGreaterThan(0));
        });
    }

    @Tag("mobile")
    @Test
    void searchAndOpenFirstResultTest() throws MalformedURLException, InterruptedException {
        String pageText = "Italy";

        step("Search and open page", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(id("org.wikipedia.alpha:id/search_src_text")).sendKeys(pageText);
            $(className("android.view.ViewGroup")).click();
        });
        step("Verify content of the page", () -> {
            $(id("org.wikipedia.alpha:id/page_list_item_title")).shouldHave(text(pageText));
        });
    }
}

