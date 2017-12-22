package evgeniy.kurinnoy.courseWorkDB;


public class User{
    private static User user;
    private int id;
    private String firstName;
    private String lastName;
    private String post;
    private int accessRight;

    public static final int ADMIN = 0;
    public static final int EXTENDS = 1;
    public static final int NORMAL = 2;

    public static User get(){
        if (user == null){
            user = new User();
        }
        return user;
    }
    private User(){
        setFirstName(new String());
        setLastName(new String());
        setPost(new String());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(int accessRight) {
        this.accessRight = accessRight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
