package com.ruoyi.process.general.service;

import com.ruoyi.process.general.domain.HistoricActivity;

import java.util.List;

public interface IProcessService {

    /**
     * 查询审批历史列表
     * @param processInstanceId
     * @param historicActivity
     * @return
     */
    List<HistoricActivity> selectHistoryList(String processInstanceId, HistoricActivity historicActivity);

}
