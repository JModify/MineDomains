package net.minefury.domains.timers;

import com.modify.fundamentum.timer.SyncRepeatingTask;
import net.minefury.domains.MineDomains;
import net.minefury.domains.timers.queue.DomainLoadQueue;

public class QueueLoadTimer extends SyncRepeatingTask {

    @Override
    public void run() {
        DomainLoadQueue loadQueue = MineDomains.getInstance().getDomainManager().getDomainLoadQueue();
        if (!loadQueue.getDomainQueue().isEmpty()) {
            loadQueue.loadNext();
        }
    }

}
