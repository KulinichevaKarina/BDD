package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final String balanceStart = ", баланс: ";
    private final String balanceFinish = " р.";

    private final SelenideElement heading = $("[data-test-id='dashboard']");
    private final ElementsCollection cards = $$(".list__item div");

     public DashboardPage() {
         heading.shouldBe(visible);
    }
    public int getCardBalance(DataHelper.CardInfo cardInfo) {
         var text = cards.findBy(Condition.text(cardInfo.getCardNumber().substring(15))).getText(); // поиск карты на последним 4 цифрам
         return expectedBalance(text);
    }
    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
         cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId())).$("button").click(); // ищем карту и нажимаем кнопку перевести
         return new TransferPage(); // возращаем обновленную страницу
    }
    private int expectedBalance(String text) {
        var start = text.indexOf(balanceStart); // ищем индекс вхождения подстроки в строку
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish); // ищем фрагмент строки между balanceStart и balanceFinish
        return Integer.parseInt(value); // переводим полученное значение к типу Integer
     }

}
