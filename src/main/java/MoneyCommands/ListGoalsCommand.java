package MoneyCommands;

import controlpanel.*;
import Money.Account;

public class ListGoalsCommand extends MoneyCommand{

    public ListGoalsCommand(){
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void execute(Account account, Ui ui, Storage storage) {
        for (int i = 1; i <= account.getShortTermGoals().size();i++) {
            //System.out.println(" " + i + "." + account.getShortTermGoals().get(i-1).toString() + "\n");
            ui.appendToOutput(" " + i + "." + account.getShortTermGoals().get(i-1).toString() + "\n");
        }
        //System.out.println("current Goal Savings: $" + account.getGoalSavings());
        ui.appendToOutput("current Goal Savings: $" + account.getGoalSavings() + "\n");

    }
}