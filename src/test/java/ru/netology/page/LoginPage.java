package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
private final SelenideElement loginFiled = $("[data-test-id='login'] input");
private final SelenideElement passwordFiled = $("[data-test-id='password'] input");
private final SelenideElement buttonFiled = $("[data-test-id='action-login']");

public VerificationPage validLogin(DataHelper.AuthInfo info) {
    loginFiled.setValue(info.getLogin());
    passwordFiled.setValue(info.getPassword());
    buttonFiled.click();
    return new VerificationPage();
}

}
