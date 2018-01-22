package com.navinfo.opentsp.platform.location.protocol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


class JavaGenerate extends Generate {

    public String generate() {
        appending("CD " + root);
        appending(root.substring(0, 2));
        List<File> list = new ArrayList<File>();
        getProtoFiles(new File(root), list);
        for (File file : list) {
            String fileName = file.getName();
            fileName = fileName.replaceAll(".proto", "");
            if ("csharp_options".equals(fileName)
                    || "descriptor".equals(fileName))
                continue;
            if (file.getPath().indexOf("\\net\\") != -1)
                continue;
//			if(file.getPath().indexOf("proto\\terminal\\") == -1)
//				continue;
            appending("protoc --java_out=./output/java "
                    + file.getPath().substring(root.length() + 1));
        }
        return this.write("java");
    }

    public static void main(String[] args) {
        JavaGenerate javaGenerate = new JavaGenerate();
        javaGenerate.runBat(javaGenerate.generate());
        List<File> javas = new ArrayList<File>();
        getLatestJavas(new File(outfile), javas);

        if (javas != null) {
            for (File file : javas) {
                String _source_path = file.getAbsolutePath();
                String _target_path = System.getProperty("user.dir") + "\\opentsp-location-core\\opentsp-location-protocol\\src"
                        + _source_path.substring(outfile.length() + 5);
                System.err.println(_target_path);
                copyFile(_source_path, _target_path);
            }
        }
    }

}
