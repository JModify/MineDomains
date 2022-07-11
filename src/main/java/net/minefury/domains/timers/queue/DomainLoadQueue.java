package net.minefury.domains.timers.queue;

import lombok.Getter;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.domain.properties.Role;
import net.minefury.domains.domain.DomainHelper;
import net.minefury.domains.message.FuryMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DomainLoadQueue {

    @Getter private Deque<DomainLoadQueueEntry> domainQueue;

    public DomainLoadQueue() {
        this.domainQueue = new LinkedList<>();
    }

    public int getQueued() {
        return domainQueue.size();
    }

    public int getPosition(Domain domain) {
        int counter = 1;
        for (DomainLoadQueueEntry entry : domainQueue) {
            if (entry.getDomain().equals(domain)) {
                break;
            }
            counter++;
        }

        return counter;
    }

    public void addEntry(DomainLoadQueueEntry entry) {
        domainQueue.offerLast(entry);
        notifyQueueJoin(entry.getDomain());
    }

    public void addPriorityEntry(DomainLoadQueueEntry entry) {
        domainQueue.offerFirst(entry);
        notifyQueueJoin(entry.getDomain());
    }

    public void loadNext() {
        DomainLoadQueueEntry nextEntry = domainQueue.peekFirst();
        DomainHelper.loadDomain(nextEntry.getLoader(), nextEntry.getDomain());
        domainQueue.pollFirst();
        notifyQueueUpdate();
    }

    /**
     * Notifies all entries in the queue that their position has been updated.
     */
    private void notifyQueueUpdate() {
        for (DomainLoadQueueEntry entry : domainQueue) {
            Domain domain = entry.getDomain();
            int position = getPosition(domain);

            String message = FuryMessage.DOMAIN_QUEUE_UPDATE.toString()
                    .replace("{POSITION}", String.valueOf(position));

            for (UUID member : domain.getMembers().keySet()) {
                Player player = Bukkit.getPlayer(member);

                if (player != null) {
                    player.sendMessage(message);
                }
            }

            Player owner = Bukkit.getPlayer(domain.getOwner());
            if (owner != null) {
                owner.sendMessage(message);
            }
        }
    }

    /**
     * Notifies all members of a domain that they have joined the loading queue
     * @param domain domain to notify
     */
    private void notifyQueueJoin(Domain domain) {

        Map<UUID, Role> members = domain.getMembers();
        int position = getPosition(domain);

        String message = FuryMessage.DOMAIN_QUEUE_JOINED.toString()
                .replace("{POSITION}", String.valueOf(position));

        for (UUID member : members.keySet()) {
            Player player = Bukkit.getPlayer(member);
            if (player != null) {
                player.sendMessage(message);
            }
        }

        Player owner = Bukkit.getPlayer(domain.getOwner());
        if (owner != null) {
            owner.sendMessage(message);
        }
    }





}
