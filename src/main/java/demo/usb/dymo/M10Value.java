package demo.usb.dymo;

public class M10Value {

  /** Expected data size in bytes */
  public static int EXPECTED_DATA_LENGTH = 6;

  private final M10State state;

  private final M10Scale scale;

  private final double value;

  private double toValue(byte val0, byte val1) {
    return ((short) (val1 << 8 | val0)) / 10.0;
  }

  public M10Value(byte[] data) {
    if (data.length != EXPECTED_DATA_LENGTH) {
      throw new IllegalStateException("Unexpected data length");
    }

    this.state = M10State.toState(data[2]);
    this.scale = M10Scale.toScale(data[3]);
    this.value = toValue(data[4], data[5]);
  }

  public int getMultiplier() {
    return M10State.Negative.equals(state) ? -1 : 1;
  }

  public String toString() {
    switch (state) {
      case Maximum:
        return "Max Value " + scale.toString();
      default:
        return String.format("%0.2f %s", value * getMultiplier(), scale.toString());
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
          throw new IllegalArgumentException("Unknown value");
      }
    }
  }

  enum M10Scale {

    Ounces, Grams;

    public static M10Scale toScale(byte value) {
      switch (value) {
        case 2:
          return Grams;
        case 11:
          return Ounces;
        default:
          throw new IllegalArgumentException("Unknown value");
      }
    }
  }
}

// private static void print(byte[] data) {
// byte mode = data[2];
// byte scaling = data[3];
// byte val0 = data[4];
// byte val1 = data[5];

// short val = (short) (val1 << 8 | val0);

// System.out.printf("%02x %02x %02x %02x %d %f %f", mode, scaling, val0, val1,
// val, (val / 10.0),
// (val / 16.0) / 10.0);
// }
