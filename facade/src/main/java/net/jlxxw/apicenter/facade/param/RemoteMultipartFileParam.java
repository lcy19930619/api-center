package net.jlxxw.apicenter.facade.param;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * 远程文件上传
 * @author zhanxiumei
 */
public class RemoteMultipartFileParam extends RemoteExecuteParam implements MultipartFile, Serializable {

    /**
     * 原始文件名称
     */
    private String originalFilenam;

    /**
     * 文件数据信息
     */
    private byte[] data;


    /**
     * Return a Resource representation of this MultipartFile. This can be used
     * as input to the {@code RestTemplate} or the {@code WebClient} to expose
     * content length and the filename along with the InputStream.
     *
     * @return this MultipartFile adapted to the Resource contract
     * @since 5.1
     */
    @Override
    public Resource getResource() {
        return null;
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>The default implementation simply copies the file input stream.
     *
     * @param dest
     * @see #getInputStream()
     * @see #transferTo(File)
     * @since 5.1
     */
    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {

    }

    /**
     * Return the name of the parameter in the multipart form.
     *
     * @return the name of the parameter (never {@code null} or empty)
     */
    @Override
    public String getName() {
        return originalFilenam;
    }


    @Override
    public String getOriginalFilename() {
        return originalFilenam;
    }

    /**
     * Return the content type of the file.
     *
     * @return the content type, or {@code null} if not defined
     * (or no file has been chosen in the multipart form)
     */
    @Override
    public String getContentType() {
        return null;
    }

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(data);
    }

    /**
     * Return the size of the file in bytes.
     *
     * @return the size of the file, or 0 if empty
     */
    @Override
    public long getSize() {
        return data.length;
    }

    /**
     * Return the contents of the file as an array of bytes.
     *
     * @return the contents of the file as bytes, or an empty byte array if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @Override
    public byte[] getBytes() throws IOException {
        return data;
    }

    /**
     * Return an InputStream to read the contents of the file from.
     * <p>The user is responsible for closing the returned stream.
     *
     * @return the contents of the file as stream, or an empty stream if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }


    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }

    public void setOriginalFilenam(String originalFilenam) {
        this.originalFilenam = originalFilenam;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
