package xyz.jhughes.socialmaps.server;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class PhotosServer {
    /**
     * @param localPath
     * @return A link on the WWW
     */
    public static String pushPhotoToServer(String localPath) {
        try {
            Map config = new HashMap();
            config.put("cloud_name", "social-earth");
            config.put("api_key", "663134884586125");
            config.put("api_secret", "4hDI05WpNGrg0be0TXhe31xeAaU");
            Cloudinary cloudinary = new Cloudinary(config);
            FileInputStream inputStream = new FileInputStream(localPath);
            String[] imagesParts = localPath.split("/");
            Map map = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
            return (String) map.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
