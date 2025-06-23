package app.validation.response;

public record ValidationResponse(boolean isValid, String errorMessage) {
    public ValidationResponse() {
        this(false, "");
    }
}
