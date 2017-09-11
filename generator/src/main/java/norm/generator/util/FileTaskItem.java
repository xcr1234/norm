package norm.generator.util;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



public class FileTaskItem {
    @XStreamAsAttribute
    private String src;
    @XStreamAsAttribute
    private String target;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "FileTaskItem{" +
                "src='" + src + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
