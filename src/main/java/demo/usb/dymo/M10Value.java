package demo.usb.dymo;

public class M10Value {

  /** Expected data size in bytes */
  public static int EXPECTED_DATA_LENGTH = 6;

  private final M10State state;

  private final M10Mode mode;

  private final double value;

  private double toValue(byte val0, byte val1) {
    return ((short) ((short) val1 << 8 | val0)) / 10.0;
  }

  public M10Value(byte[] data) {
    if (data.length != EXPECTED_DATA_LENGTH) {
      throw new IllegalStateException("Unexpected data length " + data.length);
    }

    System.out.printf("%02X %02X %02X %02X %02X %02X\n",
      data[0], data[1], data[2], data[3], data[4], data[5]);

    this.state = M10State.toState(data[1]);
    this.mode = M10Mode.toMode(data[2]);
    this.value = toValue(data[4], data[5]);
  }

  public int getMultiplier() {
    return M10State.Negative.equals(state) ? -1 : 1;
  }

  @Override
  public String toString() {
    switch (state) {
      case Maximum:
        return "Max Value " + mode.toString();
      default:
        return String.format("%0.2f %s", value * getMultiplier(), mode.toString());
    }
  }

  enum M10State {

    Zero, Positive, Negative, Maximum;

    public static M10State toState(byte value) {
      switch (value) {
        case 2:
          return Zero;
        case 4:
          return Positive;
        case 5:
          return Negative;
        case 6:
          return Maximum;
        default:
          throw new IllegalArgumentException("Unknown state value " + value);
      }
    }
  }

  enum M10Mode {

    Ounces, Grams;

    public static M10Mode toMode(byte value) {
      switch (value) {
        case 2:
          return Grams;
        case 11:
          return Ounces;
        default:
          throw new IllegalArgumentException("Unknown mode value " + value);
      }
    }
  }
}
