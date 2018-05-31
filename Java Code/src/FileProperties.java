import java.io.Serializable;
import java.util.NoSuchElementException;

public class FileProperties implements Serializable {
    private String fileName;
    private String status;
    private String size;
    private String created;
    private String modified;
    private String address;

    public FileProperties(String fileName, String status, String size, String created, String address) {
        this.fileName = fileName;
        this.status = "not completed"; // Statuses are completed and pending and pause
        this.size = size;
        this.created = created;
        this.modified = created; // Initially modified date is equal to created time but later may be changed
        this.address = address;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return fileName + " " + status + " " + size + " " + created + " " + modified + " " + address;
    }
    public String get(String item){
        switch (item){
            case "name":
                return fileName;
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
