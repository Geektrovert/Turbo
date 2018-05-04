package com.technorex.browser;
import java.util.ArrayList;

class History {
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
            for(int ind = currIndex+1; ind<history.size(); ind++)
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
}
