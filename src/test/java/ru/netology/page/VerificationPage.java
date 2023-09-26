package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement verificationField = $("[data-test-id='code'] input");
    private final SelenideElement verificationButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        verificationField.shouldBe(visible);
    }

    public DashboardPage enteringTheConfirmationCode(DataHelper.VerificationCode code) {
        verificationField.setValue(code.getCode());
        verificationButton.click();
        return new DashboardPage();

    }

}
