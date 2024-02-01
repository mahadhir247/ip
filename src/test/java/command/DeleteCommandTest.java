package command;

import org.junit.jupiter.api.Test;
import storage.Storage;
import task.Deadline;
import task.Task;
import task.TaskList;
import task.Todo;
import ui.Ui;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DeleteCommandTest {

    Storage storage;
    Ui ui;
    TaskList tasks;
    TaskList testList;

    public DeleteCommandTest() {
        this.storage = new Storage("");
        this.ui = new Ui();
        this.tasks = new TaskList();
        this.testList = new TaskList();

        Task todo1 = new Todo("test");
        Task deadline = new Deadline("return book",
                LocalDateTime.of(2024, 1, 1, 18, 0));
        Task todo3 = new Todo("task 3");

        testList.addTask(todo1);
        testList.addTask(todo3);

        tasks.addTask(todo1);
        tasks.addTask(deadline);
        tasks.addTask(todo3);
    }

    @Test
    public void execute_normalInput_success() throws Exception{
        DeleteCommand del = new DeleteCommand(1);
        del.execute(tasks, ui, storage);
        assertEquals(tasks.size(), testList.size());
        assertEquals(tasks.getTask(0), testList.getTask(0));
        assertEquals(tasks.getTask(1), testList.getTask(1));
    }

    @Test
    public void execute_invalidIndex_exceptionThrown() {
        try {
            DeleteCommand del = new DeleteCommand(-1);
            del.execute(tasks, ui, storage);
            assertEquals(tasks.size(), 2);
            fail();
        } catch (Exception e) {
            assertEquals("Not a valid task number buddy!", e.getMessage());
        }
    }
}