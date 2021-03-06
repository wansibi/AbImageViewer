package com.abrain.wiv.listeners;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;

import com.abrain.wiv.utils.DebugUtil;

/**
 * 세션 리스너입니다.
 * <p>* 세션이 종료될 떄 임시 폴더 및 파일들을 삭제합니다.
 * @author Administrator
 *
 */
public class AbHttpSessionListener implements HttpSessionListener, ServletContextListener {
	
	private static ServletContext context;
	
	private Logger logger = Logger.getLogger(AbHttpSessionListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		context = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
	}
	
	//-----------------------------------------------------------------------------------

	/**
	 * 세션이 생성될 때 호출됩니다.
	 * <p>* 세션 아이디를 획득합니다.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		String sessionId = null;
		HttpSession session = event.getSession();

		if (session != null)
			sessionId = session.getId();
		
		System.out.println("[HTTP Session] Created, ID: " + sessionId);
		
		if (logger.isDebugEnabled()) {
            logger.debug("[HTTP Session] Session ID".concat(sessionId).concat(" created at ").concat(new Date().toString()));
        }
	}

	/**
	 * 세션 종료될 때 호출됩니다.
	 * <p>* 획득한 세션 아이디의 임시 폴더들을 삭제합니다.
	 * <p>* 임시 폴더 목록
	 * <ul>
	 * 	<li>/WEB-INF/docs/
	 * 	<li>/WEB-INF/files/tmp/
	 * </ul>
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = null;
		HttpSession session = event.getSession();

		if (session != null)
			sessionId = session.getId();
		
		if (logger.isDebugEnabled()) {
            logger.debug("[HTTP Session] Session ID".concat(sessionId).concat(" destroyed at ").concat(new Date().toString()));
        }
		
		System.out.println("[HTTP Session] Destoryed, ID: " + sessionId);
		
		if (context != null && session != null){
			//-----------------------------------------------------------------------------------
			// 폴라리스 컨버터 임시 폴더 삭제
			
			String docDirPath = context.getRealPath("/WEB-INF/docs/" + sessionId);
			File docDir = new File(docDirPath);
			
			if (docDir.exists()){
				try
				{
					FileUtils.deleteDirectory(docDir);
					
					System.out.println("[HTTP Session][remove-template][doc] remove directory!!!");
				}
				catch (IOException ioe)
				{
					DebugUtil.print(ioe);
					
					System.out.println("[HTTP Session][remove-template][doc] IOException");
				}
				catch (Exception e)
				{
					DebugUtil.print(e);
					
					System.out.println("[HTTP Session][remove-template][doc] Exception");
				}
			}else{
				System.out.println("[HTTP Session][remove-template][doc] not found directory!!!");
			}
			
			//-----------------------------------------------------------------------------------
			// 인쇄 및 분할 저장 지원 임시 폴더 삭제
	
			String tmpDirPath = context.getRealPath("/WEB-INF/files/tmp/" + sessionId);
			File tmpDir = new File(tmpDirPath);
			
			if (tmpDir.exists()){
				try
				{
					FileUtils.deleteDirectory(tmpDir);
					
					System.out.println("[HTTP Session][remove-template][tmp] remove directory!!!");
				}
				catch (IOException ioe)
				{
					DebugUtil.print(ioe);
					
					System.out.println("[HTTP Session][remove-template][tmp] IOException");
				}
				catch (Exception e)
				{
					DebugUtil.print(e);
					
					System.out.println("[HTTP Session][remove-template][tmp] Exception");
				}
			}else{
				System.out.println("[HTTP Session][remove-template][tmp] not found directory!!!");
			}
			
		}
	}
}
