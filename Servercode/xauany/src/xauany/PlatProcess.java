package xauany;

import java.util.Map;

/**
 * ƽ̨����ӿ�
 */
public interface PlatProcess {
	
	/**
	 * ��xml���ó�ʼ��ƽ̨������Ϣ
	 * @param ele
	 */
	void init(org.w3c.dom.Element ele);
	
	int getType();
	
	/**
	 * ƽ̨������
	 * @return
	 */
	String getName();
	
	/**
	 * ֧���ص���·��
	 * ֧���ص���URL��http://ip:port/getOrderCallbackPath()
	 * @return
	 */
	String getOrderCallbackPath();
	
	/**
	 * �û���¼
	 * @param arg
	 * @param res
	 */
	void login(gnet.AuAnyLoginArg arg, gnet.AuAnyLoginRes res);
	
	/**
	 * ƽ̨֧����ɺ�Ļص��ӿ�
	 * @param params ����
	 * @return
	 */
	byte[] orderCallBack(Map<String, String> params);
	
}
