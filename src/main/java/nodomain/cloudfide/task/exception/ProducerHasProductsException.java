package nodomain.cloudfide.task.exception;

public class ProducerHasProductsException extends RuntimeException {
    public ProducerHasProductsException(String message) {
        super(message);
    }
}