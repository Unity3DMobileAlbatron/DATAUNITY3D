package lx.gs.activity.operational;

import cfg.operationalactivity.ActivityEntry;
import cfg.operationalactivity.RoleLevel;
import common.ErrorCode;
import lx.gs.event.AbstractEvent;
import xtable.Roleinfos;

/**
 * @author Jin Shuai
 */
public class RoleLevelHandler extends OperationalActivityHandler<RoleLevel> {

    @Override
    public boolean checkCondition(long roleId, ActivityEntry entry, AbstractEvent event) {
        return Roleinfos.selectLevel(roleId) >= cast(entry).num;
    }

    @Override
    public ErrorCode handleCondition(long roleId, ActivityEntry entry){
        return ErrorCode.OK;
    }

}
