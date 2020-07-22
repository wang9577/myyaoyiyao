package com.ustudy.resource.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.springframework.stereotype.Component;
import org.ustudy.ostools.files.services.OsFileService;


@Component("capacityFtpLet")
public class CapacityFtpLet extends DefaultFtplet {
//
//	@Resource
//	private OsFileService osFileService;
//
//	@Override
//	public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
//		Teacher teacher=(Teacher) session.getUser();
//		if(osFileService.sumLength(teacher.getHomeDirectory(), "").longValue()>=teacher.getCloudSize().longValue()) {
//			return FtpletResult.DISCONNECT;
//		}
//		return FtpletResult.DEFAULT;
//	}
//
}
