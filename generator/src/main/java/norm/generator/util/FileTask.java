package norm.generator.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;


public class FileTask {

    @XStreamImplicit(itemFieldName = "file")
    private List<FileTaskItem> fileTaskItemList;

    public List<FileTaskItem> getFileTaskItemList() {
        return fileTaskItemList;
    }

    @Override
    public String toString() {
        return "FileTask{" +
                "fileTaskItemList=" + fileTaskItemList +
                '}';
    }

    public void setFileTaskItemList(List<FileTaskItem> fileTaskItemList) {
        this.fileTaskItemList = fileTaskItemList;
    }
}
