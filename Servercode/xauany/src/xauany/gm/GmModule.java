package xauany.gm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import gm.GmLauncher;

public class GmModule {

	public static void startWithPort(int gmport, Integer csport, boolean isTest) throws IOException{
		new GmLauncher()
			.gmport(gmport) // gm socket�˿ڣ�telnet����̨��!=0 ����socket����
			.csport(csport == null ? 0 : csport.intValue()) //�ͷ�����˿ڣ�!=0 �����ͷ�����socket����
			.test(isTest) //�Ƿ��ڲ���ģʽ������GM������false��ʾ��ʽ��������Ҫ��½GM�˺Ų���ִ��GM�����¼��ʽ[admin login gmaccount, gmpassword]
			.httpport(0) // gm http�˿ڣ�!=0 �ṩ��http�ķ�ʽִ��GM����
			
			
			.autoScanGmPackage(GmModule.class.getPackage().getName()) //ɨ��package�´���gm.annotation.Moduleע���class���Զ�ע�ᵽGM������
			
			//batch ����ģ��
			.onlineRolesSupplier(() -> { // batch onlinerolesexec ����ʹ�ã���ѯ���߽�ɫ�б�
				return new ArrayList<>();
			})
			.allRolesSupplier(() -> { // batch allrolesexec ����ʹ�ã���ѯ���н�ɫ�б�
				return new LinkedList<>();
			})
			
			//protocol ����ģ��
			.protocolPackagePrefixName("xauany") // Э��packageǰ׺
			.showProtocolPredicate((protcolFullName) -> {// Э����˹���
				int index = protcolFullName.lastIndexOf('.');
				String cmdName = protcolFullName.substring(index + 1);
				return cmdName.startsWith("C"); // Э������C��ʼ����ʾ��Э���ǿͻ��˷��͸��������ģ����������ݸ�����Ĺ��������޸�
			})
			.protocolStubSupplier(() -> {// Э������
				return null;
			})
			
			//protocollog ����ģ��
			.roleFinder((protocol) -> { // ͨ��Э���ѯ����Э��Ľ�ɫ
				return 0L;
			})
			.protocolLogPredicate((protocol) -> { // Э���¼���˹���
				return true;
			})
			
			//admin ����ģ��
			.withGmAccount("admin", xauany.Config.getInstance().getGmPassword())
			
			//����GM
			.start();
	}
}
