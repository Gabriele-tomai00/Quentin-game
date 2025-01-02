package quentin.cache;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class Cache<E> implements Serializable {

    @Serial private static final long serialVersionUID = 7440346358324416350L;
    private final LinkedList<E> memory;
    private int currentLog;

    public Cache() {
        memory = new LinkedList<>();
        currentLog = 0;
    }

    public void saveLog(E log) {
        if (currentLog > 0) {
            memory.subList(0, currentLog).clear();
            currentLog = 0;
        }
        memory.addFirst(log);
        while (memory.size() > 10) {
            memory.removeLast();
        }
    }

    public E goBack() {
        return goBack(1);
    }

    public E goBack(int moves) {
        if (currentLog + moves < memory.size()) {
            currentLog += moves;
            return memory.get(currentLog);
        } else {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, Size: %d", currentLog + moves, memory.size()));
        }
    }

    public E goForward() {
        return goForward(1);
    }

    public E goForward(int moves) {
        if (currentLog - moves >= 0) {
            currentLog -= moves;
            return memory.get(currentLog);
        } else {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, currentLog: %d", currentLog - moves, currentLog));
        }
    }

    public void clear() {
        currentLog = 0;
        memory.clear();
    }

    public int getCurrentLog() {
        return currentLog;
    }

    public int getMemorySize() {
        return memory.size();
    }

    public E getLog() {
        return memory.get(currentLog);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Cache<?> cache) {
            return this.memory.equals(cache.memory);
        }
        return false;
    }
}
