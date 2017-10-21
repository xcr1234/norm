package norm.generator.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;


public class FileTask {

    @XStreamImplicit(itemFieldName = "file")
    private List<FileTaskItem> fileTaskItemList;

    @XStreamAsAttribute
    private String encoding;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public List<FileTaskItem> getFileTaskItemList() {
        return fileTaskItemList;
    }



    public void setFileTaskItemList(List<FileTaskItem> fileTaskItemList) {
        this.fileTaskItemList = fileTaskItemList;
    }
}
