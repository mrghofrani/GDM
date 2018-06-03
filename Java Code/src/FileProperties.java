import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class FileProperties implements Serializable {
    private String fileUrl;
    private String fileName;
    private String status;
    private String size;
    private String created;
    private String modified;
    private String address;
    private final ArrayList<String> UNITS = new ArrayList<>(Arrays.asList("Bytes" , "KB","MB", "GB"));

    /**
     * The constructor of the class which initializes the following parameters
     * @param fileUrl the web address of the file
     * @param status status of the file which could be completed or not
     * @param size size of the file
     * @param created created date
     * @param address address in the hardware
     */
    public FileProperties(String fileUrl, String status, String size, String created, String address) {
        this.fileUrl = fileUrl;
        fileName = System.currentTimeMillis() + fileUrl.substring(fileUrl.lastIndexOf('.'));
        this.status = "not completed"; // Statuses are completed and pending and pause
        this.size = size;
        this.created = created;
        this.modified = created; // Initially modified date is equal to created time but later may be changed
        this.address = address;
    }

    /**
     * @return FileUrl which is the webpage address of the class
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @return status of the file
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status of the file
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return size of the
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size set a size for the file
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the fileName of the file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName name of the file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return time of the created file
     */
    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    /**
     * @return address of the file in hardware
     */
    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return fileUrl + " " + status + " " + size + " " + created + " " + modified + " " + address;
    }

    /**
     * this method accepts the field name
     * and returns its value
     */
    public String get(String item){
        switch (item){
            case "name":
                return fileUrl;
            case "status":
                return status;
            case "size":
                return size;
            case "date":
                return created;
                default:
                    throw new NoSuchElementException();
        }
    }
}
