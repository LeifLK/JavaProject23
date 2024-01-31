package App.UI;

import App.Services.DataStorage;

import javax.xml.crypto.Data;

public interface UIPanel {
    void initialize();
    void refreshData(DataStorage newDataStorage);
}
