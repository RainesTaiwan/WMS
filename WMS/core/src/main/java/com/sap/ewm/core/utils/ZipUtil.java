package com.sap.ewm.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void zip( File zipFile, String zipDir ) throws Exception {
        zipFile.createNewFile();
        FileOutputStream fos = new FileOutputStream( zipFile );
        ZipOutputStream zos = new ZipOutputStream( fos );
        zip( zos, zipDir, new File( zipDir ) );
        zos.closeEntry();
        zos.close();
        fos.close();
    }

    public static void zip( ZipOutputStream zos, String zipDir, File file ) throws IOException {
        if( file.isDirectory() ){
            File[] flist = file.listFiles();
            for( File f : flist ){
                zip( zos, zipDir, f );
            }
        }else{
            if( file.getPath().equals( zipDir ) ){
                zos.putNextEntry( new ZipEntry( file.getName() ) );
            }else{
                String fileName = file.getPath().substring( zipDir.length() + 1 );
                zos.putNextEntry( new ZipEntry( fileName ) );
            }
            FileInputStream fis = new FileInputStream( file );
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            fis.close();
        }
    }

    public static void unZip( File zipFile, String outDir ) throws Exception {
        FileInputStream fis = new FileInputStream( zipFile );
        ZipInputStream zis = new ZipInputStream( fis );
        unZip( zis, outDir );
        fis.close();
        zis.close();
    }

    public static void unZip( ZipInputStream zis, String outDir ) throws IOException {
        ZipEntry entry = zis.getNextEntry();
        if( entry != null ){
            File file = new File( outDir + File.separator + entry.getName() );
            if( entry.isDirectory() ){
                file.mkdirs();
                unZip( zis, outDir );
            }else{
                File parentFile = file.getParentFile();
                parentFile.mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream( file );
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = zis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                zis.closeEntry();
                unZip( zis, outDir );
            }
        }
    }
}
