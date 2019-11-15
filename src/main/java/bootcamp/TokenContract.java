package bootcamp;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputs().size() != 0)
            throw new IllegalArgumentException("Zero Inputs Expected");

        if (tx.getOutputs().size() != 1)
            throw new IllegalArgumentException("One Output Expected");

        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("One Command Expected");

        if (!(tx.getOutput(0) instanceof TokenState))
            throw new IllegalArgumentException("Output of TokenState Expected");

        TokenState tokenState = (TokenState) tx.getOutput(0);
        if (tokenState.getAmount() < 1)
            throw new IllegalArgumentException("Positive Amount Expected");

        if (!(tx.getCommand(0).getValue() instanceof Commands.Issue))
            throw new IllegalArgumentException("Issue Command Expected");

        if (!(tx.getCommand(0).getSigners().contains(tokenState.getIssuer().getOwningKey())))
            throw new IllegalArgumentException("Issuer must sign");
    }

    public interface Commands extends CommandData {
        class Issue implements Commands {
        }
    }
}