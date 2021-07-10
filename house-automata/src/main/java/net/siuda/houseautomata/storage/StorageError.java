package net.siuda.houseautomata.storage;

public class StorageError extends RuntimeException {

    public enum Action {
        SAVE_ERROR, LOAD_ERROR
    }

    public StorageError(Action action, Throwable cause) {
        super(action.name(), cause);
    }

    public StorageError(Action action) {
        this(action, null);
    }

}
