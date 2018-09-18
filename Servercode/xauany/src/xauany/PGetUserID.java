package xauany;

/**
 * ��Xdb��ͨ��ƽ̨�˺Ż�ȡuserid�ĳ�����
 * ������ʵ�־����xdb��ز���
 * @param <T>
 */
public abstract class PGetUserID<T> extends xdb.Procedure{
	protected final int plattype;
	protected final String useridentity;
	
	private long userid;
	
	public PGetUserID(int plattype, String useridentity) {
		super();
		this.plattype = plattype;
		this.useridentity = useridentity;
	}

	@Override
	protected boolean process() throws Exception {
		T userInfo = xdbGetUserInfo();
		if(userInfo == null){
			final PNewUser p = new PNewUser(plattype, useridentity);
			if(!p.call()){
				return false;
			}

			userInfo = xdbNewUserInfo(p.getUserID(), p.getUserInfoID());
		}
		
		userid = getUserID(userInfo);
		
		if(xdb.Trace.isDebugEnabled()){
			xdb.Trace.debug(getClass().getName() + ".PGetUserID useridentity = " + useridentity + " userid = " + userid);
		}
		
		return true;
	}
	
	public long get(){
		return userid;
	}
	
	/**
	 * ��xdb�л�ȡ�û���Ϣ
	 * @return
	 */
	protected abstract T xdbGetUserInfo();
	
	/**
	 * ��ȡuserId
	 * @param userInfo
	 * @return
	 */
	protected abstract long getUserID(T userInfo);
	
	/**
	 * �½�һ���û���Ϣ�����Ҵ浽xdb�У�ͬʱ����
	 * @param userid
	 * @return
	 */
	protected abstract T xdbNewUserInfo(long userid, long userinfoid);
	
}
