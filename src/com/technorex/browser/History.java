package com.technorex.browser;

import javafx.scene.control.ComboBox;

import java.util.ArrayList;

public class History {
    private ArrayList<String> history;
    private int currIndex;
    History() {
        history = new ArrayList<>();
        currIndex = -1;
    }

    void addHistory(String string) {
        if(currIndex==history.size()-1)
            history.add(++currIndex,string);
        else {
            for(int ind = currIndex; ind<history.size(); ind++)
                history.remove(ind);
            history.add(++currIndex,string);
        }
    }

    String forward() {
        if(currIndex!=history.size()-1)
            return history.get(++currIndex);
        return null;
    }

    String backward() {
        if(currIndex>0)
            return history.get(--currIndex);
        return null;
    }

    ComboBox<String> getHistory(ComboBox<String> ref) {
        ref.getItems().removeAll(ref.getItems());
        for (int ind = history.size()-1; ind>-1; ind--)
            ref.getItems().add(history.get(ind));
        return ref;
    }
}
