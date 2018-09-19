package lx.gs.activity.operational;

import cfg.operationalactivity.ActivityEntry;
import cfg.operationalactivity.PetTotalLevel;
import common.ErrorCode;
import lx.gs.event.AbstractEvent;
import lx.gs.pet.FPet;

/**
 * @author Jin Shuai
 */
public class PetTotalLevelHandler extends OperationalActivityHandler<PetTotalLevel> {
    @Override
    public boolean checkCondition(long roleId, ActivityEntry entry, AbstractEvent event) {
        return FPet.get(roleId).getFightpets().stream()
                .mapToInt(modelId -> FPet.getPetByModelId(roleId, modelId).getLevel())
                .sum() >= cast(entry).num;
    }

    @Override
    public ErrorCode handleCondition(long roleId, ActivityEntry entry) {
        return ErrorCode.OK;
    }
}
