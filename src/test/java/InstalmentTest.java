import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Ui;
import money.Account;
import money.Instalment;
import moneycommands.AddInstalmentCommand;
import moneycommands.DeleteInstalmentCommand;
import moneycommands.ListInstalmentCommand;
import moneycommands.MoneyCommand;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstalmentTest {
    private Ui ui;
    private Account account;
    private MoneyStorage moneyStorage;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    private String testDate = "9/10/1997";
    private LocalDate dateTestDate = LocalDate.parse(testDate, dateTimeFormatter);

    public InstalmentTest() {
        Path currentDir = Paths.get("data/account-test.txt");
        String filePath = currentDir.toAbsolutePath().toString();
        moneyStorage = new MoneyStorage(filePath);
        account = new Account();
        ui = new Ui();
    }

    @Test
    public void testInstalment() throws ParseException, DukeException {
        account.getInstalments().clear();
        Instalment instalment = new Instalment(5000, "car", "instalments", dateTestDate, 120, 3);
        account.getInstalments().add(instalment);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        instalment.isPayTheMonth();
        assertEquals(true, instalment.getPayForTheMonth());

        instalment.isNotPayTheMonth();
        assertEquals(false, instalment.getPayForTheMonth());

        assertEquals("car", instalment.getDescription());
        assertEquals(5000.0, instalment.getPrice());
        assertEquals("instalments", instalment.getCategory());
        assertEquals(120, instalment.getNumOfPayments());
        assertEquals("9/10/1997", instalment.getBoughtDate());
        assertEquals("9/10/2007", instalment.getDateEndDate());
        assertEquals("[INS]" + "$" + instalment.getPrice() + " "
                + instalment.getDescription() + "(on: " + instalment.getBoughtDate() + ")", instalment.toString());
    }

    @Test
    public void testAddInstalment() throws ParseException, DukeException {
        String testInput = "add instalment mortgage /amt 100000 /within 200 months /from 12/12/2010 @6%";
        MoneyCommand addInstalmentCommand =  new AddInstalmentCommand(testInput);
        ui.clearOutputString();
        addInstalmentCommand.execute(account, ui, moneyStorage);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        int last = account.getInstalments().size() - 1;

        assertEquals(" Got it. I've added this to your instalments: \n"
                + account.getInstalments().get(last).toString() + "\n"
                + " Now you have " + account.getInstalments().size() + " instalments listed\n"
                + " You are paying $" + df.format(account.getInstalments().get(last).EqualMonthlyInstalment())
                + " per month\n" + " For " + account.getInstalments().get(last).getNumOfPayments() + " months\n"
                + " Until " + account.getInstalments().get(last).getDateEndDate() + "\n"
                + " The total amount you will pay is $" + account.getInstalments().get(last).totalAmount() + "\n"
                , ui.getOutputString());
    }

    @Test
    public void testDeleteInstalmentException() throws ParseException, DukeException {
        account.getInstalments().clear();
        Instalment instalment = new Instalment(5000, "car", "instalments", dateTestDate, 120, 3);
        Instalment instalment1 = new Instalment(100000, "mortgage", "instalments", dateTestDate, 180, 4);
        account.getInstalments().add(instalment);
        account.getInstalments().add(instalment1);

        String deleteFirstInput = "delete instalments 2";
        MoneyCommand deleteInstalmentCommand = new DeleteInstalmentCommand(deleteFirstInput);
        ui.clearOutputString();
        deleteInstalmentCommand.execute(account, ui, moneyStorage);
        assertEquals(" Noted. I've removed this Instalment:\n"
                + "  " + instalment1.toString() + "\n"
                + " Now you have " + (account.getInstalments().size()) + " instalments in the list.\n"
                , ui.getOutputString());

        String deleteSecondInput = "delete instalments 1";
        MoneyCommand deleteSecondInstalmentCommand = new DeleteInstalmentCommand(deleteSecondInput);
        ui.clearOutputString();
        deleteSecondInstalmentCommand.execute(account, ui, moneyStorage);
        assertEquals(" Noted. I've removed this Instalment:\n"
                        + "  " + instalment.toString() + "\n"
                        + " Now you have " + (account.getInstalments().size()) + " instalments in the list.\n"
                , ui.getOutputString());
    }

    @Test
    public void testListInstalments() throws ParseException, DukeException {
        account.getInstalments().clear();
        Instalment instalment = new Instalment(5000, "car", "instalments", dateTestDate, 120, 3);
        Instalment instalment1 = new Instalment(100000, "mortgage", "instalments", dateTestDate, 180, 4);
        account.getInstalments().add(instalment);
        account.getInstalments().add(instalment1);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        MoneyCommand listInstalmentCommand = new ListInstalmentCommand();
        ui.clearOutputString();
        listInstalmentCommand.execute(account, ui, moneyStorage);
        assertEquals("Got it, list will be printed in the other pane!\n", ui.getOutputString());
        assertEquals(" 1.[" + df.format(instalment.getPercentage()) + "%] " + instalment.getDescription() + " ($"
                        + df.format(instalment.EqualMonthlyInstalment()) + " per month until " + instalment.getDateEndDate() + ")\n"
                        + " 2.[" + df.format(instalment1.getPercentage()) + "%] " + instalment1.getDescription() + " ($"
                        + df.format(instalment1.EqualMonthlyInstalment()) + " per month until " + instalment1.getDateEndDate() + ")\n",
                ui.getGraphContainerString());
    }
}