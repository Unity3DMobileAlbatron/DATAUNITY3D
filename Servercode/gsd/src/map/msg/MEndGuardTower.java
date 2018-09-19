
package map.msg;

import com.goldhuman.Common.Marshal.OctetsStream;
import com.goldhuman.Common.Marshal.MarshalException;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS

abstract class __MEndGuardTower__ extends xio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MEndGuardTower extends __MEndGuardTower__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 6697849;

	public int getType() {
		return 6697849;
	}

	public int ectypeid;
	public int errcode;

	public MEndGuardTower() {
	}

	public MEndGuardTower(int _ectypeid_, int _errcode_) {
		this.ectypeid = _ectypeid_;
		this.errcode = _errcode_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(ectypeid);
		_os_.marshal(errcode);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ectypeid = _os_.unmarshal_int();
		errcode = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MEndGuardTower) {
			MEndGuardTower _o_ = (MEndGuardTower)_o1_;
			if (ectypeid != _o_.ectypeid) return false;
			if (errcode != _o_.errcode) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ectypeid;
		_h_ += errcode;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ectypeid).append(",");
		_sb_.append(errcode).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MEndGuardTower _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ectypeid - _o_.ectypeid;
		if (0 != _c_) return _c_;
		_c_ = errcode - _o_.errcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

