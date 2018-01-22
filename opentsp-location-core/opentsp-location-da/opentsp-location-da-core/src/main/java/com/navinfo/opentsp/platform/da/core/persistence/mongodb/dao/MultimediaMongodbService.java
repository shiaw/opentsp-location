package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;


import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.PictureEntity;

public interface MultimediaMongodbService {

	abstract byte[] queryPicture(String fileCode);
	abstract void savePicture(PictureEntity pictureEntity);
}
