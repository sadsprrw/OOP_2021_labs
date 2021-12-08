package models;

import java.util.Collections;
import java.util.List;

public class Utils {
    public static void sortTariffs(List<Device> devices) {
        Collections.sort(devices, new DeviceComparator());
    }
}