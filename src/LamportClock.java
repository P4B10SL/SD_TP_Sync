import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LamportClock {

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private int timestamp;

    public LamportClock() {
        this.timestamp = 0;
    }

    public void increment() {
        lock.lock();
        try {
            this.timestamp++;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getTimestamp() {
        lock.lock();
        try {
            return this.timestamp;
        } finally {
            lock.unlock();
        }
    }

    public void waitUntil(int timestamp) {
        lock.lock();
        try {
            while (this.timestamp < timestamp) {
                condition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
