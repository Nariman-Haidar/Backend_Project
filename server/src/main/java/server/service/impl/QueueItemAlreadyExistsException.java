package server.service.impl;

public class QueueItemAlreadyExistsException extends RuntimeException {

    public QueueItemAlreadyExistsException(String message) {
        super(message);
    }

}

