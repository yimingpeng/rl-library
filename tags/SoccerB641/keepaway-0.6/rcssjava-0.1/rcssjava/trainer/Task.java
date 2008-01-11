package rcssjava.trainer;

/**
 * Interface for trainer tasks
 * @author Gregory Kuhlmann
 */
public interface Task
{
    void init();
    boolean processCycle();
}
