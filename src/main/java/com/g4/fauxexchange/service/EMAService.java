package com.g4.fauxexchange.service;

import java.util.List ;
import com.g4.fauxexchange.model.EMAResult;

public interface EMAService {
    public abstract void createEMAResult(EMAResult emaResult);
    public abstract void runEMAAlgorithm();
    public abstract void deleteEMAResult(EMAResult emaResult); //Don't work on this function
    public abstract List<EMAResult> getEMAs();
    public abstract EMAResult getEMAResult(String code);
}