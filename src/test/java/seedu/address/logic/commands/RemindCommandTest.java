package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.messages.RemindMessages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author jannaleong
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code RemindCommand}.
 */
public class RemindCommandTest {
    @Test
    public void execute_remindSuccessful() {
        Model modelManager = new ModelManager(new AddressBook(getTypicalAddressBook()), new UserPrefs());
        CommandResult commandResult = new RemindCommand().execute(modelManager);
        assertEquals(String.format(RemindMessages.MESSAGE_REMIND_PERSON_SUCCESS,
                modelManager.getFilteredPersonList().size()), commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        RemindCommand firstRemindCommand = new RemindCommand();
        RemindCommand secondRemindCommand = new RemindCommand();

        // same object -> returns true
        assertTrue(firstRemindCommand.equals(secondRemindCommand));

        // different types -> returns false
        assertFalse(firstRemindCommand.equals(1));

        // null -> returns false
        assertFalse(firstRemindCommand.equals(null));
    }

    @Test
    public void toStringMethod() {
        RemindCommand remindCommand = new RemindCommand();
        String expected = RemindCommand.class.getCanonicalName() + "{}";
        assertEquals(expected, remindCommand.toString());
    }
}
