package com.navinfo.opentsp.platform.location.protocol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DoNetGenerate extends Generate {
	public void generate() {
		appending("CD " + root);
		appending(root.substring(0,2));
		List<File> list = new ArrayList<File>();
		getProtoFiles(new File(root), list);
		for (File file : list) {
			String fileName = file.getName();
			fileName = fileName.replaceAll(".proto", "");
			if ("csharp_options".equals(fileName)
					|| "descriptor".equals(fileName))
				continue;
			if (file.getPath().indexOf("\\java\\") != -1)
				continue;
			appending("protoc --descriptor_set_out=output/donet/" + fileName
					+ ".protobin --include_imports "
					+ file.getPath().substring(root.length() + 1));
			appending("protogen -output_directory=output/donet/ output/donet/"
					+ fileName + ".protobin");
		}
		this.write("donet");
	}

	public static void main(String[] args) {
		DoNetGenerate doNetGenerate = new DoNetGenerate();
		doNetGenerate.generate();

	}
}
