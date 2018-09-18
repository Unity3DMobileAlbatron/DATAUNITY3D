package apns;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum ApnsThreadPool {
	INSTANCE;
	
	private final BlockingQueue<ApnsSendMsg> lowQueue = new LinkedBlockingQueue<>(ApnsCfg.getWaitQueueCapacity());
	private final BlockingQueue<ApnsSendMsg> highQueue = new LinkedBlockingQueue<>(ApnsCfg.getWaitQueueCapacity());
	private final List<ApnsThread> threads = new ArrayList<>();
	
	
	public void start() {
		int highThreadNum = ApnsCfg.getThreadPoolSize() / 10;
		highThreadNum = Math.max(1, highThreadNum);
		int lowThreadNum = ApnsCfg.getThreadPoolSize() - highThreadNum;
		lowThreadNum = Math.max(1, lowThreadNum);
		for(int i = 0; i < highThreadNum; i ++) {
			ApnsThread thread = new ApnsThread("ApnsHighThread-" + i, highQueue);
			threads.add(thread);
			thread.start();
		}
		for(int i = 0; i < lowThreadNum; i ++) {
			ApnsThread thread = new ApnsThread("ApnsLowThread-" + i, lowQueue);
			threads.add(thread);
			thread.start();
		}
	}
	
	public void stop() {
		while(highQueue.size() > 0) {
			try {
				java.util.concurrent.TimeUnit.SECONDS.sleep(2);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//�����ȼ�����Ϣֱ�Ӷ���
		for(ApnsThread thread : threads){
			thread.shutdown();
		}
	}
	
	/**
	 * �������͵�������Ϣ�������Ͷ���
	 * @param msg
	 * @param priority
	 * @return
	 */
	public boolean sendApnsMsg(ApnsSendMsg msg, byte priority) {
		boolean isHighPriority = priority > 1;
		boolean suc = false;
		if(isHighPriority){
			suc = highQueue.offer(msg);
		}else{
			suc = lowQueue.offer(msg);
		}
		
		return suc;
	}
	
}
