package command;

import exception.BuddyException;
import storage.Storage;
import task.Task;
import task.TaskList;
import ui.Ui;

public class MarkCommand extends Command {
    CommandType commandWord;
    int taskIndex;
    public MarkCommand(CommandType commandWord, int taskIndex) {
        this.commandWord = commandWord;
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BuddyException {
        try {
            Task task = tasks.getTask(taskIndex);
            task.setDone(commandWord.equals(CommandType.MARK));
            ui.showMark(task);
        } catch (IndexOutOfBoundsException ioobe) {
            throw new BuddyException("Not a valid task number!");
        }
    }
}