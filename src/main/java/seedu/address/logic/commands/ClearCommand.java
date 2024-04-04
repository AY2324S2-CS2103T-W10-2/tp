package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.messages.ClearMessages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {
    public static final String COMMAND_WORD = "/clear";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(ClearMessages.MESSAGE_CLEAR_POOCHPLANNER_SUCCESS);
    }
}
