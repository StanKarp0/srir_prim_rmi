package utils;

public class Tools {

    private static final String host = "localhost";
    private static final String remoteName = "rmi://" + host + "/server_";

    public static String getServerPath(int remote) {
        return remoteName + remote;
    }

}
