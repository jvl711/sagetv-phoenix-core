package test;

import sagex.SageAPI;

import java.io.IOException;

public class TestDiag {
    public static void main(String args[]) throws IOException {
        SageAPI.setProvider(SageAPI.getRemoteProvider());

        String report = phoenix.api.RunPhoenixDiagnostics("detailed2");
        System.out.println(report);
    }
}
