package parser;

import command.*;
import exception.BuddyException;
import task.Deadline;
import task.Event;
import task.Todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents Parser component of Buddy, parsing inputs given by user.
 */
public class Parser {
    protected static final DateTimeFormatter DATE_TIME_PARSE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");

    /**
     * Parses input given by user.
     *
     * @param fullCommand Input given by user.
     * @return Command to be executed.
     * @throws BuddyException If input given by user is incomplete or unrecognised.
     */
    public static Command parse(String fullCommand) throws BuddyException {
        if (!fullCommand.isEmpty()) {
            String[] commandParts = fullCommand.split(" ", 2);
            CommandType commandWord = getCommandType(commandParts[0]);

            switch (commandWord) {
            case BYE:
                // Fallthrough
                return new ExitCommand();
            case LIST:
                return new ListCommand();
            case MARK:
                // Fallthrough
            case UNMARK:
                try {
                    if (commandParts.length == 1) {
                        throw new BuddyException("I need a task number buddy!");
                    }

                    int index = Integer.parseInt(commandParts[1].trim()) - 1;
                    return new MarkCommand(commandWord, index);
                } catch (NumberFormatException nfe) {
                    throw new BuddyException("Not a valid task number buddy!");
                }
            case DELETE:
                try {
                    if (commandParts.length == 1) {
                        throw new BuddyException("I need a task number buddy!");
                    }

                    int index = Integer.parseInt(commandParts[1].trim()) - 1;
                    return new DeleteCommand(index);
                } catch (NumberFormatException nfe) {
                    throw new BuddyException("Not a valid task number buddy!");
                }
            case TODO:
                if (commandParts.length == 1) {
                    throw new BuddyException("Please include a task!");
                }

                Todo todo = new Todo(commandParts[1].trim());
                return new AddCommand(todo);
            case DEADLINE:
                try {
                    if (commandParts.length == 1) {
                        throw new BuddyException("Please include a task and deadline!");
                    }

                    String[] requestBody = commandParts[1].split("/by", 2);
                    if (requestBody.length <= 1 || requestBody[1].isEmpty()) {
                        throw new BuddyException("Please include a deadline!");
                    }

                    LocalDateTime by = LocalDateTime.parse(requestBody[1].trim(), DATE_TIME_PARSE_FORMAT);
                    Deadline deadline = new Deadline(requestBody[0], by);
                    return new AddCommand(deadline);

                } catch (DateTimeParseException dtpe) {
                    throw new BuddyException("Not a valid date format!");
                }
            case EVENT:
                try {
                    if (commandParts.length == 1) {
                        throw new BuddyException("Please include a task and start/end times");
                    }

                    String[] requestBody = commandParts[1].split("/from", 2);
                    if (requestBody.length <= 1 || requestBody[1].isEmpty()) {
                        throw new BuddyException("Please include start/end time!");
                    }

                    String[] requestTime = requestBody[1].split("/to", 2);
                    if (requestTime.length <= 1 || requestTime[1].isEmpty()) {
                        throw new BuddyException("Please include end time!");
                    }

                    LocalDateTime from = LocalDateTime.parse(requestTime[0].trim(), DATE_TIME_PARSE_FORMAT);
                    LocalDateTime to = LocalDateTime.parse(requestTime[1].trim(), DATE_TIME_PARSE_FORMAT);
                    Event event = new Event(requestBody[0].trim(), from, to);
                    return new AddCommand(event);
                } catch (DateTimeParseException dtpe) {
                    throw new BuddyException("Not a valid date format!");
                }
            case FIND:
                if (commandParts.length == 1) {
                    throw new BuddyException("What are you trying to find buddy?");
                }

                return new FindCommand(commandParts[1].trim());
            default:
                throw new BuddyException("Not a valid command!");
            }
        }
        return new Command();
    }

    /**
     * Assigns type of command to input given by user.
     *
     * @param cmd Command given by user.
     * @return CommandType of command to be executed.
     */
    public static CommandType getCommandType(String cmd) {
        try {
            return CommandType.valueOf(cmd.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            return CommandType.INVALID;
        }
    }
}
