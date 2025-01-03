package com.fw.wcs.core.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Matcher;

@Component
@ConditionalOnProperty( prefix = "ftp", name = { "server", "port", "username", "password", "uploadDir", "downloadDir" }, matchIfMissing = false )
public class FtpClient {

    private FTPClient ftp;

    @Value("${ftp.server}")
    private String address;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String userName;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.uploadDir}")
    private String uploadDir;

    @Value("${ftp.downloadDir}")
    private String downloadDir;

    /**Ftp協議字元編碼*/
    private String serverCharset = "ISO-8859-1";

    /**本體字元編碼*/
    private String localCharset = "GBK";

    /**
     *
     * @param path 上傳到ftp伺服器哪個路徑下
     * @param addr 地址
     * @param port 埠號
     * @param username 使用者名稱
     * @param password 密碼
     * @return
     * @throws Exception
     */
    private boolean connect() throws Exception {
        boolean result = false;
        int reply;
        ftp = new FTPClient();
        ftp.connect(address,port);
        ftp.login(userName,password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory( File.separator );
        ftp.enterLocalPassiveMode();
        result = true;
        return result;
    }

    /**
     * 上傳檔案
     *
     * @param file
     * @throws Exception
     */
    public void upload( File file ) throws Exception{
        connect();
        _upload( file );
        closeConnect();
    }

    /**
     * 上傳檔案
     *
     * @param remotePath
     * @param file
     * @throws IOException
     */
    public void upload( String remotePath, File file ) throws Exception{
        connect();
        _upload( remotePath, file );
        closeConnect();
    }

    /**
     * 下載檔案
     *
     * @param remotePath
     * @param localPath
     * @throws Exception
     */
    public void download( String remotePath, String localPath ) throws Exception{
        connect();
        _download( remotePath, localPath );
        closeConnect();
    }

    /**
     * ftp下載檔案到本地
     *
     * @param remoteFile
     * @param localFile
     * @throws Exception
     */
    public void download( String remoteFile, File localFile ) throws Exception{
        connect();
        _download( remoteFile, localFile );
        closeConnect();
    }

    /**
     * 刪除檔案
     *
     * @param remotePath
     * @throws IOException
     */
    public void deleteFile( String remotePath ) throws Exception{
        connect();
        _deleteFile( remotePath );
        closeConnect();
    }

    public void deleteFile1( String filePath ) throws Exception{
        connect();
        boolean b =ftp.deleteFile( filePath );
        closeConnect();
    }

    public void mkdirs( String remotePath ) throws Exception {
        connect();
        String[] paths = remotePath.split( Matcher.quoteReplacement(File.separator) );
        for( String p : paths ){
            ftp.makeDirectory( p );
            ftp.changeWorkingDirectory( p );
        }
        closeConnect();
    }

    private void _upload( File file ) throws Exception{
        if(file.isDirectory()){
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath()+ File.separator +files[i] );
                _upload(file1);
            }
            ftp.changeToParentDirectory();
        }else{
            FileInputStream input = new FileInputStream(file);
            ftp.storeFile(file.getName(), input);
            input.close();
        }
    }


    private void _upload( String remotePath, File file ) throws Exception {
        String[] paths = remotePath.split( Matcher.quoteReplacement(File.separator) );
        for( String p : paths ){
            ftp.makeDirectory( p );
            ftp.changeWorkingDirectory( p );
        }
        _upload( file );
    }

    private void _download( String remotePath, String localPath ) throws Exception {
       FTPFile[] ftpFiles = ftp.listFiles( remotePath );
        if( ftpFiles.length > 0 ){
            for( FTPFile ftpFile : ftpFiles ){
                if( ftpFile.isDirectory() ){
                    _download( remotePath + File.separator + ftpFile.getName(), localPath + File.separator + ftpFile.getName() );
                }else{
                    File localDir = new File( localPath );
                    localDir.mkdirs();

                    File localFile = new File( localPath + File.separator + ftpFile.getName() );
                    localFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream( localFile );
                    ftp.retrieveFile( remotePath + File.separator + ftpFile.getName(), fos );
                    fos.flush();
                    fos.close();
                }
            }
        }
    }


    private void _download( String remoteFile, File localFile ) throws Exception {

        localFile.createNewFile();
        FileOutputStream fos = new FileOutputStream( localFile );
        ftp.retrieveFile( remoteFile, fos );
        fos.flush();
        fos.close();
    }


    private void _deleteFile( String remotePath ) throws Exception {

        FTPFile[] ftpFiles = ftp.listFiles( remotePath );
        boolean b = false;
        if( ftpFiles.length > 0 ){
            for( FTPFile ftpFile : ftpFiles ){
                if( ftpFile.isDirectory() ){
                    _deleteFile( remotePath + File.separator + ftpFile.getName() );
                    b = ftp.removeDirectory( remotePath + File.separator + ftpFile.getName() );
                }else{
                    b= ftp.deleteFile( remotePath + File.separator + ftpFile.getName() );
                }
            }
        }
    }

    private String convertFtpCharset( String fileName ){
        try {
            return new String( fileName.getBytes( localCharset ), serverCharset );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private String convertLocalCharset( String fileName ){
        try {
            return new String( fileName.getBytes( serverCharset ), localCharset );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void closeConnect() {
        try{
            if( ftp != null && ftp.isConnected() ){
                ftp.logout();
                ftp.disconnect();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUploadDir(){
        return uploadDir;
    }

    public String getDownloadDir(){
        return downloadDir;
    }

}
