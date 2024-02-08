package command;

import exception.BuddyException;
import storage.Storage;
import task.Task;
import task.TaskList;

/**
 * Represents Command that deletes task from task list.
 */
public class DeleteCommand extends Command {
    protected int taskIndex;

    /**
     * Creates DeleteCommand with specified task to be deleted.
     *
     * @param taskIndex Index of task to be deleted.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Deletes task from TaskList.
     *
     * @param tasks TaskList consisting of Tasks.
     * @param storage Current Storage object used.
     * @throws BuddyException If given index of task is not in TaskList.
     */
    @Override
    public String execute(TaskList tasks, Storage storage) throws BuddyException {
        String response = "";
        try {
            Task task = tasks.getTask(taskIndex);
            tasks.deleteTask(task);
            response += "I've deleted the following task:\n" + task + "You have " + tasks.size() + " tasks remaining!";
            return response;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new BuddyException("Not a valid task number buddy!");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
