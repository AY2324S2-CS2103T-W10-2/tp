package seedu.address.logic.parser;

import static seedu.address.logic.messages.AddMessages.ADD;
import static seedu.address.logic.messages.AddMessages.FAILED_TO_ADD;
import static seedu.address.logic.messages.AddMessages.OTHER_TYPE;
import static seedu.address.logic.messages.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.messages.NoteMessages.DEFAULT_NOTE;
import static seedu.address.logic.messages.RateMessages.DEFAULT_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.messages.AddMessages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Rating;
import seedu.address.model.tag.Tag;

//@@author chiageng
/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {
    public static final String MESSAGE_NULL_ARGUMENTS = "argument to pass for add command is null";
    public static final String MESSAGE_COMMENCE_PARSING = "Going to start parsing for add command.";
    private final Logger logger = LogsCenter.getLogger(getClass());

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution. Parameter {@code args} cannot be null.
     * @param args The arguments of the AddCommand object.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        assert (args != null) : MESSAGE_NULL_ARGUMENTS;

        logger.log(Level.INFO, MESSAGE_COMMENCE_PARSING);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_NOTE,
                        PREFIX_RATING);

        // validates user command fields
        ParserUtil.verifyNoUnknownPrefix(args, AddCommand.MESSAGE_USAGE, ADD,
                FAILED_TO_ADD,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_NOTE, PREFIX_RATING);
        ParserUtil.verifyNoMissingField(argMultimap, AddCommand.MESSAGE_USAGE, ADD,
                FAILED_TO_ADD,
                PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL);
        boolean isPreambleEmpty = argMultimap.isPreambleEmpty();
        if (!isPreambleEmpty) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Person person = createPersonContact(argMultimap);

        return new AddCommand(person);
    }
    //@@author

    /**
     * Creates a {@code Person} contact based on the argument multimap.
     * @param argMultimap Contains the mappings of values to the specific prefixes.
     * @return A person contact.
     * @throws ParseException If the user enters invalid parameters.
     */
    private Person createPersonContact(ArgumentMultimap argMultimap) throws ParseException {
        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).orElseThrow());
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).orElseThrow());
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).orElseThrow());
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).orElseThrow());
            String noteContent = argMultimap.getValue(PREFIX_NOTE).orElse(DEFAULT_NOTE);
            Note note = noteContent.equals(DEFAULT_NOTE) ? new Note(noteContent) : ParserUtil.parseNote(noteContent);
            Rating rating = ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING).orElse(DEFAULT_RATING));
            Tag tag = new Tag(OTHER_TYPE);
            Set<Tag> tags = new HashSet<>();
            tags.add(tag);
            return new Person(name, phone, email, address, note, tags, rating);
        } catch (ParseException pe) {
            throw new ParseException(String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, pe.getMessage()));
        }
    }
}
