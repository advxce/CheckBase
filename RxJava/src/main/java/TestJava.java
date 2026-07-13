public class TestJava {

    static void uploadUserName(UserJava user){
        user.name = "Oleg";
    }

    static void changeName(UserJava user){
        user = new UserJava("Petya");
        System.out.println("user2:"+ user.name);
    }

    public static void main(String[] args){

        UserJava user = new UserJava("Dima");
        System.out.println("name:"+user.name);
        uploadUserName(user);
        System.out.println("name:"+user.name);
        changeName(user);
        System.out.println("name:"+user.name);
    }



}


class UserJava{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

   UserJava(String name){
       this.name = name;
   }

}