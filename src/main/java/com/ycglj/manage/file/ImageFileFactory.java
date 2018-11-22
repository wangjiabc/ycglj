package com.ycglj.manage.file;

import java.util.List;
import java.util.Map;

public class ImageFileFactory extends AbstractFileUpload{

	@Override
	public Map<String,Object> upload(Object object,String ID,String dataType,String license,List<String> names, List<byte[]> files) {
		// TODO Auto-generated method stub
		return uploadFile(object,ID,dataType,license,names, files);
	}
	

}
