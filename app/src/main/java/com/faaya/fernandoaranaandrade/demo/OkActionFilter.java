package com.faaya.fernandoaranaandrade.demo;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;

import java.util.List;

interface OkActionFilter {
    void doAction(List<TaskType> checked, boolean unfishedTask);
}
