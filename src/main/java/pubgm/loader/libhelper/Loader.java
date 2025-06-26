package pubgm.loader.libhelper;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Loader {
    private String gamename;
    private String name;
    private int version;
    private String url;

    public Loader(String gamename, String name, int version, String url) {
        this.gamename = gamename;
        this.name = name;
        this.version = version;
        this.url = url;
    }

    public Loader() {

    }

    public String getGameName() {
        return gamename;
    }

    public String getName() {
        return name;
    }

    public void setGameName(String gamename) {
        this.name = gamename;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
