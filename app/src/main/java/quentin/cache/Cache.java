package quentin.cache;

import java.util.LinkedList;
import java.util.List;

public class Cache<E> {

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
        if (currentLog > 0) {
            currentLog--;
        }
    }

    public E goBack() {
        return goBack(1);
    }

    public E goBack(int moves) {
        currentLog -= moves;
        return memory.get(currentLog);
    }

    public E goForward() {
        return goForward(1);
    }

    public E goForward(int moves) {
        currentLog += moves;
        return memory.get(currentLog);
    }

    public void loadLogs(List<E> logs) {
        for (E log : logs) {
            memory.addFirst(log);
        }
        while (memory.size() > 10) {
            memory.removeLast();
        }
    }

    public void clear() {
        memory.clear();
    }

    public int getCurrentLog() {
        return currentLog;
    }

    public int getMemorySize() {
        return memory.size();
    }

    public E getLog() {
        return memory.peek();
    }
}
