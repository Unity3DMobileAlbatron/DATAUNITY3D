package lx.gs.activity.operational;

import cfg.operationalactivity.ActivityEntry;
import cfg.operationalactivity.ArenaWin;
import common.ErrorCode;
import lx.gs.activity.FActivity;
import lx.gs.event.AbstractEvent;
import lx.gs.event.EventType;
import xbean.RoleActivityRecord;

/**
 * @author Jin Shuai
 */
public class ArenaWinHandler extends OperationalActivityHandler<ArenaWin> {
    @Override
    public boolean checkCondition(long roleId, ActivityEntry entry, AbstractEvent event) {
        return FActivity.getActivityDataByType(roleId, getOperationalType(entry.id)).getArenawin() >= cast(entry).num;
    }

    @Override
    public ErrorCode handleCondition(long roleId, ActivityEntry entry) {
        return ErrorCode.OK;
    }

    @Override
    public boolean consumeEvent(AbstractEvent event, ActivityEntry entry) {
        if(event.eventType == EventType.ARENA_WIN){
            RoleActivityRecord record = FActivity.getActivityDataByType(event.roleId, getOperationalType(entry.id));
            record.setArenawin(record.getArenawin() + 1);
            return true;
        }
        return false;
    }
}
