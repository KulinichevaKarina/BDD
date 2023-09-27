package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;


public class MoneyTransferTests {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var LoginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.enteringTheConfirmationCode(verificationCode);
    }

    @Test
    void transferFromFirstToSecond() {
        var firstCardInfo = getCardFirstId(); // получаем экземпляр класса CardInfo и добавляем информацию по карте
        var secondCardInfo = getCardSecondId();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo); // получаем баланс карты
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = ValidAmountOnTheCard(firstCardBalance); //10 000
        var expectedBalanceFirstCard = firstCardBalance - amount; // ожидаемый результат 0
        var expectedBalanceSecondCard = secondCardBalance + amount; // ожидаемый результат 20 000
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo); //указываем какую карту нужно пополнить
        dashboardPage = transferPage.makeValidTransfer(valueOf(amount), firstCardInfo); // указываем с какой карты совершаем операцию
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo); // фактический результат после перевода
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void transferFromSecondToFirst() {
        var firstCardInfo = getCardFirstId();
        var secondCardInfo = getCardSecondId();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = ValidAmountOnTheCard(secondCardBalance); //20 000
        var expectedBalanceFirstCard = firstCardBalance + amount; // 20 000
        var expectedBalanceSecondCard = secondCardBalance - amount; // 0
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(valueOf(amount), secondCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void transferInValid() {
        var firstCardInfo = getCardFirstId();
        var secondCardInfo = getCardSecondId();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = ValidAmountOnTheCard(firstCardBalance); // указываем сумму перевода больше доступной
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo); //указываем какую карту нужно пополнить
        transferPage.makeTransfer(valueOf(amount), secondCardInfo); // указываем с какой карты совершаем операцию
        transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей остаток на карте списания");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void transferMoneyFromACardWithAnIncorrectNumber() {
        var firstCardInfo = getCardFirstId();
        var secondCardInfo = getCardSecondId();
        var firstBalanceCard = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = InValidAmountOnTheCard(firstBalanceCard);
        var expectedBalanceFirstCard = firstBalanceCard - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);//указали неверную карту
        transferPage.makeTransfer(valueOf(amount), firstCardInfo);
        transferPage.findErrorMessage("Ошибка Ошибка! Произошла ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

}





