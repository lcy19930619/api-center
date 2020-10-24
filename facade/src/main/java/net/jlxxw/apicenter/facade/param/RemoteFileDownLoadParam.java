package net.jlxxw.apicenter.facade.param;

import net.jlxxw.apicenter.facade.dto.RemoteExecuteReturnDTO;

import java.io.*;

/**
 * @author zhanxiumei
 */
public class RemoteFileDownLoadParam extends RemoteExecuteReturnDTO {
    /**
     *  文件名称
     */
    private String fileName;

    /**
     * 具体二进制数据
     */
    private byte[] data;


    /**
     * 返回下载数据
     * @param fileName
     * @param data
     */
    public void returnDownLoadData(String fileName,byte[] data){
        this.data = data;
        this.fileName = fileName;
    }

    /**
     * 返回下载数据
     * @param file
     * @throws IOException
     */
    public void returnDownLoadData(File file) throws IOException {
        this.fileName = file.getName();
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        data  = bos.toByteArray();
    }

    /**
     * 返回下载数据
     * @param filePath
     * @throws IOException
     */
    public void returnDownLoadData(String filePath) throws IOException {
        File file = new File(filePath);
        this.fileName = file.getName();
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        data  = bos.toByteArray();
    }

    /**
     * 返回下载数据
     * @param fileName
     * @param inputStream
     * @throws IOException
     */
    public void returnDownLoadData(String fileName, InputStream inputStream) throws IOException {
        this.fileName = fileName;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = inputStream.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        inputStream.close();
        bos.close();
        data  = bos.toByteArray();
    }


    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

}
