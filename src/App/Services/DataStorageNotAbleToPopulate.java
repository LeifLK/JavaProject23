package App.Services;

public class DataStorageNotAbleToPopulate extends Exception {
    public DataStorageNotAbleToPopulate(String errorMessage) {
        super(errorMessage);
    }
}
