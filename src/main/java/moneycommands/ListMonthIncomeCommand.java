package moneycommands;

import controlpanel.MoneyStorage;
import controlpanel.Ui;
import money.Account;
import money.Income;

/**
 * This command lists all income sources within the Month Income List to the user.
 */
public class ListMonthIncomeCommand extends MoneyCommand {

    /**
     * Constructor of the list command.
     */
    public ListMonthIncomeCommand(){
    }

    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * This method executes the list month income command.
     * Displays all income sources in the Month Income List to the user according to index.
     * @param account Account object containing all financial info of user saved on the programme
     * @param ui Handles interaction with the user
     * @param storage Saves and loads data into/from the local disk
     */
    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) {
        int counter = 1;
        for (Income i : account.getIncomeListCurrMonth()) {
            ui.appendToOutput(" " + counter + "." + i.toString() + "\n");
            counter++;
        }

        ui.appendToOutput("Total income for the month: $" + account.getCurrMonthIncome() + "\n");
    }
}