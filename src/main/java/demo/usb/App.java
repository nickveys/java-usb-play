package demo.usb;

import java.util.List;
import java.util.Optional;

import javax.usb.UsbDevice;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import demo.usb.dymo.M10;

public class App {

  public static void main(String[] args) throws Exception {
    UsbServices services = UsbHostManager.getUsbServices();
    UsbHub hub = services.getRootUsbHub();
    List<UsbDevice> devices = hub.getAttachedUsbDevices();
    Optional<UsbDevice> maybeDevice = devices.stream().filter(M10::isDymoM10).findAny();

    if (maybeDevice.isPresent()) {
      UsbDevice device = maybeDevice.get();
      System.out.println("Scale found: " + device);
      M10 m10 = new M10(device);
      System.out.println(m10.read());
    } else {
      System.out.println("Scale not found!");
    }
  }
}
