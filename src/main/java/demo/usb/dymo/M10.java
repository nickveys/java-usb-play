package demo.usb.dymo;

import java.util.Arrays;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

public class M10 implements AutoCloseable {

  private static final short VID = (short) 0x0922;

  private static final short PID = (short) 0x8003;

  private final UsbInterface iface;

  private final UsbPipe pipe;

  public static boolean isDymoM10(UsbDevice device) {
    UsbDeviceDescriptor udd = device.getUsbDeviceDescriptor();
    return udd.idVendor() == VID && udd.idProduct() == PID;
  }

  public M10(UsbDevice device) throws UsbException {
    UsbConfiguration config = device.getActiveUsbConfiguration();
    List<UsbInterface> ifs = config.getUsbInterfaces();
    System.out.println(ifs);

    iface = ifs.get(0);
    iface.claim();
    List<UsbEndpoint> ends = iface.getUsbEndpoints();
    System.out.println(ends);

    UsbEndpoint end = ends.get(0);
    pipe = end.getUsbPipe();
    pipe.open();
  }

  @Override
  public void close() throws UsbException {
    pipe.close();
    iface.release();
  }

  public M10Value read() throws UsbException {
    byte[] data = new byte[M10Value.EXPECTED_DATA_LENGTH];
    int num = pipe.syncSubmit(data);
    System.out.println(num);
    System.out.println(Arrays.toString(data));
    return new M10Value(data);
  }
}
