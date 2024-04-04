package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COMMISSION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRODUCT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PRODUCT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRODUCT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRODUCT_BOB;
import static seedu.address.logic.messages.Messages.FAILED_TO_ADD;
import static seedu.address.logic.messages.Messages.MESSAGE_COMMAND_FORMAT;
import static seedu.address.logic.messages.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.messages.Messages.MESSAGE_MISSING_FIELD_FORMAT;
import static seedu.address.logic.messages.Messages.MESSAGE_UNKNOWN_FIELD_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRODUCT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.BOBSUPPLIER;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddSupplierCommand;
import seedu.address.logic.messages.AddMessages;
import seedu.address.logic.messages.Messages;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Price;
import seedu.address.model.person.Product;
import seedu.address.model.person.Supplier;
import seedu.address.testutil.SupplierBuilder;

public class AddSupplierCommandParserTest {
    private AddSupplierCommandParser parser = new AddSupplierCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Supplier expectedPerson = new SupplierBuilder(BOBSUPPLIER).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + PRODUCT_DESC_BOB + PRICE_DESC_BOB, new AddSupplierCommand(expectedPerson));
    }


    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + PRODUCT_DESC_BOB + PRICE_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY
                        + ADDRESS_DESC_AMY + PRODUCT_DESC_AMY + PRICE_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE,
                        PREFIX_PRODUCT, PREFIX_PRICE));

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid product
        assertParseFailure(parser, validExpectedPersonString + INVALID_PRODUCT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PRODUCT));

        // invalid price
        assertParseFailure(parser, validExpectedPersonString + INVALID_PRICE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PRICE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        ArrayList<String> undetectedFields = new ArrayList<>();
        undetectedFields.add("name");
        String exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        String expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        undetectedFields = new ArrayList<>();
        undetectedFields.add("phone");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                expectedMessage);

        // missing email prefix
        undetectedFields = new ArrayList<>();
        undetectedFields.add("email");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                expectedMessage);

        // missing address prefix
        undetectedFields = new ArrayList<>();
        undetectedFields.add("address");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                expectedMessage);

        // missing product prefix
        undetectedFields = new ArrayList<>();
        undetectedFields.add("product");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + VALID_PRODUCT_BOB + PRICE_DESC_BOB,
                expectedMessage);

        // missing price prefix
        undetectedFields = new ArrayList<>();
        undetectedFields.add("price");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + VALID_PRICE_BOB,
                expectedMessage);

        // all prefixes missing
        undetectedFields = new ArrayList<>();
        undetectedFields.add("name");
        undetectedFields.add("address");
        undetectedFields.add("phone");
        undetectedFields.add("email");
        undetectedFields.add("product");
        undetectedFields.add("price");
        exception = String.format(MESSAGE_MISSING_FIELD_FORMAT, undetectedFields);
        expectedMessage = FAILED_TO_ADD + exception + "\n"
                + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                        + VALID_PRODUCT_BOB + VALID_PRICE_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Name.MESSAGE_CONSTRAINTS));

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Phone.MESSAGE_CONSTRAINTS));

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Email.MESSAGE_CONSTRAINTS));

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Address.MESSAGE_CONSTRAINTS));

        // invalid product
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_PRODUCT_DESC + PRICE_DESC_BOB,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Product.MESSAGE_CONSTRAINTS));

        // invalid employment
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + PRODUCT_DESC_BOB + INVALID_PRICE_DESC,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Price.MESSAGE_CONSTRAINTS));

        /*
         invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
        Tag.MESSAGE_CONSTRAINTS);
         */

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + INVALID_PRODUCT_DESC + INVALID_PRICE_DESC,
                String.format(AddMessages.MESSAGE_ADD_INVALID_PARAMETERS, Name.MESSAGE_CONSTRAINTS));

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidField_failure() {
        String exception = FAILED_TO_ADD + String.format(MESSAGE_UNKNOWN_FIELD_FORMAT, "[commission]");
        exception += "\n" + String.format(MESSAGE_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);
        assertParseFailure(parser, NAME_DESC_BOB + COMMISSION_DESC_BOB + PHONE_DESC_BOB
                        + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + PRODUCT_DESC_BOB + PRICE_DESC_BOB,
                exception);
    }
}
