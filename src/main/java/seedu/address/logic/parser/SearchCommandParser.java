package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMISSION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRODUCT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;

import java.util.stream.Stream;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.messages.SearchMessages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.KeywordPredicate;

/**
 * Parses input arguments and creates a new SearchCommand object
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchCommand
     * and returns a SearchCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME,
                        PREFIX_PHONE,
                        PREFIX_EMAIL,
                        PREFIX_ADDRESS,
                        PREFIX_SALARY,
                        PREFIX_EMPLOYMENT,
                        PREFIX_PRICE,
                        PREFIX_PRODUCT,
                        PREFIX_SKILL,
                        PREFIX_COMMISSION);

        if (!atLeastOnePrefixPresent(argMultimap,
                PREFIX_NAME,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_SALARY,
                PREFIX_EMPLOYMENT,
                PREFIX_PRICE,
                PREFIX_PRODUCT,
                PREFIX_SKILL,
                PREFIX_COMMISSION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(SearchMessages.MESSAGE_SEARCH_MISSING_FIELD,
                    SearchCommand.MESSAGE_USAGE));
        }

        return new SearchCommand(new KeywordPredicate(argMultimap));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean atLeastOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
