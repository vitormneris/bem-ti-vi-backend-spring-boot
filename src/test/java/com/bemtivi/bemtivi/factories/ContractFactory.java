package com.bemtivi.bemtivi.factories;

import java.util.ArrayList;
import java.util.List;

public interface ContractFactory<T> {

    default T createOneObject() {
        return createObject( true);
    }

    default List<T> createMultipleObjects(Integer number) {
        List<T> object = new ArrayList<>();
        for (int i = 1; i <= number; i++) object.add(createObject(true));
        return object;
    }

    default List<T> createMultipleDeactivationObjects(Integer number) {
        List<T> object = new ArrayList<>();
        for (int i = 1; i <= number; i++) object.add(createObject(false));
        return object;
    }

    T createObject(boolean isActive);
}
