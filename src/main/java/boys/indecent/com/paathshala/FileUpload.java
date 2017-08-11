package boys.indecent.com.paathshala;

/**
 * Created by nSpider on 29-Jul-17.
 */

public class FileUpload {
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public FileUpload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public FileUpload(){}
}
