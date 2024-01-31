package App.UI;

import App.Services.DataStorage;

public interface UIPanel {
    void initialize();
    void refreshData(DataStorage newDataStorage);
}
