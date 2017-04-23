import java.util.concurrent.atomic.AtomicReference;
import java.util.EmptyStackException;
import java.util.concurrent.TimeoutException;

public class LockFreeStack<T> {
  AtomicReference<Node> top = new AtomicReference<Node>(null);
  static final int capacity = 8;
  EliminationArray<T> eliminationArray = new EliminationArray<T>(capacity);

  protected boolean tryPush(Node node) {
    Node oldTop = top.get();
    node.next = oldTop;
    return top.compareAndSet(oldTop, node);
  }

  public void push(T value) {
    Node node = new Node(value);
    while (true) {
      if (tryPush(node)) {
        return;
      } else try {
        T otherValue = eliminationArray.visit(value, capacity);
        if (otherValue == null) {
          return;
        }
      } catch (TimeoutException ex) {
        // IGNORE
      }
    }
  }

  protected Node tryPop() throws EmptyStackException {
    Node oldTop = top.get();
    if (oldTop == null) {
      throw new EmptyStackException();
    }
    Node newTop = oldTop.next;
    if (top.compareAndSet(oldTop, newTop)) {
      return oldTop;
    } else {
      return null;
    }
  }

  public T pop() throws EmptyStackException {
    while (true) {
      Node returnNode = tryPop();
      if (returnNode != null) {
        return returnNode.value;
      } else try {
        T otherValue = eliminationArray.visit(null, capacity);
        if (otherValue != null) {
          return otherValue;
        }
      } catch (TimeoutException ex) {
          // IGNORE
      }
    }
  }

  private class Node {
    public T value;
    public Node next;
    public Node(T val) {
      value = val;
      next = null;
    }
  }
}
