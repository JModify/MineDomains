package net.minefury.domains.manager;

import net.minefury.domains.data.flatfile.FlatFileHelper;
import net.minefury.domains.timers.AutoUnloadTimer;
import net.minefury.domains.timers.QueueLoadTimer;

public class TaskManager {

    private QueueLoadTimer queueLoadTimer;
    private AutoUnloadTimer autoUnloadTimer;

    public TaskManager() {
        queueLoadTimer = new QueueLoadTimer();
        autoUnloadTimer = new AutoUnloadTimer();
    }

    public void init() {
        startEnabledTasksOrCancel();
    }

    /**
     * Starts or cancels repeating tasks based on configuration values.
     */
    private void startEnabledTasksOrCancel() {

        if (FlatFileHelper.isQueueEnabled()) {
            long interval = FlatFileHelper.getQueueInterval();
            queueLoadTimer.startTask(20, interval);
        } else {
            queueLoadTimer.cancelTask();
        }

        if (FlatFileHelper.isAutoUnloadEnabled()) {
            long interval = FlatFileHelper.getAutoUnloadInterval();
            autoUnloadTimer.startTask(20, interval);
        } else {
            autoUnloadTimer.cancelTask();
        }
    }

}
