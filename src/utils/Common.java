package utils;

public class Common {

    private static final String host = "localhost";
    private static final String remoteName = "rmi://" + host + "/server_";

    public static String getServerPath(int remote) {
        return remoteName + remote;
    }

}
