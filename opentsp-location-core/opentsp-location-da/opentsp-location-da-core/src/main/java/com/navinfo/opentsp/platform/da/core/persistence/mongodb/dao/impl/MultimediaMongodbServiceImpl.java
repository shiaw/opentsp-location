package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.PictureEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.MultimediaMongodbService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
/**
 *
 * @author
 *
 */
public class MultimediaMongodbServiceImpl extends MongoDaoImp implements MultimediaMongodbService {
	/**
	 * 查询图片信息
	 * @param fileName
	 * @return returnBuf
	 */
	@Override
	public byte[] queryPicture(String fileName) {
		GridFS gridFS = null;
		try {
			gridFS = new GridFS(MongoManager.getDB(LCMongo.DB.LC_GPS_MULTIMEDIA));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DBObject query  = new BasicDBObject("filename", fileName);
		GridFSDBFile gridFSDBFile = gridFS.findOne(query);
		byte [] returnBuf=new byte[(int) gridFSDBFile.getLength()];
		OutputStream out=new ByteArrayOutputStream();
		InputStream inputStream=null;
		try {
			inputStream = gridFSDBFile.getInputStream();
			inputStream.read(returnBuf);

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return returnBuf;

	}
	/**
	 * 保存图片信息
	 * @param pictureEntity
	 */
	@Override
	public void savePicture(PictureEntity pictureEntity) {
		//获取图片数据
		InputStream picStream=new ByteArrayInputStream(pictureEntity.getData());
		try  {
			GridFS gridFS = null;
			try {
				gridFS = new GridFS(MongoManager.getDB(LCMongo.DB.LC_GPS_MULTIMEDIA));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GridFSInputFile gridFSInputFile = gridFS.createFile(picStream);
			gridFSInputFile.setFilename(pictureEntity.getFileName());
			gridFSInputFile.save();
		} catch (MongoException e) {
			e.printStackTrace();
		}finally{
			if(picStream!=null){
				try {
					picStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	}


}
