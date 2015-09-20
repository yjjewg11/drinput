package com.px.news.doorrecord;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.company.news.ProjectProperties;
import com.company.news.entity.DoorRecord;
import com.company.news.jsonform.DoorUserJsonform;
import com.px.news.doorrecord.utils.ReadWriteUtil;

public class DoorMain {
	private static final Logger log = Logger.getLogger(DoorMain.class);
	private static boolean isRunning = false;
	private static String cardFilter="";

	/**
	 * 未绑定的卡号提交自动绑定请求给服务器
	 */
	private static void autobindcarduser(DoorUserJsonform user){
		//进行卡号过滤判断
//		if(cardFilter.indexOf(","+user.getCardid()+",")!=-1){
//			//已同步过，直接返回
//			log.info("已同步过，不需要再同步");
//			return ;
//		}
		try {
			DataSubmit d = new DataSubmit();
			
			if(cardFilter.indexOf(","+user.getCardid()+",")!=-1){
				//已同步过，直接返回
				//log.info("已同步过，不需要再同步");
				return ;
			}
			boolean flag =d.autobind(user);
			
			//同步成功,更新过滤文件
			if(flag){
				cardFilter+=(user.getCardid()+",");
				ReadWriteUtil.writeCardStr(cardFilter);
				
				log.info("autobind success.Cardid="+user.getCardid());
			}else{
				log.error("autobind fail");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("autobindcarduser:异常");
			log.error(e);
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		log.info("serverName="+ProjectProperties.getProperty("serverName", ""));
		log.info("portNumber="+ProjectProperties.getPropertyAsInt("portNumber", 1433));
		try {
			//初始化过滤ID卡号
			cardFilter=ReadWriteUtil.readCardStr();
			//每次启动做一次用户信息校验,没有的就进行用户同步,先将userid和caridId同步金
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("初始化读取过滤卡号失败");
		}
//		
//		try {
//			List<DoorUserJsonform> list = DataRead.readCardUserListByLastEditRq();
//			log.info("main:CardUser list size="+list.size());
//			for(DoorUserJsonform o:list){
//				autobindcarduser(o);
//			}
//			//log.info("上传用户与卡绑定关系数据,成功!共:"+list.size());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error(e);
//			
//		}
		Timer timer = new Timer();
		log.info("定时器已启动");
		// 在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
		log.info("1秒后开始执行定时任务");
//		log.info("任务参数 执行频率（毫秒）:" + Constants.frequency + " 单次同步时间区间(毫秒):"
//				+ Constants.time_interval);
		timer.schedule(new submitTask(), 1000, Constants.frequency);

	}

	public static class submitTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 为防止上次任务还未执行完（当然，一般任务是没有这么长的），避免第二次又被调度以引起执行冲突，设置了当前是否正在执行的状态标志isRunning
			
			if (!isRunning) {
				isRunning = true;
				log.info("开始执行指定任务");
			}else{
				log.info("isRunning="+ isRunning);
			}

			Date startTime = null;
			DataSubmit d1 = new DataSubmit();
			
			//心跳
			boolean heartbeatflag=false;
			try {
				heartbeatflag=d1.heartbeat("o");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error(d1);
			}
			if(!heartbeatflag){

				log.info("服务器连接失败！");
				isRunning=false;
				return ;
			}
			
			//用户同步
			try {
				List<DoorUserJsonform> list = DataRead.readCardUserListByLastEditRq();
				log.info("main:readCardUserListByLastEditRq list size="+list.size());
				for(DoorUserJsonform o:list){
					autobindcarduser(o);
				}
				//log.error("上传用户与卡绑定关系数据,成功!共:"+list.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
				
			}
			try {
				startTime = ReadWriteUtil.readTimestamp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error("读取初始化时间戳错误！");
			}
			
			Integer pagesize=DataRead.pagesize;
			// 当前时间减去时间戳大于时间间隔时，继续执行
			boolean isNeedRead=true;
//			while ((new Date().getTime() - startTime.getTime()) > Constants.time_interval) {
				while (isNeedRead) {
				log.info("-------------begain----------------");
				
//				Date endTime = new Timestamp(startTime.getTime()
//						+ Constants.time_interval);
//				//log.info("读取记录的时间戳:" + startTime+"~~~~"+ endTime);
				Date endTime=null;
				try {
					List<DoorRecord> list = DataRead.readList(startTime,
							endTime);
					log.info("本次读取记录:" + list.size());
					if(DataRead.pagesize.equals(list.size())){//判断是否读取完毕
						isNeedRead=true;
					}else{
						isNeedRead=false;
					}
					if (list.size() > 0) {
						DataSubmit d = new DataSubmit();
						boolean flag = d.submitList(list);
						// 写入成功，更新本地时间戳
						if (flag) {
							log.info("成功提交数据到服务器");
							
							if(DataRead.lastDoor_record_date!=null){
								startTime = DataRead.lastDoor_record_date;
								ReadWriteUtil.writeTimestamp(startTime);
							}

//							for(DoorRecord doorRecord:list)
//							//调用自动绑定
//							autobind(doorRecord.getCardid());
						} else {
							
							log.error("数据提交服务器失败。3秒后重试本次时间戳任务");
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
//					else {
//						log.info("不需要同步");
//						ReadWriteUtil.writeTimestamp(endTime);
//						startTime = endTime;
//					}

					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("数据提交服务器错误！");
					// 执行完成一次后，等待3秒
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}

			}

			log.info("本次任务结束");
			isRunning=false;

		}
		
		/**
		 * 未绑定的卡号提交自动绑定请求给服务器
		 */
		private void autobind(String cardid){
			log.info("autobind,cardid:"+cardid);
			//进行卡号过滤判断
			if(cardFilter.indexOf(","+cardid+",")!=-1){
				//已同步过，直接返回
			//	log.info("已同步过，不需要再同步");
				return ;
			}
			log.info("同步用户，不需要再同步");
			try {
				DoorUserJsonform user=DataRead.getCard(cardid);
				
				DataSubmit d = new DataSubmit();
				boolean flag =d.autobind(user);
				
				//同步成功,更新过滤文件
				if(flag){
					cardFilter+=(cardid+",");
					ReadWriteUtil.writeCardStr(cardFilter);
					
					log.info("autobind success!cardid="+cardid);
				}else{
					log.error("autobind fail!cardid="+cardid);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("门禁卡信息读取失败.cardid="+cardid);
				e.printStackTrace();
			}
			log.info("autobind end");
			
		}

	}

}
