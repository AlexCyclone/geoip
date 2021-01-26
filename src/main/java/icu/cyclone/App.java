package icu.cyclone;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Aleksey Babanin
 * @since 2021/01/26
 */
public class App {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            printUseMessage();
            return;
        }
        String sourceFilePath = args[0];
        String destinationFilePath = args[1];

        List<String> addressList = AddressReader.getAddressList(sourceFilePath);
        List<Map<String, String>> result = GeoIpResolver.getGeoIp(addressList);
        ListWriter.writeData(result, destinationFilePath);
    }

    private static void printUseMessage() {
        System.out.println("Incorrect parameters. Use:");
        System.out.println("    java -jar geoip.jar [sourceAddressListFile.txt] [geoIpInfoFilePath.csv]");
    }
}
