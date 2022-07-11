package net.minefury.domains.exceptions;

public class DocumentParseException extends Exception {

    public DocumentParseException() {
        super();
    }

    public DocumentParseException(String message) {
        super(message);
    }

    public DocumentParseException(String message, String domainId) {
        super("Failed to parse document to domain "
                + domainId + ". " + message);
    }

}
