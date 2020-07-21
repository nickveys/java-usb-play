package demo.usb;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;
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

  // private static void go2(UsbDevice device) throws Exception {
  //   UsbConfiguration config = device.getActiveUsbConfiguration();
  //   List<UsbInterface> ifaces = config.getUsbInterfaces();
  //   System.out.println(ifaces);

  //   try (UsbInterfaceClaimer claimer = new UsbInterfaceClaimer(ifaces.get(0))) {
  //     List<UsbEndpoint>
  //   }
  // }

  // private static void go(UsbDevice device) throws Exception {
  //   UsbConfiguration config = device.getActiveUsbConfiguration();
  //   List<UsbInterface> ifs = config.getUsbInterfaces();
  //   System.out.println(ifs);
  //   UsbInterface iface = ifs.get(0);
  //   try {
  //     iface.claim();
  //     List<UsbEndpoint> ends = iface.getUsbEndpoints();
  //     System.out.println(ends);
  //     UsbEndpoint end = ends.get(0);
  //     UsbPipe pipe = end.getUsbPipe();
  //     try {
  //       pipe.open();
  //       byte[] data = new byte[6];
  //       int num = pipe.syncSubmit(data);
  //       System.out.println(num);
  //       System.out.println(Arrays.toString(data));
  //       print(data);
  //     } finally {
  //       pipe.close();
  //     }
  //   } finally {
  //     iface.release();
  //   }
  // }

  // private static boolean isDymoScale(UsbDevice device) {
  //   UsbDeviceDescriptor udd = device.getUsbDeviceDescriptor();
  //   return udd.idVendor() == VID && udd.idProduct() == PID;
  // }

  // private static void print(byte[] data) {
  //   byte mode = data[2];
  //   byte scaling = data[3];
  //   byte val0 = data[4];
  //   byte val1 = data[5];

  //   short val = (short) (val1 << 8 | val0);

  //   System.out.printf("%02x %02x %02x %02x %d %f %f", mode, scaling, val0, val1, val, (val / 10.0),
  //       (val / 16.0) / 10.0);
  // }
}
