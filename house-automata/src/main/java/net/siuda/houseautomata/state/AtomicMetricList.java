package net.siuda.houseautomata.state;

import net.siuda.houseautomata.model.IntMetric;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicMetricList {

    private final AtomicReference<AtomicMetricListElement> head = new AtomicReference<>(null);

    public IntMetric first() {
        return head.get() != null ? head.get().value : null;
    }

    public List<IntMetric> slice(Long from, Long to) {
        List<IntMetric> result = new LinkedList<>();
        AtomicMetricListElement current = head.get();
        for (; current != null && current.value.getTimestamp() >= to; current = current.next.get());
        for (; current != null && current.value.getTimestamp() >= from; current = current.next.get()) {
            result.add(current.value);
        }
        return result;
    }

    public List<IntMetric> dump() {
        List<IntMetric> result = new LinkedList<>();
        for(AtomicMetricListElement current = head.get(); current != null; current = current.next.get()) {
            result.add(current.value);
        }
        return result;
    }

    public void add(IntMetric value) {
        AtomicMetricListElement newHead = new AtomicMetricListElement(value);
        newHead.next.set(head.get());
        head.set(newHead);
    }

    public List<IntMetric> split(Long timestamp) {
        List<IntMetric> result = new LinkedList<>();
        AtomicMetricListElement current = head.get();
        if (current != null && current.next != null) {
            for (; current.next != null && current.value.getTimestamp() > timestamp; current = current.next.get()) ;
            current = current.next.getAndSet(null);
            for (; current != null; current = current.next.get()) {
                result.add(current.value);
            }
        }
        return result;
    }

    public static class AtomicMetricListElement {

        private IntMetric value;

        private AtomicMetricListElement(IntMetric value) {
            this.value = value;
        }

        private AtomicReference<AtomicMetricListElement> next = new AtomicReference<>();

    }

}
