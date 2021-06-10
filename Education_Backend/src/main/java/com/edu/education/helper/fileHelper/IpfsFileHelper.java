package com.edu.education.helper.fileHelper;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * FileHelper
 * 上传文件
 * 需要更改dirPath 和 ipfsPath
 */
@Component
public class IpfsFileHelper {
    @Value(value = "${com.tempPath}")
    private String dirPath;
    @Value(value = "${com.ipfsPath}")
    private String ipfsPath;

    public IpfsFileHelper(String dirPath, String ipfsPath){
        this.dirPath = dirPath;
        this.ipfsPath = ipfsPath;
    }

    public IpfsFileHelper(){
        super();
    }

    /**
     * upload
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public String upload(MultipartFile multipartFile) throws Exception{
        if(multipartFile.isEmpty()){
            return null;
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File tempDir = new File(dirPath);
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }
        final File tempFile = File.createTempFile(UUID.randomUUID().toString(), prefix, tempDir);

        System.out.println("tempFile at:"+tempFile.getAbsolutePath());
        // MultipartFile to File
        multipartFile.transferTo(tempFile);
        IPFS ipfs = new IPFS(ipfsPath);
        ipfs.refs.local();
        //保存上传文件
        NamedStreamable.FileWrapper savefile = new NamedStreamable.FileWrapper(tempFile);
        //Multihash addResult = ipfs.add(savefile);
        MerkleNode result = ipfs.add(savefile).get(0);
        Multihash multihash = result.hash;
        //System.out.println(multihash);
        //删除临时文件
        deleteFile(tempFile);
        //返回结果里面获取保存文件的唯一hash，基于文件内容的 hash
        return multihash.toString();
    }

    /**
     * 删除
     *
     * @param files 文件
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}